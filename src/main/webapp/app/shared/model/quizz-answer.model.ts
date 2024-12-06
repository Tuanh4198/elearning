export interface IQuizzAnswer {
  id?: number | string;
  rootId?: number;
  title?: string;
  content?: string;
}

export const defaultValue: Readonly<IQuizzAnswer> = {};
