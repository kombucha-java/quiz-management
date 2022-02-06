import { IGame } from 'app/shared/model/game.model';

export interface IRestaurant {
  id?: number;
  restaurantName?: string;
  address?: string;
  games?: IGame[] | null;
}

export const defaultValue: Readonly<IRestaurant> = {};
