import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: '487cb2d4-1ac9-4cf6-ad44-b5e464dff695',
};

export const sampleWithPartialData: IClient = {
  id: 'bf0b9146-edec-4709-bcd4-67c90c5edf11',
  name: 'emulation whenever',
  email: 'Hortense.Mertz22@yahoo.com',
  phone: '1-249-266-8611',
};

export const sampleWithFullData: IClient = {
  id: '358c7711-c701-4c70-9a4e-937fadf4341d',
  clientId: 14971,
  name: 'Focused salmon',
  email: 'Carlos_King@hotmail.com',
  phone: '1-322-771-4598 x88293',
  company: 'panel didactic',
};

export const sampleWithNewData: NewClient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
