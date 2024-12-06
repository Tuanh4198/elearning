import { ExamQuizzAssignStrategyEnum } from 'app/shared/model/enumerations/exam-quizz-assign-strategy-enum.model';
import { ExamPointStrategyEnum } from 'app/shared/model/enumerations/exam-point-strategy-enum.model';
import { ExamQuizzPoolStrategyEnum } from 'app/shared/model/enumerations/exam-quizz-pool-strategy-enum.model';
import { IExamQuizzPool } from 'app/shared/model/exam-quizz-pool.model';
import { ICategory } from 'app/shared/model/category.model';
import { IMetafield } from 'app/shared/model/metafields.model';
import { ExamStrategyEnum } from 'app/shared/model/enumerations/exam-strategy-enum.model';

export enum ExamMetafieldKey {
  working_time = 'working_time',
  max_number_of_test = 'max_number_of_test',
}

export interface IExam {
  id?: number;
  title?: string;
  categoryId?: number;
  requireJoin?: boolean;
  assignStrategy?: ExamQuizzAssignStrategyEnum;
  assignStrategyJson?: string;
  applyTime?: string;
  expireTime?: string;
  pointStrategy?: ExamPointStrategyEnum;
  minPointToPass?: number;
  description?: string;
  thumbUrl?: string;
  courseId?: number;
  numberOfQuestion?: number;
  poolStrategy?: ExamQuizzPoolStrategyEnum;
  examStrategy?: ExamStrategyEnum;
  quizzPools?: IExamQuizzPool[];
  category?: ICategory;
  metafields?: IMetafield<ExamMetafieldKey>[];
}

export const defaultValue: Readonly<IExam> = {
  requireJoin: false,
};
