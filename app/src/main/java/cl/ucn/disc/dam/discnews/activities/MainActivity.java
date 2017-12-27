package cl.ucn.disc.dam.discnews.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import cl.ucn.disc.dam.discnews.R;
import cl.ucn.disc.dam.discnews.adapters.ArticleDBFlowAdapter;
import cl.ucn.disc.dam.discnews.tasks.GetSaveArticlesTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MainActivity extends ListActivity implements GetSaveArticlesTask.TaskListener, ShakeDetector.Listener {

    /**
     * Adapter de {@link cl.ucn.disc.dam.discnews.model.Article}.
     */
    private BaseAdapter articleAdapter;

    /**
     * Running background task
     */
    private GetSaveArticlesTask getSaveArticlesTask;

    /**
     * Shake Detector
     */
    private ShakeDetector shakeDetector;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mostrar la barrita
        final ActionBar actionBar = super.getActionBar();
        if (actionBar != null) {
            // actionBar.setLogo(R.drawable.ic_launcher_foreground);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        // Row division
        int[] colors = {0, 0xFFFF0000, 0};
        this.getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        this.getListView().setDividerHeight(5);

        // Adaptador de articles
        this.articleAdapter = new ArticleDBFlowAdapter(this);
        super.setListAdapter(this.articleAdapter);

        // Si no hay articulos en el adaptador (y por lo tanto en la base de datos) ..
        if (this.articleAdapter.isEmpty()) {
            // .. ejecuto la tarea para obtenerlas.
            this.runGetAndSaveArticlesTask();
        }

        // Detector de terremotos
        this.shakeDetector = new ShakeDetector(this);

    }

    /**
     * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onCreate
     * @see #onStop
     * @see #onResume
     */
    @Override
    protected void onStart() {
        super.onStart();

        // Al iniciar la aplicación, inicio la deteccion de terremotos
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector.start(sensorManager);

        // Show the database size!
        Toast.makeText(this, "Articles in BD: " + this.articleAdapter.getCount(), Toast.LENGTH_SHORT).show();

    }

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onRestart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestart
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onDestroy
     */
    @Override
    protected void onStop() {
        super.onStop();

        // Detengo el detector de sismos
        shakeDetector.stop();
    }

    /**
     * Metodo que realiza la ejecucion en segundo plano de la tarea que obtiene los
     * {@link cl.ucn.disc.dam.discnews.model.Article} desde Internet.
     */
    private void runGetAndSaveArticlesTask() {

        // Si ya hay una tarea de obtencion de articulos corriendo no ejecuto una nueva!
        if (this.getSaveArticlesTask != null) {
            Toast.makeText(this, "Already downloading Articles ..", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show little message
        Toast.makeText(this, "Downloading Articles ..", Toast.LENGTH_LONG).show();

        // Inicio la tarea
        log.debug("Starting GetSaveArticlesTask ..");
        this.getSaveArticlesTask = new GetSaveArticlesTask(this);
        this.getSaveArticlesTask.execute();

    }

    /**
     * Aviso que se termino la obtencion de los {@link cl.ucn.disc.dam.discnews.model.Article}.
     *
     * @param newsArticles numero de articulos nuevos obtenidos.
     */
    @Override
    public void taskFinished(Integer newsArticles) {

        // Show little message
        Toast.makeText(this, "New Articles: " + newsArticles, Toast.LENGTH_LONG).show();

        log.debug("Finished!");
        this.articleAdapter.notifyDataSetChanged();

        // Clean the task!
        this.getSaveArticlesTask = null;

    }

    /**
     * Called on the main thread when the device is shaken.
     */
    @Override
    public void hearShake() {

        // Vibro para indicar que se detecto el terremoto
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(150);
        }

        this.runGetAndSaveArticlesTask();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_reload: {
                this.runGetAndSaveArticlesTask();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);

    }
}
