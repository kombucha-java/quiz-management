import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GameResult from './game-result';
import GameResultDetail from './game-result-detail';
import GameResultUpdate from './game-result-update';
import GameResultDeleteDialog from './game-result-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GameResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GameResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GameResultDetail} />
      <ErrorBoundaryRoute path={match.url} component={GameResult} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GameResultDeleteDialog} />
  </>
);

export default Routes;
