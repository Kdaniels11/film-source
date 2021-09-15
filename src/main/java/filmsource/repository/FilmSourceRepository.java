package filmsource.repository;

import filmsource.domain.FilmSource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FilmSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilmSourceRepository extends JpaRepository<FilmSource, Long> {}
