import React, { useState } from 'react';
import { ActionIcon, Button, Checkbox, Flex, Input, Text } from '@mantine/core';
import { RichTextEditorCustom } from 'app/pages/admin/Quizz/components/RichTextEditorCustom';
import { Plus, Trash } from '@phosphor-icons/react';
import { IQuizzAnswer } from 'app/shared/model/quizz-answer.model';
import { v4 as uuidv4 } from 'uuid';

const defaultAnswers: IQuizzAnswer[] = [
  {
    id: uuidv4(),
    title: `option${String.fromCharCode(65)}`,
    content: '',
  },
  {
    id: uuidv4(),
    title: `option${String.fromCharCode(66)}`,
    content: '',
  },
];

const defaultCorrectAnswer = `option${String.fromCharCode(65)}`;

export const MultipleChoiceQuizz = () => {
  const [correctAnswer, setCorrectAnswer] = useState<string[]>([defaultCorrectAnswer]);
  const [answers, setAnswers] = useState<IQuizzAnswer[]>(defaultAnswers);

  const handleChangeAnswer = (value: string, updateId?: number | string) => {
    const newAnswers = [...(answers || [])].map(a => {
      if (a.id?.toString() === updateId?.toString()) {
        return {
          ...a,
          content: value,
        };
      }
      return a;
    });
    setAnswers(newAnswers);
  };

  const handleAddAnswer = () => {
    const newAnswers = [
      ...(answers || []),
      {
        id: uuidv4(),
        title: `option${String.fromCharCode(65 + (answers?.length || 0))}`,
        content: '',
      },
    ];
    setAnswers(newAnswers);
  };

  const handleRemoveAnswer = (deleteId?: string | number) => {
    if (!answers?.length || answers?.length <= 2) return;
    const newAnswers = [...(answers?.filter(a => a.id?.toString() !== deleteId?.toString()) ?? [])].map((a, idx) => ({
      ...a,
      title: `option${String.fromCharCode(65 + idx)}`,
    }));
    setAnswers(newAnswers);
  };

  const handleMetafieldChange = (value: string) => {
    // const metafields = [{ key: QuizzMetafieldKey.answer, value: `option${value.replace('option', '')}` }];
    setCorrectAnswer(old => (old.includes(value) ? old.filter(o => o !== value) : [...old, value]));
  };

  return (
    <Flex direction="column" w="100%" gap={20}>
      <RichTextEditorCustom />
      <Input.Label required>
        <Text display="inline-block" c="#1F2A37" fw={600}>
          Đáp án (Có thể chọn nhiều đáp án đúng)
        </Text>
      </Input.Label>
      {answers?.map((answer, idx) => (
        <Flex key={answer.id} gap={12} align="center" w="100%">
          <Checkbox
            checked={correctAnswer.includes(answer?.title ?? '')}
            onChange={() => handleMetafieldChange(answer.title ?? '')}
            label={String.fromCharCode(65 + idx)}
          />
          <Input
            flex={1}
            placeholder={`Đáp án ${String.fromCharCode(65 + idx)}`}
            value={answer.content}
            onChange={e => handleChangeAnswer(e.currentTarget.value, answer.id)}
          />
          <ActionIcon disabled={answers?.length <= 2} variant="transparent" onClick={() => handleRemoveAnswer(answer.id)}>
            <Trash size={16} />
          </ActionIcon>
        </Flex>
      ))}
      <Flex justify="flex-start">
        <Button className="quizz-add-btn" onClick={handleAddAnswer} variant="outline" leftSection={<Plus />}>
          Thêm đáp án
        </Button>
      </Flex>
    </Flex>
  );
};
