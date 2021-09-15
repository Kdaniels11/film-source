import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FilmSource from './film-source';
import FilmSourceDetail from './film-source-detail';
import FilmSourceUpdate from './film-source-update';
import FilmSourceDeleteDialog from './film-source-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FilmSourceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FilmSourceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FilmSourceDetail} />
      <ErrorBoundaryRoute path={match.url} component={FilmSource} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FilmSourceDeleteDialog} />
  </>
);

export default Routes;
