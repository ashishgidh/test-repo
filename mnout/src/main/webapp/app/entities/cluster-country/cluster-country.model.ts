export interface IClusterCountry {
  id: number;
  name?: string | null;
}

export type NewClusterCountry = Omit<IClusterCountry, 'id'> & { id: null };
