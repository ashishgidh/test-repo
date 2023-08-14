export interface IBranch {
  id: string;
  branchId?: number | null;
  name?: string | null;
}

export type NewBranch = Omit<IBranch, 'id'> & { id: null };
