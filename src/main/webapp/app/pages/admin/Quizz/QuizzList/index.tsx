import React, { Fragment } from 'react';
import { Container, Flex, Space, Text } from '@mantine/core';
import { FilterProvider } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/context/FilterContext';
import { SectionQuizzFilter } from 'app/pages/admin/Quizz/QuizzList/components/SectionQuizzFilter';
import { SectionQuizzList } from 'app/pages/admin/Quizz/QuizzList/components/SectionQuizzList';
import { CreateButton } from 'app/pages/admin/Quizz/QuizzList/components/CreateButton';

const Component = () => {
  return (
    <Fragment>
      <Flex align="center" justify="space-between">
        <Text fz={24} fw={700} c="#1F2A37">
          Câu hỏi
        </Text>
        <CreateButton />
      </Flex>
      <Space h={'md'} />
      <Container style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
        <SectionQuizzFilter />
        <Space h={'md'} />
        <SectionQuizzList />
      </Container>
      <Space h="60" />
    </Fragment>
  );
};

export const QuizzList = () => {
  return (
    <FilterProvider>
      <Component />
    </FilterProvider>
  );
};
