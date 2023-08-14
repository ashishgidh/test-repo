import { ICluster, NewCluster } from './cluster.model';

export const sampleWithRequiredData: ICluster = {
  id: 4866,
};

export const sampleWithPartialData: ICluster = {
  id: 9971,
  name: 'needily Central',
};

export const sampleWithFullData: ICluster = {
  id: 21162,
  name: 'Krone',
};

export const sampleWithNewData: NewCluster = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
