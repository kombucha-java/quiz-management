import { IGame } from 'app/shared/model/game.model';

export interface IGameResult {
  id?: number;
  place?: number;
  points?: number;
  link?: string | null;
  tableContentType?: string | null;
  table?: string | null;
  game?: IGame | null;
}

export const defaultValue: Readonly<IGameResult> = {};
