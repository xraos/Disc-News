package cl.ucn.disc.dam.discnews.tasks;

import android.os.AsyncTask;

import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.List;

import cl.ucn.disc.dam.discnews.controller.NewsController;
import cl.ucn.disc.dam.discnews.model.Article;

/**
 * Created by RaosF on 09-11-2017.
 */

//@Slf4j
public final class GetArticlesTask extends AsyncTask<Void, Void, List<Article>> {

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
    protected List<Article> doInBackground(Void... voids) {

        // Cronometro
        final StopWatch stopWatch = StopWatch.createStarted();

       // log.debug("Running in background...");

        // FIXME: Sera atributo de la clase?
        final NewsController newsController = new NewsController();

        try {
            // Obtengo los articles desde internet via el controlador
            return newsController.getArticles();
        } catch (IOException e) {
            return null;
        } finally {
            // Cuanto tiempo demoro?
            //log.debug("Time: ()", stopWatch);

        }


    }
}
