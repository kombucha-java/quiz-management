import { IGame } from 'app/shared/model/game.model';
import { IGameType } from 'app/shared/model/game-type.model';

export interface IFranchise {
  id?: number;
  franchiseName?: string;
  games?: IGame[] | null;
  gameType?: IGameType | null;
}

export const defaultValue: Readonly<IFranchise> = {};
