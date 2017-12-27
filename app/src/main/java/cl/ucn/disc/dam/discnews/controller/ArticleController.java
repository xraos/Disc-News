package cl.ucn.disc.dam.discnews.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import cl.ucn.disc.dam.discnews.model.Article;
import cl.ucn.disc.dam.discnews.model.NewsApi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Clase Principal que contiene los metodos de acceso a las noticias.
 */

@Slf4j
public final class ArticleController {
    /**
     * Des-Serializador GSON
     */
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Article.class, new ArticleAdapter())
            .setPrettyPrinting() // TODO: Eliminar en modo produccion
            .create();

    /**
     * API Key
     */
    private static final String apiKey = "8dd673e94a9e4086a41b4cde0b6aa1c5";

    /**
     * URL desde donde se obtendran los {@link Article}.
     */
    private static final String url = "https://newsapi.org/v2/top-headlines?";

    /**
     * Cliente OkHttp
     */
    private final OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * Obtencion de {@link Article}s desde Internet.
     *
     * @return the {@link List} of {@link Article}.
     */
    public List<Article> getArticles(final String source) throws IOException {

        // Cronometro
        final StopWatch stopWatch = StopWatch.createStarted();

        // URL to get news
        final String apiUrl = url + "sources=" + source + "&sortBy=latest&apiKey=" + apiKey;

        log.debug("Getting Articles, using url: {}", apiUrl);

        // Peticion
        final Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        // Respuesta
        final Response response = okHttpClient.newCall(request).execute();
        final String json = response.body().string();

        final NewsApi newsApi = gson.fromJson(json, NewsApi.class);

        // Fix de la fecha
        for (final Article article : newsApi.getArticles()) {

            // Fix del articulo
            Article.fix(article);
            // log.debug("DateFixed: {}", article.getPublishedAtDateTime());

        }

        // Returning the data!
        try {
            return newsApi.getArticles();
        } finally {
            log.debug("GetArticles in: {}", stopWatch);
        }
    }

    /**
     * Des-Serializador de {@link Article} via adaptador.
     */
    public static class ArticleAdapter implements JsonDeserializer<Article> {

        /**
         * Gson invokes this call-back method during deserialization when it encounters a field of the
         * specified type.
         * <p>In the implementation of this call-back method, you should consider invoking
         * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
         * for any non-trivial field of the returned object. However, you should never invoke it on the
         * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
         * call-back method again).
         *
         * @param json    The Json data being deserialized
         * @param typeOfT The type of the Object to deserialize to
         * @param context
         * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
         * @throws JsonParseException if json is not in the expected format of {@code typeofT}
         */
        @Override
        public Article deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            final JsonObject jsonObject = json.getAsJsonObject();

            final Date publishedAt = context.deserialize(jsonObject.get("publishedAt"), Date.class);

            final Article article = Article.builder()
                    .author(getString(jsonObject, "author"))
                    .title(getString(jsonObject , "title"))
                    .description(getString(jsonObject, "description"))
                    .url(getString(jsonObject, "url"))
                    .urlToImage(getString(jsonObject,"urlToImage"))
                    .publishedAt(publishedAt)
                    .source(context.deserialize(jsonObject.get("source"), Article.Source.class))
                    .build();

            Article.fix(article);

            return article;
        }

        /**
         *
         * @param jsonObject
         * @param key
         * @return null or data if exists.
         */
        private String getString(final JsonObject jsonObject, final String key) {

            final JsonElement json = jsonObject.get(key);

            if (!json.isJsonNull()) {
                return json.getAsString();
            }

            return null;
        }


      }
    }
