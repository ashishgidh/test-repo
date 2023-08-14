import { IProgram, NewProgram } from './program.model';

export const sampleWithRequiredData: IProgram = {
  id: '4b9357e0-8524-433f-bcb6-9c4c9c4cf7df',
};

export const sampleWithPartialData: IProgram = {
  id: 'd00c8d45-f104-457e-b16f-d1a0a89d2ad5',
  branchId: 11674,
  wvLOBId: 4822,
};

export const sampleWithFullData: IProgram = {
  id: 'f268b484-5275-4a7a-aaf8-4ef4b157a277',
  programId: 15229,
  clusterId: 17754,
  countryId: 6397,
  branchId: 5914,
  maxLOBId: 2078,
  wvLOBId: 30628,
  programEffectiveDate: 'ack',
  programExpiryDate: 'Senior shaft Customer',
};

export const sampleWithNewData: NewProgram = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
