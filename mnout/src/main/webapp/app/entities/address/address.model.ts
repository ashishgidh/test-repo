import { IClient } from 'app/entities/client/client.model';

export interface IAddress {
  addressId?: number | null;
  street1?: string | null;
  street2?: string | null;
  city?: string | null;
  country?: string | null;
  postalCode?: string | null;
  county?: string | null;
  client?: Pick<IClient, 'id'> | null;
}
