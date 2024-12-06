import React, { Fragment, useCallback, useState } from 'react';
import './styles.scss';
import { ActionIcon, Badge, Button, Checkbox, Container, Flex, Text, Group, Input, Radio, Space, Textarea } from '@mantine/core';
import { IQuizz, QuizzMetafieldKey } from 'app/shared/model/quizz.model';
import { Plus, Trash, X } from '@phosphor-icons/react';
import { QuizzTypeEnum } from 'app/shared/model/enumerations/quizz-type-enum.model';
import { v4 as uuidv4 } from 'uuid';
import { useForm } from '@mantine/form';
import { useMutateQuizz } from 'app/pages/admin/Quizz/AddQuizz/hooks/useMutateQuizz';
import { notiError } from 'app/shared/notifications';
import { IQuizzAnswer } from 'app/shared/model/quizz-answer.model';
import { SelectQuizzCategory } from 'app/pages/admin/Quizz/components/SelectQuizzCategory';
import { EssayQuizz } from 'app/pages/admin/Quizz/components/EssayQuizz';
import { MultipleChoiceQuizz } from 'app/pages/admin/Quizz/components/MultipleChoiceQuizz';
import { BlankQuizz } from 'app/pages/admin/Quizz/components/BlankQuizz';

type TempQuizz = IQuizz & {
  tempId: string;
  number: number;
};

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

