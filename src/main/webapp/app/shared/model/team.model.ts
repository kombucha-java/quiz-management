import { IGame } from 'app/shared/model/game.model';

export interface ITeam {
  id?: number;
  teamName?: string;
  games?: IGame[] | null;
}

export const defaultValue: Readonly<ITeam> = {};
