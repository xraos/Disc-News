package cl.ucn.disc.dam.discnews.controller;

import android.app.ListActivity;
import android.os.Bundle;

import cl.ucn.disc.dam.discnews.tasks.GetArticlesTask;

public class MainActivity extends ListActivity {
    private final ArticleAdapter articleAdapter = new ArticleAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        super.setListAdapter(articleAdapter);

        final GetArticlesTask getArticlesTask = new GetArticlesTask();
        getArticlesTask.execute(articleAdapter);
    }


}
