import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './film-source.reducer';
import { IFilmSource } from 'app/shared/model/film-source.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FilmSource = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const filmSourceList = useAppSelector(state => state.filmSource.entities);
  const loading = useAppSelector(state => state.filmSource.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="film-source-heading" data-cy="FilmSourceHeading">
        <Translate contentKey="filmSourceApp.filmSource.home.title">Film Sources</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="filmSourceApp.filmSource.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="filmSourceApp.filmSource.home.createLabel">Create new Film Source</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {filmSourceList && filmSourceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="filmSourceApp.filmSource.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="filmSourceApp.filmSource.lastname">Lastname</Translate>
                </th>
                <th>
                  <Translate contentKey="filmSourceApp.filmSource.firstname">Firstname</Translate>
                </th>
                <th>
                  <Translate contentKey="filmSourceApp.filmSource.position">Position</Translate>
                </th>
                <th>
                  <Translate contentKey="filmSourceApp.filmSource.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="filmSourceApp.filmSource.rate">Rate</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filmSourceList.map((filmSource, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${filmSource.id}`} color="link" size="sm">
                      {filmSource.id}
                    </Button>
                  </td>
                  <td>{filmSource.lastname}</td>
                  <td>{filmSource.firstname}</td>
                  <td>{filmSource.position}</td>
                  <td>{filmSource.location}</td>
                  <td>{filmSource.rate}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${filmSource.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${filmSource.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${filmSource.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="filmSourceApp.filmSource.home.notFound">No Film Sources found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FilmSource;
