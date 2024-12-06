export interface IQuizzCategory {
  id?: number;
  title?: string;
  description?: string | null;
  createdBy?: string;
  createdAt?: string;
}

export const defaultValue: Readonly<IQuizzCategory> = {};
