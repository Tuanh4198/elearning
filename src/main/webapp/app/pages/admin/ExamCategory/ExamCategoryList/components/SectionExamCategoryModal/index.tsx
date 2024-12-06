import { Button, Flex, Input, Modal, Space } from '@mantine/core';
import React, { useCallback } from 'react';
import { useDisclosure } from '@mantine/hooks';
import { useForm } from '@mantine/form';
import { useMutateCategory } from 'app/pages/admin/ExamCategory/ExamCategoryList/hooks/useMutateCategory';
import { useFilter } from 'app/pages/admin/ExamCategory/ExamCategoryList/context/FilterContext';
import { useFetchCategories } from 'app/pages/admin/ExamCategory/ExamCategoryList/hooks/useFetchCategories';
import { ICategory } from 'app/shared/model/category.model';

interface IExamCategoryModal {
  params: ReturnType<typeof useDisclosure>;
  initialValues: ICategory;
  title: string;
  buttonSubmitContent: string;
}

export const SectionExamCategoryModal = ({ params, initialValues, title, buttonSubmitContent }: IExamCategoryModal) => {
  const [isOpen, { close, open, toggle }] = params;
  const { debouncedSearch, page } = useFilter();
  const { refetch } = useFetchCategories({ search: debouncedSearch, page });

  const form = useForm({
    mode: 'uncontrolled',
    initialValues,
    validate: {
      title: value => (value && value.length > 0 ? null : 'Tên danh mục không hợp lệ'),
    },
  });

  const { onAdd, onUpdate } = useMutateCategory({
    onSuccess() {
      refetch();
      close();
    },
    onFailed() {},
  });

  const submitAdd = useCallback(() => {
    const values = form.getValues();
    if (values.id) {
      onUpdate(values);
    } else {
      onAdd(values);
    }
  }, []);

  return (
    <Modal opened={isOpen} onClose={close} title={<b>{title}</b>}>
      <form>
        <Input key={form.key('id')} {...form.getInputProps('id')} style={{ display: 'none' }} />
        <Input placeholder={'Nhập tên danh mục'} key={form.key('title')} {...form.getInputProps('title')} />
      </form>
      <Space h={16} />
      <Flex direction={'row'} justify={'flex-end'}>
        <Button variant="outline" onClick={close}>
          Hủy
        </Button>
        <Space w={16} />
        <Button onClick={submitAdd}>{buttonSubmitContent}</Button>
      </Flex>
    </Modal>
  );
};
