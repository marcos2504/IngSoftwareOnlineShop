import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
};

export const sampleWithPartialData: ICustomer = {
  id: 8561,
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  firstName: 'Lyla',
  lastName: 'Langosh',
  email: 'Conor_Powlowski15@hotmail.com',
  telephone: '787-506-7917 x264',
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
