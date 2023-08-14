export interface IPortfolioSegment {
  id: string;
  portfolioSigment?: number | null;
  name?: string | null;
  segment?: string | null;
}

export type NewPortfolioSegment = Omit<IPortfolioSegment, 'id'> & { id: null };
