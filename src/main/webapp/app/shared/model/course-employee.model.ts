import { ICourse } from 'app/shared/model/course.model';
import { CourseEmployeeStatusEnum } from 'app/shared/model/enumerations/course-employee-status-enum.model';
import { IMetafield } from 'app/shared/model/metafields.model';

export enum CourseEmployeeMetafieldKey {
  // learn_at = 'learn_at',
  attend_at = 'attend_at',
  learn_info = 'learn_info',
}

export interface ICourseEmployee {
  id?: number;
  code?: string;
  name?: string;
  rootId?: number;
  status?: CourseEmployeeStatusEnum;
  course?: ICourse;
  metafields?: IMetafield<CourseEmployeeMetafieldKey>[];
}

export const defaultValue: Readonly<ICourseEmployee> = {};
