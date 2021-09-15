import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './film-source.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FilmSourceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const filmSourceEntity = useAppSelector(state => state.filmSource.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="filmSourceDetailsHeading">
          <Translate contentKey="filmSourceApp.filmSource.detail.title">FilmSource</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{filmSourceEntity.id}</dd>
          <dt>
            <span id="lastname">
              <Translate contentKey="filmSourceApp.filmSource.lastname">Lastname</Translate>
            </span>
          </dt>
          <dd>{filmSourceEntity.lastname}</dd>
          <dt>
            <span id="firstname">
              <Translate contentKey="filmSourceApp.filmSource.firstname">Firstname</Translate>
            </span>
          </dt>
          <dd>{filmSourceEntity.firstname}</dd>
          <dt>
            <span id="position">
              <Translate contentKey="filmSourceApp.filmSource.position">Position</Translate>
            </span>
          </dt>
          <dd>{filmSourceEntity.position}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="filmSourceApp.filmSource.location">Location</Translate>
            </span>
          </dt>
          <dd>{filmSourceEntity.location}</dd>
          <dt>
            <span id="rate">
              <Translate contentKey="filmSourceApp.filmSource.rate">Rate</Translate>
            </span>
          </dt>
          <dd>{filmSourceEntity.rate}</dd>
        </dl>
        <Button tag={Link} to="/film-source" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/film-source/${filmSourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FilmSourceDetail;
