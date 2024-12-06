import React from 'react';
import { Flex, Select, TextInput } from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { MagnifyingGlass, CalendarBlank } from '@phosphor-icons/react';

export const SectionFilterGroup = () => {
  return (
    <Flex gap={12} justify="space-between" mb={20}>
      <TextInput flex={7} radius={4} placeholder="Tìm kiếm kì thi" leftSectionPointerEvents="none" leftSection={<MagnifyingGlass />} />
      <Select flex={3} radius={4} placeholder="Danh mục" data={['React', 'Angular', 'Vue', 'Svelte']} />
      <Select flex={3} radius={4} placeholder="Trạng thái" data={['React', 'Angular', 'Vue', 'Svelte']} />
      <DateInput radius={4} rightSection={<CalendarBlank />} flex={4} placeholder="Chọn thời gian" />
    </Flex>
  );
};
