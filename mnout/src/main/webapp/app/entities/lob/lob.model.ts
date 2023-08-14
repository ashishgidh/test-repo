export interface ILOB {
  id: number;
  wvLobCode?: string | null;
  wvLobName?: string | null;
  maxLobCode?: string | null;
  maxLobName?: string | null;
}

export type NewLOB = Omit<ILOB, 'id'> & { id: null };
