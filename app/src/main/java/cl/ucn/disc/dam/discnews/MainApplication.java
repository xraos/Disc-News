package cl.ucn.disc.dam.discnews;

import android.app.Application;

import cl.ucn.disc.dam.discnews.tasks.GetArticlesTask;

/**
 * Created by RaosF on 09-11-2017.
 */

public class MainApplication extends Application {

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    @Override
    public void onCreate() {
        super.onCreate();

        final GetArticlesTask gaTask = new GetArticlesTask();
        gaTask.execute();


    }
}
