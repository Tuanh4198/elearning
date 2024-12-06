import { IMetafield } from 'app/shared/model/metafields.model';

export enum ExamQuizzPoolMetafieldKey {
  weight = 'weight',
}

export interface IExamQuizzPool {
  id?: number;
  rootId?: number;
  sourceId?: number;
  metafields?: IMetafield<ExamQuizzPoolMetafieldKey>[];
}

export const defaultValue: Readonly<IExamQuizzPool> = {};
