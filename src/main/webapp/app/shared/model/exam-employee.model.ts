import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';
import { IExam } from 'app/shared/model/exam.model';
import { IQuizz } from 'app/shared/model/quizz.model';

export interface IExamEmployee {
  id?: number;
  code?: string;
  name?: string;
  rootId?: number;
  status?: ExamEmployeeStatusEnum;
  exam?: IExam;
  quizzs?: IQuizz[];
}

export const defaultValue: Readonly<IExamEmployee> = {};
