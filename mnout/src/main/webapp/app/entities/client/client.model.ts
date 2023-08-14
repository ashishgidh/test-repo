import { IProgram } from 'app/entities/program/program.model';

export interface IClient {
  id: string;
  clientId?: number | null;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  company?: string | null;
  program?: Pick<IProgram, 'id'> | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
