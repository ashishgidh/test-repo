import { IClusterCountry, NewClusterCountry } from './cluster-country.model';

export const sampleWithRequiredData: IClusterCountry = {
  id: 30497,
};

export const sampleWithPartialData: IClusterCountry = {
  id: 20526,
  name: 'plum',
};

export const sampleWithFullData: IClusterCountry = {
  id: 26593,
  name: 'female South Cotton',
};

export const sampleWithNewData: NewClusterCountry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
