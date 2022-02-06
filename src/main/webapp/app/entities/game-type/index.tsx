import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GameType from './game-type';
import GameTypeDetail from './game-type-detail';
import GameTypeUpdate from './game-type-update';
import GameTypeDeleteDialog from './game-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GameTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GameTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GameTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={GameType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GameTypeDeleteDialog} />
  </>
);

export default Routes;
