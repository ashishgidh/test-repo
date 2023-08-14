import { IBranch, NewBranch } from './branch.model';

export const sampleWithRequiredData: IBranch = {
  id: 'c5a79285-99b7-4234-8afc-5ef0406055a0',
};

export const sampleWithPartialData: IBranch = {
  id: 'ff9d4fe1-d1ea-42eb-86a4-91fcfd5d5a37',
  branchId: 22080,
  name: 'Copper Bespoke',
};

export const sampleWithFullData: IBranch = {
  id: '64676cf2-a32e-4a3d-9f52-3c295b753e95',
  branchId: 9290,
  name: 'hacking Interactions',
};

export const sampleWithNewData: NewBranch = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
