import { IProduct } from 'app/entities/product/product.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IWishList {
  id: number;
  title?: string | null;
  restricted?: boolean | null;
  products?: Pick<IProduct, 'id' | 'title'>[] | null;
  customer?: Pick<ICustomer, 'id'> | null;
}

export type NewWishList = Omit<IWishList, 'id'> & { id: null };
