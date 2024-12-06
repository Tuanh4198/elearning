import React, { Fragment, useState } from 'react';
import './styles.scss';
import { ActionIcon, Badge, Button, Checkbox, Container, Flex, Text, Group, Input, Radio, Space, Textarea, Loader } from '@mantine/core';
import { IQuizz, QuizzMetafieldKey } from 'app/shared/model/quizz.model';
import { Plus, Trash, X } from '@phosphor-icons/react';
import { QuizzTypeEnum } from 'app/shared/model/enumerations/quizz-type-enum.model';
import { v4 as uuidv4 } from 'uuid';
import { useForm } from '@mantine/form';
import { useMutateQuizz } from 'app/pages/admin/Quizz/UpdateQuizz/hooks/useMutateQuizz';
import { notiError } from 'app/shared/notifications';
import { IQuizzAnswer } from 'app/shared/model/quizz-answer.model';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import { SelectQuizzCategory } from 'app/pages/admin/Quizz/components/SelectQuizzCategory';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';

type TempQuizzAnswer = IQuizzAnswer & {
  tempId: string;
};

type TempQuizz = IQuizz & {
  answers?: TempQuizzAnswer[];
};

export const UpdateQuizz = () => {
  const { id } = useParams<{ id: string }>();

  const fetchQuizzDetail = async () => {
    if (!id) return null;
    return await axios.get<IQuizz>(`/api/quizzes/${id}`);
  };

  const { isLoading, isFetching, data } = useQuery({
    queryKey: ['useFetchQuizzDetail', id],
    queryFn: fetchQuizzDetail,
    gcTime: 0,
  });

  const fetchCategoryDetail = async () => {
    if (!data?.data.categoryId) return null;
    return await axios.get<IQuizzCategory>(`/api/quizz-categories/${data?.data.categoryId}`);
  };

  const {
    isLoading: isLoadingCategory,
    isFetching: isFetchingCategory,
    data: dataCategory,
  } = useQuery({
    queryKey: ['useFetchQuizzCategoryDetail', data?.data.categoryId],
    queryFn: fetchCategoryDetail,
    gcTime: 0,
  });

  if (isLoading || isFetching || isLoadingCategory || isFetchingCategory) return <Loader size={16} />;

  return <UpdateQuizzForm quizz={data?.data} quizzCategory={dataCategory?.data} />;
};

