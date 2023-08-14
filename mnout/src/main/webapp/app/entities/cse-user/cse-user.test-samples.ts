import { ICSEUser, NewCSEUser } from './cse-user.model';

export const sampleWithRequiredData: ICSEUser = {
  id: 10018,
};

export const sampleWithPartialData: ICSEUser = {
  id: 4329,
};

export const sampleWithFullData: ICSEUser = {
  id: 7147,
  name: 'anti District',
  email: 'Cortney58@yahoo.com',
  phone: '716.741.2933 x3766',
};

export const sampleWithNewData: NewCSEUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
