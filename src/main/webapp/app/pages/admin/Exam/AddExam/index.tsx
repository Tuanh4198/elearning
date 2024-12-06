import React, { Fragment, useCallback } from 'react';
import { useForm } from '@mantine/form';
import { Button, Checkbox, Container, Flex, Input, Select, Space, Textarea } from '@mantine/core';
import { DateTimePicker } from '@mantine/dates';
import { ExamMetafieldKey, IExam } from 'app/shared/model/exam.model';
import { ExamQuizzPoolMetafieldKey, IExamQuizzPool } from 'app/shared/model/exam-quizz-pool.model';
import { ExamQuizzPoolStrategyEnum } from 'app/shared/model/enumerations/exam-quizz-pool-strategy-enum.model';
import { ExamPointStrategyEnum } from 'app/shared/model/enumerations/exam-point-strategy-enum.model';
import { ExamQuizzAssignStrategyEnum } from 'app/shared/model/enumerations/exam-quizz-assign-strategy-enum.model';
import { IMetafield } from 'app/shared/model/metafields.model';
import { ExamStrategyEnum } from 'app/shared/model/enumerations/exam-strategy-enum.model';
import { useMutateExam } from 'app/pages/admin/Exam/hooks/useMutateExam';
import { adminPathName } from 'app/config/constants';
import { AdminRoutes } from 'app/pages/admin/routes';
import { useNavigate } from 'react-router-dom';
import { notiError } from 'app/shared/notifications';
import { addExam } from 'app/pages/admin/Exam/apis';

