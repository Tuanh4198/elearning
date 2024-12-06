import React from 'react';
import { Input } from '@mantine/core';
import { MagnifyingGlass } from '@phosphor-icons/react';
import { useFilter } from 'app/pages/admin/CourseCategory/CourseCategoryList/context/FilterContext';

export const SectionCourseCategoryFilter = () => {
  const { search, setSearch } = useFilter();
  return (
    <Input
      flex={1}
      value={search}
      w={380}
      h={32}
      placeholder={'Tìm kiếm danh mục'}
      leftSection={<MagnifyingGlass />}
      onChange={e => {
        setSearch(e.target.value);
      }}
    />
  );
};