const UpdateQuizzForm = ({ quizz, quizzCategory }: { quizz?: IQuizz; quizzCategory?: IQuizzCategory }) => {
  const [selectedCategoryId, setSelectedCategoryId] = useState<string | undefined>(quizzCategory?.id?.toString());

  const form = useForm<TempQuizz>({
    initialValues: {
      ...quizz,
      answers: quizz?.answers?.map(a => ({
        ...a,
        tempId: uuidv4(),
      })),
    },
  });

  const handleAnswerChange = (value: string, tempId?: number | string) => {
    let updatedQuizzAnswers = [...(form.values.answers || [])];
    updatedQuizzAnswers = updatedQuizzAnswers?.map(a => {
      if (a.tempId === tempId) {
        return {
          ...a,
          content: value,
        };
      }
      return a;
    });
    form.setFieldValue('answers', updatedQuizzAnswers);
  };

  const handleAddAnswer = () => {
    const updatedQuizzAnswers = [
      ...(form.values.answers || []),
      {
        tempId: uuidv4(),
        title: `option${String.fromCharCode(65 + (form.values.answers?.length || 0))}`,
        content: '',
      },
    ];
    form.setFieldValue('answers', updatedQuizzAnswers);
  };

  const handleRemoveAnswer = (tempId?: number | string) => {
    let updatedQuizzAnswers = [...(form.values.answers || [])];
    if (updatedQuizzAnswers?.length <= 2) return;
    updatedQuizzAnswers = [...(updatedQuizzAnswers?.filter(a => a.tempId !== tempId) ?? [])].map((a, idx) => ({
      ...a,
      title: `option${String.fromCharCode(65 + idx)}`,
    }));
    form.setFieldValue('answers', updatedQuizzAnswers);
  };

  const handleMetafieldChange = (value: string) => {
    let updatedQuizzMetafields = { ...(form.values.metafields ?? []) };
    updatedQuizzMetafields = updatedQuizzMetafields?.map(m => {
      if (m.key === QuizzMetafieldKey.answer) {
        return {
          ...m,
          value: `option${value.replace('option', '')}`,
        };
      }
      return m;
    });
    form.setFieldValue('metafields', updatedQuizzMetafields);
  };

  const { onUpdate } = useMutateQuizz({
    onSuccess() {
      close();
    },
    onFailed() {},
  });

  const handleSubmit = (values: TempQuizz) => {
    if (!selectedCategoryId) {
      notiError({ message: 'Chưa chọn danh mục' });
      return;
    }
    const { answers, categoryId, content, metafields, type } = values;
    const _answers = (answers ?? []).map(({ tempId, ...rest }: TempQuizzAnswer) => rest);
    onUpdate({
      ...quizz,
      categoryId,
      content,
      metafields,
      type,
      answers: type === QuizzTypeEnum.MULTIPLE_CHOICE ? ((_answers ?? []) as IQuizzAnswer[]) : [],
    });
  };

  const correctAnswer = form.values?.metafields?.find(mf => mf.key === 'answer')?.value;
  const answers = form.values?.answers;

  return (
    <Fragment>
      <Flex align="center" justify="space-between">
        <Text fz={24} fw={700} c="#1F2A37">
          Chi tiết câu hỏi
        </Text>
      </Flex>
      <Space h={'md'} />
      <Flex direction={'row'} w={'100%'} h={'100%'}>
        <Container flex={1} style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
          <b>Danh sách danh mục</b>
          <Space h={16} />
          <SelectQuizzCategory allowClear={false} defaultSelected={quizzCategory} onSelectedItem={id => setSelectedCategoryId(id)} />
        </Container>
        <Space w={16} />
        <Container flex={3} style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
          <form onSubmit={form.onSubmit(handleSubmit)}>
            <Flex direction={'column'} gap={15} align="flex-start">
              <Radio.Group withAsterisk {...form.getInputProps(`type`)}>
                <Group>
                  <Radio disabled value={QuizzTypeEnum.MULTIPLE_CHOICE} label="Câu hỏi trắc nghiệm" />
                  <Radio disabled value={QuizzTypeEnum.ESSAY} label="Câu hỏi tự luận" />
                  <Radio disabled value={QuizzTypeEnum.BLANK} label="Câu hỏi điền vào chỗ trống" />
                </Group>
              </Radio.Group>
              <Textarea minRows={16} w="100%" placeholder={'Nhập nội dung câu hỏi'} {...form.getInputProps(`content`)} />
              {form.values?.type === QuizzTypeEnum.MULTIPLE_CHOICE && (
                <>
                  {correctAnswer && (
                    <Badge h={24} m={0} bg={'#D0EBFF'} c={'#1E1E73'}>
                      Câu {correctAnswer.replace('option', '')} đúng
                    </Badge>
                  )}
                  {form.values?.answers?.map((answer: TempQuizzAnswer, idx) => (
                    <Flex key={answer.id} gap={12} align="center" w="100%">
                      <Checkbox
                        checked={correctAnswer === answer.title}
                        onChange={() => handleMetafieldChange(answer.title ?? '')}
                        label={String.fromCharCode(65 + idx)}
                      />
                      <Input
                        flex={1}
                        placeholder={`Đáp án ${String.fromCharCode(65 + idx)}`}
                        value={answer.content}
                        onChange={e => handleAnswerChange(e.currentTarget.value, answer.tempId)}
                      />
                      {/* <ActionIcon
                        disabled={!answers || answers?.length <= 2}
                        variant="transparent"
                        onClick={() => handleRemoveAnswer(answer.id)}
                      >
                        <Trash size={16} />
                      </ActionIcon> */}
                    </Flex>
                  ))}
                  {/* <Button className="quizz-add-btn" onClick={handleAddAnswer} variant="outline" leftSection={<Plus />}>
                    Thêm đáp án
                  </Button> */}
                </>
              )}
              {/* <Button type="submit">Sửa câu hỏi</Button> */}
            </Flex>
          </form>
        </Container>
      </Flex>
    </Fragment>
  );
};
