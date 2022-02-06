import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Franchise from './franchise';
import GameType from './game-type';
import Team from './team';
import Player from './player';
import GameResult from './game-result';
import Restaurant from './restaurant';
import Game from './game';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}franchise`} component={Franchise} />
      <ErrorBoundaryRoute path={`${match.url}game-type`} component={GameType} />
      <ErrorBoundaryRoute path={`${match.url}team`} component={Team} />
      <ErrorBoundaryRoute path={`${match.url}player`} component={Player} />
      <ErrorBoundaryRoute path={`${match.url}game-result`} component={GameResult} />
      <ErrorBoundaryRoute path={`${match.url}restaurant`} component={Restaurant} />
      <ErrorBoundaryRoute path={`${match.url}game`} component={Game} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
