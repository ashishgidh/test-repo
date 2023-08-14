import { IAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {};

export const sampleWithPartialData: IAddress = {
  street1: 'over',
};

export const sampleWithFullData: IAddress = {
  addressId: 25097,
  street1: 'stair',
  street2: 'Male',
  city: 'Sacramento',
  country: 'Iraq',
  postalCode: 'Tala West Northwest',
  county: 'Strategist',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
