/* eslint-disable @typescript-eslint/no-non-null-assertion */
import React, { useCallback, useRef, useState } from 'react';
import { IQuizz } from 'app/shared/model/quizz.model';
import { QuizzTypeEnum } from 'app/shared/model/enumerations/quizz-type-enum.model';
import { Checkbox, Radio, Textarea } from '@mantine/core';
import { debounce } from 'lodash';

export interface IAnswerBody {
  quizz_id: number;
  answer: string;
}
const splitCharacters = '||';
export const useEnterAnswer = () => {
  const answersRef = useRef<{ [key: number]: IAnswerBody }>({});
  const [answers, setAnswer] = useState<{ [key: number]: IAnswerBody }>({});

  const onEssayQuizzChange = debounce(({ value, quizzId }: { value: string; quizzId: number }) => {
    setAnswer(oldValue => ({
      ...oldValue,
      [quizzId]: {
        quizz_id: quizzId,
        answer: value,
      },
    }));
    answersRef.current = {
      ...answersRef.current,
      [quizzId]: {
        quizz_id: quizzId,
        answer: value,
      },
    };
  }, 500);

  const onBlankQuizzChange = useCallback(
    ({ valueIds, valueTitles, quizzId }: { valueIds: string[]; valueTitles: string[]; quizzId: number }) => {
      setAnswer(oldValue => ({
        ...oldValue,
        [quizzId]: {
          quizz_id: quizzId,
          answer: valueIds.length > 1 ? valueIds.join(splitCharacters) : valueIds.length > 0 ? valueIds[0] : '',
        },
      }));
      answersRef.current = {
        ...answersRef.current,
        [quizzId]: {
          quizz_id: quizzId,
          answer: valueTitles.length > 1 ? valueTitles.join(splitCharacters) : valueTitles.length > 0 ? valueTitles[0] : '',
        },
      };
    },
    []
  );

  const onMultipleChoiceQuizzChange = useCallback(
    ({ valueId, valueTitle, quizzId }: { valueId: string; valueTitle: string; quizzId: number }) => {
      setAnswer(oldValue => ({
        ...oldValue,
        [quizzId]: {
          quizz_id: quizzId,
          answer: valueId,
        },
      }));
      answersRef.current = {
        ...answersRef.current,
        [quizzId]: {
          quizz_id: quizzId,
          answer: valueTitle,
        },
      };
    },
    []
  );

  const renderFormAnswer = useCallback(
    (quizz: IQuizz) => {
      if (!quizz.id) return null;
      const blankValue = answers?.[quizz.id]?.answer?.split(splitCharacters);
      const multipleChoiceValue = answers?.[quizz.id]?.answer;
      switch (quizz.type) {
        case QuizzTypeEnum.ESSAY:
          return (
            <Textarea
              onChange={e =>
                onEssayQuizzChange({
                  value: e.target.value,
                  quizzId: quizz.id!,
                })
              }
              placeholder="Trả lời"
              rows={4}
            />
          );
        case QuizzTypeEnum.BLANK:
          return (
            <Checkbox.Group
              value={blankValue}
              onChange={(value: string[]) =>
                onBlankQuizzChange({
                  valueIds: value,
                  valueTitles: quizz?.answers?.filter(a => value.includes(a.id!.toString()))?.map(a => a.title!) || [],
                  quizzId: quizz.id!,
                })
              }
            >
              {quizz?.answers?.map(a => (
                <Checkbox key={a.id?.toString()} value={a.id?.toString()} label={a?.content} />
              ))}
            </Checkbox.Group>
          );
        case QuizzTypeEnum.MULTIPLE_CHOICE:
          return (
            <Radio.Group
              value={multipleChoiceValue}
              onChange={value =>
                onMultipleChoiceQuizzChange({
                  valueId: value,
                  valueTitle: quizz?.answers?.filter(a => a?.id?.toString() === value)?.[0]?.title || '',
                  quizzId: quizz.id!,
                })
              }
            >
              {quizz?.answers?.map(a => (
                <Radio key={a.id?.toString()} value={a.id?.toString()} label={a?.content} />
              ))}
            </Radio.Group>
          );
        default:
          return `Lỗi dạng câu hỏi ${quizz.type}`;
      }
    },
    [answers]
  );

  return {
    answers,
    setAnswer,
    answersRef: answersRef.current,
    renderFormAnswer,
  };
};
