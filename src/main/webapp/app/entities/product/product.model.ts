import dayjs from 'dayjs/esm';
import { ICategory } from 'app/entities/category/category.model';
import { IWishList } from 'app/entities/wish-list/wish-list.model';

export interface IProduct {
  id: number;
  title?: string | null;
  keywords?: string | null;
  description?: string | null;
  rating?: number | null;
  price?: number | null;
  image?: string | null;
  imageContentType?: string | null;
  dateAdded?: dayjs.Dayjs | null;
  dateModified?: dayjs.Dayjs | null;
  categories?: Pick<ICategory, 'id'>[] | null;
  wishLists?: Pick<IWishList, 'id'>[] | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
