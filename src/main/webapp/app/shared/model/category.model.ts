export interface ICategory {
  id?: number;
  title?: string;
  type?: CategoryTypeEnum;
  description?: string;
  createdBy?: string;
  createdAt?: string;
}

export enum CategoryTypeEnum {
  COURSE = 'COURSE',
  EXAM = 'EXAM',
}

export const defaultValue: Readonly<ICategory> = {};
