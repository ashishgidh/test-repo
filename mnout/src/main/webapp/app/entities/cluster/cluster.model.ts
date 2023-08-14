export interface ICluster {
  id: number;
  name?: string | null;
}

export type NewCluster = Omit<ICluster, 'id'> & { id: null };
