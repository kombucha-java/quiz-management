import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import franchise from 'app/entities/franchise/franchise.reducer';
// prettier-ignore
import gameType from 'app/entities/game-type/game-type.reducer';
// prettier-ignore
import team from 'app/entities/team/team.reducer';
// prettier-ignore
import player from 'app/entities/player/player.reducer';
// prettier-ignore
import gameResult from 'app/entities/game-result/game-result.reducer';
// prettier-ignore
import restaurant from 'app/entities/restaurant/restaurant.reducer';
// prettier-ignore
import game from 'app/entities/game/game.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  franchise,
  gameType,
  team,
  player,
  gameResult,
  restaurant,
  game,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
