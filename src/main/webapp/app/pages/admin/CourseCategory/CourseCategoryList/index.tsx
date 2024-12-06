import React, { Fragment } from 'react';
import { Container, Flex, Space, Text } from '@mantine/core';
import { SectionCourseCategoryFilter } from 'app/pages/admin/CourseCategory/CourseCategoryList/components/SectionCourseCategoryFilter';
import { SectionCourseCategoryList } from 'app/pages/admin/CourseCategory/CourseCategoryList/components/SectionCourseCategoryList';
import { FilterProvider } from 'app/pages/admin/CourseCategory/CourseCategoryList/context/FilterContext';
import { CreateButton } from 'app/pages/admin/CourseCategory/CourseCategoryList/components/CreateButton';

const Component = () => {
  return (
    <Fragment>
      <Flex align="center" justify="space-between">
        <Text fz={24} fw={700} c="#1F2A37">
          Danh mục khóa học
        </Text>
        <CreateButton />
      </Flex>
      <Space h={'md'} />
      <Container style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff" bd="1px solid #E5E7EB">
        <SectionCourseCategoryFilter />
        <Space h={'md'} />
        <SectionCourseCategoryList />
      </Container>
      <Space h="60" />
    </Fragment>
  );
};

export const CourseCategoryList = () => {
  return (
    <FilterProvider>
      <Component />
    </FilterProvider>
  );
};
