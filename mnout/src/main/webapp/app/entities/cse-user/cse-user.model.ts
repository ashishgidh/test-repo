export interface ICSEUser {
  id: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
}

export type NewCSEUser = Omit<ICSEUser, 'id'> & { id: null };
