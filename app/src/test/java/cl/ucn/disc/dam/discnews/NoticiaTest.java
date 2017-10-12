package cl.ucn.disc.dam.discnews;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import cl.ucn.disc.dam.discnews.model.Noticia;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NoticiaTest {
    @Test
    public void testConstructor(){
        final Noticia noticia = new Noticia();
        Assertions.assertThat(noticia).isNotNull();
        Assertions.assertThat(noticia.getTitulo()).isNull();
    }
}