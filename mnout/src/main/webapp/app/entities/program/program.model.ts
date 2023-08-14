export interface IProgram {
  id: string;
  programId?: number | null;
  clusterId?: number | null;
  countryId?: number | null;
  branchId?: number | null;
  maxLOBId?: number | null;
  wvLOBId?: number | null;
  programEffectiveDate?: string | null;
  programExpiryDate?: string | null;
}

export type NewProgram = Omit<IProgram, 'id'> & { id: null };
