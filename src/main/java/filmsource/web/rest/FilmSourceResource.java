package filmsource.web.rest;

import filmsource.domain.FilmSource;
import filmsource.repository.FilmSourceRepository;
import filmsource.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link filmsource.domain.FilmSource}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FilmSourceResource {

    private final Logger log = LoggerFactory.getLogger(FilmSourceResource.class);

    private static final String ENTITY_NAME = "filmSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilmSourceRepository filmSourceRepository;

    public FilmSourceResource(FilmSourceRepository filmSourceRepository) {
        this.filmSourceRepository = filmSourceRepository;
    }

    /**
     * {@code POST  /film-sources} : Create a new filmSource.
     *
     * @param filmSource the filmSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filmSource, or with status {@code 400 (Bad Request)} if the filmSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/film-sources")
    public ResponseEntity<FilmSource> createFilmSource(@RequestBody FilmSource filmSource) throws URISyntaxException {
        log.debug("REST request to save FilmSource : {}", filmSource);
        if (filmSource.getId() != null) {
            throw new BadRequestAlertException("A new filmSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilmSource result = filmSourceRepository.save(filmSource);
        return ResponseEntity
            .created(new URI("/api/film-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /film-sources/:id} : Updates an existing filmSource.
     *
     * @param id the id of the filmSource to save.
     * @param filmSource the filmSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filmSource,
     * or with status {@code 400 (Bad Request)} if the filmSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filmSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/film-sources/{id}")
    public ResponseEntity<FilmSource> updateFilmSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilmSource filmSource
    ) throws URISyntaxException {
        log.debug("REST request to update FilmSource : {}, {}", id, filmSource);
        if (filmSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filmSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filmSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilmSource result = filmSourceRepository.save(filmSource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filmSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /film-sources/:id} : Partial updates given fields of an existing filmSource, field will ignore if it is null
     *
     * @param id the id of the filmSource to save.
     * @param filmSource the filmSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filmSource,
     * or with status {@code 400 (Bad Request)} if the filmSource is not valid,
     * or with status {@code 404 (Not Found)} if the filmSource is not found,
     * or with status {@code 500 (Internal Server Error)} if the filmSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/film-sources/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FilmSource> partialUpdateFilmSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilmSource filmSource
    ) throws URISyntaxException {
        log.debug("REST request to partial update FilmSource partially : {}, {}", id, filmSource);
        if (filmSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filmSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filmSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilmSource> result = filmSourceRepository
            .findById(filmSource.getId())
            .map(
                existingFilmSource -> {
                    if (filmSource.getLastname() != null) {
                        existingFilmSource.setLastname(filmSource.getLastname());
                    }
                    if (filmSource.getFirstname() != null) {
                        existingFilmSource.setFirstname(filmSource.getFirstname());
                    }
                    if (filmSource.getPosition() != null) {
                        existingFilmSource.setPosition(filmSource.getPosition());
                    }
                    if (filmSource.getLocation() != null) {
                        existingFilmSource.setLocation(filmSource.getLocation());
                    }
                    if (filmSource.getRate() != null) {
                        existingFilmSource.setRate(filmSource.getRate());
                    }

                    return existingFilmSource;
                }
            )
            .map(filmSourceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, filmSource.getId().toString())
        );
    }

    /**
     * {@code GET  /film-sources} : get all the filmSources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filmSources in body.
     */
    @GetMapping("/film-sources")
    public List<FilmSource> getAllFilmSources() {
        log.debug("REST request to get all FilmSources");
        return filmSourceRepository.findAll();
    }

    /**
     * {@code GET  /film-sources/:id} : get the "id" filmSource.
     *
     * @param id the id of the filmSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filmSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/film-sources/{id}")
    public ResponseEntity<FilmSource> getFilmSource(@PathVariable Long id) {
        log.debug("REST request to get FilmSource : {}", id);
        Optional<FilmSource> filmSource = filmSourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(filmSource);
    }

    /**
     * {@code DELETE  /film-sources/:id} : delete the "id" filmSource.
     *
     * @param id the id of the filmSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/film-sources/{id}")
    public ResponseEntity<Void> deleteFilmSource(@PathVariable Long id) {
        log.debug("REST request to delete FilmSource : {}", id);
        filmSourceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