export const AddQuizz = () => {
  const [selectedCategoryId, setSelectedCategoryId] = useState<string>();
  const [activeQuizz, setActiveQuizz] = useState<number>(0);

  const form = useForm({
    initialValues: {
      quizzes: [
        {
          tempId: uuidv4(),
          number: 0,
          type: QuizzTypeEnum.MULTIPLE_CHOICE,
          content: '',
          categoryId: selectedCategoryId ? Number(selectedCategoryId) : null,
          answers: defaultAnswers,
          metafields: [{ key: QuizzMetafieldKey.answer, value: `option${String.fromCharCode(65)}` }],
        },
      ] as TempQuizz[],
    },
  });

  const addTempQuizz = useCallback(() => {
    form.setFieldValue('quizzes', [
      ...form.values.quizzes,
      {
        tempId: uuidv4(),
        number: form.values.quizzes.length,
        type: QuizzTypeEnum.MULTIPLE_CHOICE,
        content: '',
        categoryId: selectedCategoryId ? Number(selectedCategoryId) : null,
        answers: defaultAnswers.map(a => ({ ...a, tempId: uuidv4() })),
        metafields: [{ key: QuizzMetafieldKey.answer, value: `option${String.fromCharCode(65)}` }],
      },
    ]);
  }, [form, selectedCategoryId]);

  const removeQuizz = useCallback(
    (quizzId: string) => {
      const newQuizzes = form.values.quizzes.filter((_, index) => _.tempId !== quizzId);
      newQuizzes.forEach((quizz, index) => (quizz.number = index));
      form.setFieldValue('quizzes', newQuizzes);
      setActiveQuizz(0);
    },
    [form]
  );

  const handleAnswerChange = (quizzIndex: number, value: string, tempId?: number | string) => {
    const updatedQuizzes = [...form.values.quizzes];
    const quiz = updatedQuizzes[quizzIndex];
    quiz.answers = [...(quiz.answers || [])].map(a => {
      if (a.id === tempId) {
        return {
          ...a,
          content: value,
        };
      }
      return a;
    });
    form.setFieldValue('quizzes', updatedQuizzes);
  };

  const handleAddAnswer = (quizzIndex: number) => {
    const updatedQuizzes = [...form.values.quizzes];
    const quiz = updatedQuizzes[quizzIndex];
    quiz.answers = [
      ...(quiz.answers || []),
      {
        id: uuidv4(),
        title: `option${String.fromCharCode(65 + (quiz.answers?.length || 0))}`,
        content: '',
      },
    ];
    form.setFieldValue('quizzes', updatedQuizzes);
  };

  const handleRemoveAnswer = (quizzIndex: number, tempId?: number | string) => {
    if (!tempId) return;
    const updatedQuizzes = [...form.values.quizzes];
    const quiz = updatedQuizzes[quizzIndex];
    if (!quiz.answers?.length || quiz.answers?.length <= 2) return;
    quiz.answers = [...(quiz.answers?.filter(a => a.id !== tempId) ?? [])].map((a, idx) => ({
      ...a,
      title: `option${String.fromCharCode(65 + idx)}`,
    }));
    form.setFieldValue('quizzes', updatedQuizzes);
  };

  const handleMetafieldChange = (quizzIndex: number, value: string) => {
    const updatedQuizzes = [...form.values.quizzes];
    const quiz = updatedQuizzes[quizzIndex];
    quiz.metafields = [{ key: QuizzMetafieldKey.answer, value: `option${value.replace('option', '')}` }];
    form.setFieldValue('quizzes', updatedQuizzes);
  };

  const { onAdd } = useMutateQuizz({
    onSuccess() {
      close();
    },
    onFailed() {},
  });

  const handleSubmit = (values: { quizzes: TempQuizz[] }) => {
    if (!selectedCategoryId) {
      notiError({ message: 'Chưa chọn danh mục' });
      return;
    }
    const finalValues = values.quizzes.map(quizz => {
      quizz.categoryId = Number(selectedCategoryId);
      return quizz;
    });
    for (const finalValue of finalValues) {
      const { answers, categoryId, content, metafields, type } = finalValue;
      const _answers = answers?.map(({ id, ...rest }) => rest);
      onAdd({
        categoryId,
        content,
        metafields: type === QuizzTypeEnum.MULTIPLE_CHOICE ? metafields : [],
        type,
        answers: type === QuizzTypeEnum.MULTIPLE_CHOICE ? ((_answers ?? []) as IQuizzAnswer[]) : [],
      });
    }
    form.reset();
    setSelectedCategoryId(undefined);
    setActiveQuizz(0);
  };

  const correctAnswer = form.values.quizzes[activeQuizz]?.metafields?.find(mf => mf.key === 'answer')?.value;
  const answers = form.values.quizzes[activeQuizz]?.answers;

  return (
    <Fragment>
      <Flex align="center" justify="space-between">
        <Text fz={24} fw={700} c="#1F2A37">
          Tạo câu hỏi
        </Text>
      </Flex>
      <Space h={'md'} />
      <Flex direction={'row'} w={'100%'} h={'100%'}>
        <Container flex={1} style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
          <b>Danh sách danh mục</b>
          <Space h={16} />
          <SelectQuizzCategory allowClear={false} onSelectedItem={id => setSelectedCategoryId(id)} />
        </Container>
        <Space w={16} />
        <Container flex={3} style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
          <form onSubmit={form.onSubmit(handleSubmit)}>
            <Flex w={'100%'} wrap={'wrap'} align="center" gap={8}>
              {form.values.quizzes.map((quizz, index) => (
                <Badge
                  h={24}
                  m={0}
                  key={quizz.tempId}
                  className="quizz-badge"
                  bg={quizz.number === activeQuizz ? '#D0EBFF' : '#E5E7EB'}
                  c={quizz.number === activeQuizz ? '#1E1E73' : '#6B7280'}
                  rightSection={
                    <div
                      style={{ cursor: 'pointer' }}
                      onClick={e => {
                        e.preventDefault();
                        removeQuizz(quizz.tempId);
                      }}
                    >
                      <X />
                    </div>
                  }
                  onClick={() => setActiveQuizz(quizz.number)}
                >
                  {`Câu ${index + 1}`}
                </Badge>
              ))}
              <Button className="quizz-add-btn" onClick={addTempQuizz} variant="outline" leftSection={<Plus />}>
                Thêm câu hỏi
              </Button>
            </Flex>
            <Space h={16} />
            {form.values.quizzes.length > 0 && (
              <Flex direction={'column'} gap={15} align="flex-start">
                <Radio.Group withAsterisk {...form.getInputProps(`quizzes.${activeQuizz}.type`)}>
                  <Group>
                    <Radio value={QuizzTypeEnum.MULTIPLE_CHOICE} label="Câu hỏi trắc nghiệm" />
                    <Radio value={QuizzTypeEnum.ESSAY} label="Câu hỏi tự luận" />
                    <Radio disabled value={QuizzTypeEnum.BLANK} label="Câu hỏi điền vào chỗ trống" />
                  </Group>
                </Radio.Group>
                <Textarea
                  minRows={16}
                  w="100%"
                  placeholder={'Nhập nội dung câu hỏi'}
                  {...form.getInputProps(`quizzes.${activeQuizz}.content`)}
                />
                {form.values.quizzes[activeQuizz]?.type === QuizzTypeEnum.MULTIPLE_CHOICE && (
                  <>
                    {correctAnswer && (
                      <Badge h={24} m={0} bg={'#D0EBFF'} c={'#1E1E73'}>
                        Câu {correctAnswer.replace('option', '')} đúng
                      </Badge>
                    )}
                    {answers?.map((answer, idx) => (
                      <Flex key={answer.id} gap={12} align="center" w="100%">
                        <Checkbox
                          checked={correctAnswer === answer.title}
                          onChange={() => handleMetafieldChange(activeQuizz, answer.title ?? '')}
                          label={String.fromCharCode(65 + idx)}
                        />
                        <Input
                          flex={1}
                          placeholder={`Đáp án ${String.fromCharCode(65 + idx)}`}
                          value={answer.content}
                          onChange={e => handleAnswerChange(activeQuizz, e.currentTarget.value, answer.id)}
                        />
                        <ActionIcon
                          disabled={answers?.length <= 2}
                          variant="transparent"
                          onClick={() => handleRemoveAnswer(activeQuizz, answer.id)}
                        >
                          <Trash size={16} />
                        </ActionIcon>
                      </Flex>
                    ))}
                    <Button className="quizz-add-btn" onClick={() => handleAddAnswer(activeQuizz)} variant="outline" leftSection={<Plus />}>
                      Thêm đáp án
                    </Button>
                  </>
                )}
                <Button type="submit">Tạo câu hỏi</Button>
              </Flex>
            )}
          </form>

          {/* <EssayQuizz />
          <MultipleChoiceQuizz /> */}
          {/* <BlankQuizz /> */}
        </Container>
      </Flex>
    </Fragment>
  );
};
