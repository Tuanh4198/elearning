import React, { Fragment } from 'react';
import { Container, Flex, Space, Text } from '@mantine/core';
import { SectionQuizzCategoryFilter } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/components/SectionQuizzCategoryFilter';
import { SectionQuizzCategoryList } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/components/SectionQuizzCategoryList';
import { FilterProvider, useFilter } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/context/FilterContext';
import { CreateButton } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/components/CreateButton';
import { useFetchCategories } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/hooks/useFetchCategories';

const Component = () => {
  const { debouncedSearch, page } = useFilter();
  const { data: categories, isLoading, size, total, refetch } = useFetchCategories({ search: debouncedSearch, page });

  return (
    <Fragment>
      <Flex align="center" justify="space-between">
        <Text fz={24} fw={700} c="#1F2A37">
          Danh mục câu hỏi
        </Text>
        <CreateButton refetch={refetch} />
      </Flex>
      <Space h={'md'} />
      <Container style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
        <SectionQuizzCategoryFilter />
        <Space h={'md'} />
        <SectionQuizzCategoryList categories={categories} isLoading={isLoading} size={size} total={total} refetch={refetch} />
      </Container>
      <Space h="60" />
    </Fragment>
  );
};

export const QuizzCategoryList = () => {
  return (
    <FilterProvider>
      <Component />
    </FilterProvider>
  );
};
