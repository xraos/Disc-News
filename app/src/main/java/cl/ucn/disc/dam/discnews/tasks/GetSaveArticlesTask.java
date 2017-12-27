package cl.ucn.disc.dam.discnews.tasks;

import android.os.AsyncTask;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.List;

import cl.ucn.disc.dam.discnews.controller.ArticleController;
import cl.ucn.disc.dam.discnews.model.Article;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by RaosF on 27-12-2017.
 */

@Slf4j
public final class GetSaveArticlesTask extends AsyncTask<Void, Void, Integer> {

    /**
     * Listener de las tareas a terminar.
     */
    private TaskListener taskListener;

    /**
     * @param taskListener
     */
    public GetSaveArticlesTask(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param voids The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Integer doInBackground(Void... voids) {

        // Getting from Internet
        final List<Article> articles = getArticles();

        // Saving in database
        if (articles != null && articles.size() != 0) {

            log.debug("Saving {} Articles in database ..", articles.size());

            // Cronometro
            final StopWatch stopWatch = StopWatch.createStarted();

            final ModelAdapter<Article> modelAdapter = FlowManager.getModelAdapter(Article.class);

            // Contador de nuevas noticias
            int saved = 0;
            for (final Article article : articles) {

                // Si la noticia ya existe, no es necesario almacenar nada.
                if (modelAdapter.exists(article)) {
                    continue;
                }

                // Inserto en la base de datos y cuento 1 mas.
                modelAdapter.insert(article);
                saved++;

            }
            log.debug("Saved {} new Articles in {}.", saved, stopWatch);

            return saved;

            /*
            // Version mas rapida de almacenaje, pero sin contar las inserciones.
            FastStoreModelTransaction<Article> fastStoreModelTransaction = FastStoreModelTransaction.saveBuilder(FlowManager.getModelAdapter(Article.class))
                    .addAll(articles).build();

            fastStoreModelTransaction.execute(FlowManager.getWritableDatabase(AppDatabase.class));
           */

        }

        return null;

    }

    /**
     * @return the {@link List} of {@link Article}.
     */
    private List<Article> getArticles() {

        // FIXME: Sera atributo de la clase?
        final ArticleController articleController = new ArticleController();

        try {
            // Obtengo los articles desde internet via el controlador
            return articleController.getArticles("techcrunch,ars-technica,engadget,buzzfeed,wired");
        } catch (IOException e) {
            log.error("Error", e);
            return null;
        }
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param integer The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(Integer integer) {

        if (taskListener != null) {
            taskListener.taskFinished(integer);
        }

    }

    /**
     *
     */
    public interface TaskListener {

        /**
         * Aviso que se termino la obtencion de los {@link Article}.
         */
        void taskFinished(final Integer newsArticles);

    }

}

