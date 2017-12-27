package cl.ucn.disc.dam.discnews.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.UUID;

import cl.ucn.disc.dam.discnews.dao.AppDatabase;
import cl.ucn.disc.dam.discnews.dao.SourceConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@Table(database = AppDatabase.class)
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    /**
     * ID
     */
    @PrimaryKey
    @Getter
    UUID id;

    /**
     * Author
     */
    @Getter
    @Column
    String author;

    /**
     * Title
     */
    @Getter
    @Column
    String title;

    /**
     * Description
     */
    @Getter
    @Column
    String description;

    /**
     * URL: main link
     */
    @Getter
    @Column
    String url;

    /**
     * URL: link to image
     */
    @Getter
    @Column
    String urlToImage;

    /**
     * Date: 2017-11-16T19:40:25Z
     */
    @Getter
    @Column
    Date publishedAt;

    /**
     * Source
     */
    @Getter
    @Column(typeConverter = SourceConverter.class)
    Source source;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);

    }

    /**
     * Internal article source.
     */
    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static final class Source {

        /**
         *
         */
        @Getter
        String id;

        /**
         *
         */
        @Getter
        String name;

        /**
         * @return the String representation.
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }

    }

}