package cl.ucn.disc.dam.discnews.model;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by RaosF on 27-12-2017.
 */

@Slf4j
public final class EntityUtils {

    /**
     * Can't instantiate!
     */
    private EntityUtils() {

    }

    /**
     *
     * @param dateStr
     * @return the {@link DateTime}.
     */
    public static DateTime parse(final String dateStr) {

        try {
            return ISODateTimeFormat.dateTimeNoMillis().parseDateTime(dateStr);
        } catch (IllegalArgumentException ex) {
            // Nothing here ..
        }

        try {
            return ISODateTimeFormat.dateTime().parseDateTime(dateStr);
        } catch (IllegalArgumentException ex) {
            // Nothing here ..
        }

        log.warn("Can't parse date {}, using defaults ..", dateStr);
        return new DateTime();

    }

}
