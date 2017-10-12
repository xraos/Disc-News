package cl.ucn.disc.dam.discnews.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Representa una Noticia en un instante de tiempo
 * para el sistema de DISC News
 *
 * @author Rodrigo Oyarzun Saire
 */

public final class Noticia {

    /**
     * Descripcion en una linea del titulo de la noticia
     */
    private String titulo;

    /**
     * Descripcion en 2 lineas y con un maximo de 140 chars
     */
    private String resumen;

    /**
     * Contenido completo de la Noticia
     */
    private String contenido;

    /**
     * Fecha de la noticia
     */
    private LocalDateTime fecha;

    /**
     * Icono de la noticia 64x64 pixels
     */
    private String icono;

    /**
     * Autor de la Noticia en formato: "Diego Urrutia <durrutia@ucn.cl>
     */

    private String autor;

    /**
     *
     * @return representacion en formato String de Noticia
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);

    }
}
