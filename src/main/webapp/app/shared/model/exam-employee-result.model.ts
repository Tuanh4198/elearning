import { ExamEmployeeStatusEnum } from 'app/shared/model/enumerations/exam-employee-status-enum.model';

export interface IExamEmployeeResult {
  id?: number;
  rootId?: number;
  startAt?: string;
  finishedAt?: string;
  numberOfCorrect?: number;
  numberOfQuestion?: number;
  numberOfTest?: number;
  status?: ExamEmployeeStatusEnum;
}
