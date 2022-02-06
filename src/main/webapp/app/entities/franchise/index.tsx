import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Franchise from './franchise';
import FranchiseDetail from './franchise-detail';
import FranchiseUpdate from './franchise-update';
import FranchiseDeleteDialog from './franchise-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FranchiseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FranchiseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FranchiseDetail} />
      <ErrorBoundaryRoute path={match.url} component={Franchise} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FranchiseDeleteDialog} />
  </>
);

export default Routes;
