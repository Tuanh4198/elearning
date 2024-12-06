/* eslint-disable @typescript-eslint/no-misused-promises */
import { Button, Flex, Modal, TextInput, Text } from '@mantine/core';
import { isNotEmpty, useForm } from '@mantine/form';
import { useDisclosure } from '@mantine/hooks';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';
import { notiError, notiSuccess } from 'app/shared/notifications';
import axios from 'axios';
import React, { useMemo, useState } from 'react';

const ModalUpsert = ({
  initialValues,
  onSuccessed,
  onClose,
}: {
  initialValues?: IQuizzCategory;
  onSuccessed: () => void;
  onClose: () => void;
}) => {
  const [submitting, setSubmitting] = useState(false);

  const form = useForm<IQuizzCategory>({
    mode: 'uncontrolled',
    initialValues,
    validate: {
      title: isNotEmpty('Tên bắt buộc nhập'),
    },
  });

  const onSubmit = async (values: IQuizzCategory) => {
    setSubmitting(true);
    try {
      const body = {
        ...initialValues,
        ...values,
      };
      let res;
      if (initialValues && initialValues.id) {
        res = await axios.put<IQuizzCategory>(`/api/quizz-categories/${body.id}`, body);
      } else {
        res = await axios.post<IQuizzCategory>(`/api/quizz-categories`, body);
      }
      onSuccessed();
      onClose();
      notiSuccess({ message: `${body.id ? 'Sửa' : 'Tạo'} danh mục ${body.title} thành công` });
    } catch (error: any) {
      const msgErr = error?.response?.data?.errors?.message?.[0];
      notiError({ message: msgErr ?? 'Tạo thất bại, vui lòng thử lại sau.' });
    }
    setSubmitting(false);
  };

  return (
    <form onSubmit={form?.onSubmit(onSubmit)} className="modal-body">
      <TextInput
        w="100%"
        autoFocus
        placeholder="Nhập tên danh mục"
        c="#4B5563"
        mb={20}
        maxLength={255}
        withAsterisk
        {...form?.getInputProps('title')}
      />
      <Flex justify="flex-end" gap={20}>
        <Button disabled={submitting} variant="outline" onClick={onClose}>
          Hủy
        </Button>
        <Button type="submit" loading={submitting} variant="filled">
          {initialValues ? 'Sửa' : 'Tạo'}
        </Button>
      </Flex>
    </form>
  );
};

export const useModalUpsert = ({ initialValues, onSuccessed }: { initialValues?: IQuizzCategory; onSuccessed: () => void }) => {
  const [opened, { open, close }] = useDisclosure(false);

  const modal = useMemo(
    () => (
      <Modal
        closeOnEscape={false}
        opened={opened}
        onClose={close}
        title={
          <Text color="#111928" fw={600} fz={18}>
            {initialValues ? 'Sửa' : 'Tạo mới'} danh mục câu hỏi
          </Text>
        }
        size="sm"
        radius={8}
      >
        <ModalUpsert initialValues={initialValues} onClose={close} onSuccessed={onSuccessed} />
      </Modal>
    ),
    [opened, initialValues, onSuccessed]
  );

  return {
    openModal: open,
    closeModal: close,
    modal,
  };
};
