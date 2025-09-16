import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 2568,
  postcode: 'admired yu',
  country: 'An',
};

export const sampleWithPartialData: IAddress = {
  id: 21652,
  address1: 'times',
  postcode: 'adumbrate ',
  country: 'Ka',
};

export const sampleWithFullData: IAddress = {
  id: 16440,
  address1: 'ouch molasses',
  address2: 'overdue obsess willfully',
  city: 'Robelmouth',
  postcode: 'whereas',
  country: 'To',
};

export const sampleWithNewData: NewAddress = {
  postcode: 'zowie uh-h',
  country: 'Bu',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
