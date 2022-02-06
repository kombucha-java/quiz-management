import { IFranchise } from 'app/shared/model/franchise.model';

export interface IGameType {
  id?: number;
  gameTypeName?: string;
  franchises?: IFranchise[] | null;
}

export const defaultValue: Readonly<IGameType> = {};
