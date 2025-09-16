import { IWishList, NewWishList } from './wish-list.model';

export const sampleWithRequiredData: IWishList = {
  id: 711,
  title: 'exotic over',
};

export const sampleWithPartialData: IWishList = {
  id: 1757,
  title: 'sun different',
  restricted: true,
};

export const sampleWithFullData: IWishList = {
  id: 9410,
  title: 'because accompanist',
  restricted: true,
};

export const sampleWithNewData: NewWishList = {
  title: 'than gee',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
