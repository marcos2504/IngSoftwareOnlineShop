import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  title: 'hm sleepily',
  price: 17510.56,
};

export const sampleWithPartialData: IProduct = {
  id: 15347,
  title: 'silently honorable',
  description: 'fooey ha divine',
  price: 4492.48,
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  title: 'cafe',
  keywords: 'along amongst',
  description: 'hm ugh',
  rating: 2989,
  price: 1604.77,
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
  dateAdded: dayjs('2025-09-15'),
  dateModified: dayjs('2025-09-15'),
};

export const sampleWithNewData: NewProduct = {
  title: 'anti inject why',
  price: 26562.23,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
