package cl.ucn.disc.dam.discnews.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import cl.ucn.disc.dam.discnews.model.Article;
import cl.ucn.disc.dam.discnews.model.NewsApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Clase Principal que contiene los metodos de acceso a las noticias.
 */

public class NewsController {

    /**
     *
     */
    public static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    /**
     * URL desde donde se obtendra los articulos
     */
    final String url = "https://newsapi.org/v1/articles?source=techcrunch&apiKey=b4748897fc9f461495692c6e2237be12";

    /**
     * Cliente Okhttp
     */

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Obtencion the {@link Article}s desde internet.
     * @return the {@link List} of {@link Article}
     */

    public List<Article> getArticles() throws IOException {
        // Peticion
        final Request request = new Request.Builder()
                .url(url)
                .build();

        // Repuesta
        final Response response = client.newCall(request).execute();
        final String json = response.body().string();

        final NewsApi newsapi = gson.fromJson(json, NewsApi.class);

        return newsapi.getArticles();
    }

}