const initalValues: IExam = {
  title: '',
  requireJoin: false,
  assignStrategy: ExamQuizzAssignStrategyEnum.SPEC_USERS, // Example value
  assignStrategyJson: '',
  pointStrategy: ExamPointStrategyEnum.PERCENTAGE, // Example value
  minPointToPass: 0,
  description: '',
  thumbUrl: '',
  numberOfQuestion: 0,
  poolStrategy: ExamQuizzPoolStrategyEnum.WEIGHT, // Example value
  examStrategy: ExamStrategyEnum.RANDOM,
  quizzPools: [
    {
      metafields: [
        {
          key: ExamQuizzPoolMetafieldKey.weight,
          value: '100',
        },
      ],
    },
  ],
  metafields: [
    {
      key: ExamMetafieldKey.working_time,
      value: '5',
    },
    {
      key: ExamMetafieldKey.max_number_of_test,
      value: '10',
    },
  ] as IMetafield<ExamMetafieldKey, string>[],
};
export const AddExam = () => {
  const navigate = useNavigate();
  const form = useForm<IExam>({
    initialValues: initalValues,
  });

  const { onAdd } = useMutateExam({
    onSuccess() {
      navigate(`${adminPathName}${AdminRoutes.EXAM}`);
    },
    onFailed() {},
  });

  // Add/Remove metafields
  const addMetafield = () => {
    const newMetafield = { key: '', value: '' };
    form.setFieldValue('metafields', [...(form.values.metafields || ([] as any)), newMetafield]);
  };

  const removeMetafield = (index: number) => {
    form.setFieldValue(
      'metafields',
      (form.values.metafields || []).filter((_, i) => i !== index)
    );
  };

  // Add/Remove quizzPools
  const addQuizzPool = () => {
    const newQuizzPool: IExamQuizzPool = {
      metafields: [{ key: ExamQuizzPoolMetafieldKey.weight, value: '100' }],
    };
    form.setFieldValue('quizzPools', [...(form.values.quizzPools || []), newQuizzPool]);
  };

  const removeQuizzPool = (index: number) => {
    form.setFieldValue(
      'quizzPools',
      (form.values.quizzPools || []).filter((_, i) => i !== index)
    );
  };

  const handleSubmit = useCallback((values: IExam) => {
    if (!values.applyTime || !values.expireTime) {
      notiError({ message: 'Chưa chọn ngày bắt đầu, ngày kết thúc' });
      return;
    }
    const submitValues = JSON.parse(JSON.stringify(values));
    const applyDate = new Date(submitValues.applyTime);
    const utcApplyDate = applyDate.toISOString();

    console.log(utcApplyDate);

    const expireDate = new Date(submitValues.expireTime);
    const utcExpireDate = expireDate.toISOString();

    submitValues.applyTime = utcApplyDate;
    submitValues.expireTime = utcExpireDate;
    console.log('Submitting:', submitValues);
    addExam(submitValues);
    // Handle form submission
  }, []);

  return (
    <Fragment>
      <Container>
        <form onSubmit={form.onSubmit(handleSubmit)}>
          <Flex direction="column" gap="md">
            <Input.Wrapper label="Tiêu đề">
              <Input placeholder="Tiêu đề bài kiểm tra" {...form.getInputProps('title')} />
            </Input.Wrapper>
            <Input.Wrapper label="Danh mục">
              <Input placeholder="Category ID" type="number" {...form.getInputProps('categoryId')} />
            </Input.Wrapper>
            <Checkbox label="Bắt buộc tham gia" {...form.getInputProps('requireJoin', { type: 'checkbox' })} />
            <Select
              placeholder="Cách giao"
              data={['SPEC_USERS']} // Example data
              {...form.getInputProps('assignStrategy')}
            />
            <Input placeholder="Giao cho ai" {...form.getInputProps('assignStrategyJson')} />
            <DateTimePicker placeholder="Thời gian bắt đầu" {...form.getInputProps('applyTime')} />
            <DateTimePicker placeholder="Thời gian kết thúc" {...form.getInputProps('expireTime')} />
            <Select
              placeholder="Cách tính điểm"
              data={['PERCENTAGE']} // Example data
              {...form.getInputProps('pointStrategy')}
            />
            <Input.Wrapper label="Điểm tối thiểu">
              <Input placeholder="Điểm tối thiểu" type="number" {...form.getInputProps('minPointToPass')} />
            </Input.Wrapper>
            <Input.Wrapper label="Mô tả">
              <Textarea placeholder="Mô tả" {...form.getInputProps('description')} />
            </Input.Wrapper>
            <Input.Wrapper label="Ảnh thumb">
              <Input placeholder="Ảnh thumb" {...form.getInputProps('thumbUrl')} />
            </Input.Wrapper>
            <Input.Wrapper label="Id khóa học">
              <Input placeholder="Nhập id khóa học" type="number" {...form.getInputProps('courseId')} />
            </Input.Wrapper>
            <Input.Wrapper label="Số lượng câu hỏi">
              <Input placeholder="Nhập số lượng câu hỏi" type="number" {...form.getInputProps('numberOfQuestion')} />
            </Input.Wrapper>
            <Select
              placeholder="Chiến lược chọn câu hỏi"
              data={['WEIGHT', 'MANUAL']} // Example data
              {...form.getInputProps('poolStrategy')}
            />
            <Select
              placeholder="Chiến lược sắp đề"
              data={['RANDOM', 'ALL']} // Example data
              {...form.getInputProps('examStrategy')}
            />
            <Space h="md" />
            <div>
              <strong>Metafields</strong>
              {form.values.metafields &&
                form.values.metafields.map((metafield, index) => (
                  <Flex key={index} direction="row" align="center" gap="md">
                    <Input
                      placeholder="Key"
                      value={metafield.key}
                      onChange={e => {
                        const updatedMetafields = [...(form.values.metafields || [])];
                        updatedMetafields[index].key = e.currentTarget.value as ExamMetafieldKey;
                        form.setFieldValue('metafields', updatedMetafields);
                      }}
                    />
                    <Input
                      placeholder="Value"
                      value={metafield.value}
                      onChange={e => {
                        if (!form.values.metafields) return;
                        const updatedMetafields = [...form.values.metafields];
                        updatedMetafields[index].value = e.currentTarget.value;
                        form.setFieldValue('metafields', updatedMetafields);
                      }}
                    />
                    <Button color="red" onClick={() => removeMetafield(index)}>
                      Xóa metafields
                    </Button>
                  </Flex>
                ))}
              <Button onClick={addMetafield}>Thêm Metafield</Button>
            </div>

            {/* Quizz Pools */}
            <Space h="md" />
            <div>
              <strong>Pool câu hỏi</strong>
              {form.values.quizzPools &&
                form.values.quizzPools.map((pool, index) => (
                  <Flex key={index} direction="column" gap="md">
                    <Input
                      placeholder="Source ID"
                      value={pool.sourceId}
                      onChange={e => {
                        const updatedPools = [...(form.values.quizzPools || [])];
                        updatedPools[index].sourceId = Number(e.currentTarget.value);
                        form.setFieldValue('quizzPools', updatedPools);
                      }}
                    />
                    {/* Metafields for Quizz Pool */}
                    {pool.metafields &&
                      pool.metafields.map((metafield, metafieldIndex) => (
                        <Flex key={metafieldIndex} direction="row" align="center" gap="md">
                          <Input
                            placeholder="Key"
                            value={metafield.key}
                            onChange={e => {
                              const updatedPools = [...(form.values.quizzPools || [])];
                              if (!updatedPools?.[index]?.metafields?.[metafieldIndex]) return;
                              // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                              updatedPools[index].metafields![metafieldIndex]!.key = e.currentTarget.value as ExamQuizzPoolMetafieldKey;
                              form.setFieldValue('quizzPools', updatedPools);
                            }}
                          />
                          <Input
                            placeholder="Value"
                            value={metafield.value}
                            onChange={e => {
                              if (!form.values.quizzPools) return;
                              const updatedPools = [...form.values.quizzPools];
                              if (!updatedPools?.[index]?.metafields || !updatedPools?.[index]?.metafields?.[metafieldIndex]) return;
                              // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                              updatedPools[index].metafields![metafieldIndex]!.value = e.currentTarget.value;
                              form.setFieldValue('quizzPools', updatedPools);
                            }}
                          />
                          <Button
                            color="red"
                            onClick={() => {
                              if (!form.values.quizzPools) return;
                              const updatedPools = [...form.values.quizzPools];
                              if (!updatedPools || !updatedPools[index]) return;
                              updatedPools[index].metafields = updatedPools[index].metafields?.filter((_, i) => i !== metafieldIndex);
                              form.setFieldValue('quizzPools', updatedPools);
                            }}
                          >
                            Xóa Metafield
                          </Button>
                        </Flex>
                      ))}
                  </Flex>
                ))}
              <Button onClick={addQuizzPool}>Thêm pool</Button>
            </div>
            <Space h="md" />
            <Button type="submit">Submit</Button>
          </Flex>
        </form>
      </Container>
      <Space h="60" />
    </Fragment>
  );
};
