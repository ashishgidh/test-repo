import { ILOB, NewLOB } from './lob.model';

export const sampleWithRequiredData: ILOB = {
  id: 28651,
};

export const sampleWithPartialData: ILOB = {
  id: 3877,
  wvLobName: 'dissect Automotive',
  maxLobName: 'Expanded',
};

export const sampleWithFullData: ILOB = {
  id: 5353,
  wvLobCode: 'Chair Specialist',
  wvLobName: 'hastily',
  maxLobCode: 'Account',
  maxLobName: 'Practical Handcrafted',
};

export const sampleWithNewData: NewLOB = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
