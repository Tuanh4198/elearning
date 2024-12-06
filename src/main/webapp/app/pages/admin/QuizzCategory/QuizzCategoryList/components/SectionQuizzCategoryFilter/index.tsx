import React from 'react';
import { Input } from '@mantine/core';
import { MagnifyingGlass } from '@phosphor-icons/react';
import { useFilter } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/context/FilterContext';

export const SectionQuizzCategoryFilter = () => {
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
