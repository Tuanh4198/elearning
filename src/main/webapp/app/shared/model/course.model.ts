import { ICategory } from 'app/shared/model/category.model';
import { IDocument } from 'app/shared/model/document.model';
import { AssignStrategyEnum } from 'app/shared/model/enumerations/assign-strategy-enum.model';
import { IMetafield } from 'app/shared/model/metafields.model';

export enum CourseMetafieldKey {
  minimum_study_time = 'minimum_study_time',
}

export interface ICourse {
  id?: number;
  title?: string;
  categoryId?: number;
  requireJoin?: boolean;
  requireAttend?: boolean;
  meetingUrl?: string;
  examId?: number;
  applyTime?: string;
  expireTime?: string;
  assignStrategy?: AssignStrategyEnum;
  assignStrategyJson?: string;
  description?: string;
  thumbUrl?: string;
  category?: ICategory;
  documents?: IDocument[];
  metafields?: IMetafield<CourseMetafieldKey>[];
}
