import React, { Fragment } from 'react';
import { Container, Flex, Space, Text } from '@mantine/core';
import { CreateButton } from 'app/pages/admin/ExamCategory/ExamCategoryList/components/CreateButton';
import { SectionExamCategoryFilter } from 'app/pages/admin/ExamCategory/ExamCategoryList/components/SectionExamCategoryFilter';
import { SectionExamCategoryList } from 'app/pages/admin/ExamCategory/ExamCategoryList/components/SectionExamCategoryList';
import { FilterProvider } from 'app/pages/admin/ExamCategory/ExamCategoryList/context/FilterContext';

const Component = () => {
  return (
    <Fragment>
      <Flex align="center" justify="space-between">
        <Text fz={24} fw={700} c="#1F2A37">
          Danh mục kì thi
        </Text>
        <CreateButton />
      </Flex>
      <Space h={'md'} />
      <Container style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
        <SectionExamCategoryFilter />
        <Space h={'md'} />
        <SectionExamCategoryList />
      </Container>
      <Space h="60" />
    </Fragment>
  );
};

export const ExamCategoryList = () => {
  return (
    <FilterProvider>
      <Component />
    </FilterProvider>
  );
};
