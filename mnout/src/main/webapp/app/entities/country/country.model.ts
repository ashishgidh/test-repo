import { IClusterCountry } from 'app/entities/cluster-country/cluster-country.model';

export interface ICountry {
  id?: number | null;
  name?: string | null;
  code?: string | null;
  clusterCountry?: Pick<IClusterCountry, 'id'> | null;
}
