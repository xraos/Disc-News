package cl.ucn.disc.dam.discnews.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.converter.TypeConverter;

import cl.ucn.disc.dam.discnews.model.Article;

/**
 * Created by RaosF on 27-12-2017.
 */

public final class SourceConverter extends TypeConverter<String, Article.Source> {

    /**
     * Des-Serializador GSON
     */
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    /**
     * Converts the ModelClass into a DataClass
     *
     * @param model this will be called upon syncing
     * @return The DataClass value that converts into a SQLite type
     */
    @Override
    public String getDBValue(Article.Source model) {
        return gson.toJson(model);
    }

    /**
     * Converts a DataClass from the DB into a ModelClass
     *
     * @param data This will be called when the model is loaded from the DB
     * @return The ModelClass value that gets set in a Model that holds the data class.
     */
    @Override
    public Article.Source getModelValue(String data) {
        return gson.fromJson(data, Article.Source.class);
    }
}
