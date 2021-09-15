package filmsource.domain;

import static org.assertj.core.api.Assertions.assertThat;

import filmsource.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilmSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilmSource.class);
        FilmSource filmSource1 = new FilmSource();
        filmSource1.setId(1L);
        FilmSource filmSource2 = new FilmSource();
        filmSource2.setId(filmSource1.getId());
        assertThat(filmSource1).isEqualTo(filmSource2);
        filmSource2.setId(2L);
        assertThat(filmSource1).isNotEqualTo(filmSource2);
        filmSource1.setId(null);
        assertThat(filmSource1).isNotEqualTo(filmSource2);
    }
}
