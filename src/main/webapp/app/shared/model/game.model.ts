import dayjs from 'dayjs';
import { IGameResult } from 'app/shared/model/game-result.model';
import { IFranchise } from 'app/shared/model/franchise.model';
import { ITeam } from 'app/shared/model/team.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IGame {
  id?: number;
  dateTime?: string;
  theme?: string;
  gameResult?: IGameResult | null;
  franchise?: IFranchise | null;
  team?: ITeam | null;
  restaurant?: IRestaurant | null;
}

export const defaultValue: Readonly<IGame> = {};
