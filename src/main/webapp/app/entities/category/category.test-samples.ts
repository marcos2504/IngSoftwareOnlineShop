import dayjs from 'dayjs/esm';

import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 8109,
  description: 'midst croon cautiously',
};

export const sampleWithPartialData: ICategory = {
  id: 21368,
  description: 'uh-huh blindly and',
  sortOrder: 7827,
  dateAdded: dayjs('2025-09-15'),
  dateModified: dayjs('2025-09-15'),
};

export const sampleWithFullData: ICategory = {
  id: 28780,
  description: 'ouch',
  sortOrder: 8787,
  dateAdded: dayjs('2025-09-15'),
  dateModified: dayjs('2025-09-15'),
  status: 'RESTRICTED',
};

export const sampleWithNewData: NewCategory = {
  description: 'where pfft regularly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
