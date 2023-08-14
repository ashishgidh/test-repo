import { ICountry } from './country.model';

export const sampleWithRequiredData: ICountry = {};

export const sampleWithPartialData: ICountry = {
  name: 'Cambridgeshire beastie drat',
  code: 'navigating orange',
};

export const sampleWithFullData: ICountry = {
  id: 26093,
  name: 'Producer Checking North',
  code: 'approach Woman',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
