import { Button, Flex, Input, Modal, Space } from '@mantine/core';
import React, { useCallback } from 'react';
import { useDisclosure } from '@mantine/hooks';
import { useForm } from '@mantine/form';
import { useMutateCategory } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/hooks/useMutateCategory';
import { useFilter } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/context/FilterContext';
import { useFetchCategories } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/hooks/useFetchCategories';
import { ICategoryRequest } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/apis';

interface IQuizzCategoryModal {
  params: ReturnType<typeof useDisclosure>;
  initialValues: ICategoryRequest;
  title: string;
  buttonSubmitContent: string;
}

export const SectionQuizzCategoryModal = ({ params, initialValues, title, buttonSubmitContent }: IQuizzCategoryModal) => {
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
