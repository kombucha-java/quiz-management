import { IUser } from 'app/shared/model/user.model';

export interface IPlayer {
  id?: number;
  nickName?: string;
  internalUser?: IUser | null;
}

export const defaultValue: Readonly<IPlayer> = {};
