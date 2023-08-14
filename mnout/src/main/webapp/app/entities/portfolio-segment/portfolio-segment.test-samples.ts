import { IPortfolioSegment, NewPortfolioSegment } from './portfolio-segment.model';

export const sampleWithRequiredData: IPortfolioSegment = {
  id: 'dbc6d5ee-344c-481a-968c-75db3d65a47d',
};

export const sampleWithPartialData: IPortfolioSegment = {
  id: '68a4e75a-66bc-40c0-bb08-4b4966764933',
  portfolioSigment: 29844,
};

export const sampleWithFullData: IPortfolioSegment = {
  id: 'c969a3f4-84b0-4f0d-89f8-d60b41dc7d2e',
  portfolioSigment: 13888,
  name: 'generate',
  segment: 'Savings white josh',
};

export const sampleWithNewData: NewPortfolioSegment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
