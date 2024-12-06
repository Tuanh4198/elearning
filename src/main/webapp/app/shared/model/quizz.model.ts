import { QuizzTypeEnum } from 'app/shared/model/enumerations/quizz-type-enum.model';
import { IMetafield } from 'app/shared/model/metafields.model';
import { IQuizzAnswer } from 'app/shared/model/quizz-answer.model';

export enum QuizzMetafieldKey {
  answer = 'answer',
}

export interface IQuizz {
  id?: number;
  content?: string;
  categoryId?: number | null;
  category?: string | null;
  type?: QuizzTypeEnum | null;
  answers?: IQuizzAnswer[];
  metafields?: IMetafield<QuizzMetafieldKey>[];
}
