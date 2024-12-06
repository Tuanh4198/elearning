import { ActionIcon, Box, Group, Loader, LoadingOverlay, Pagination, Table, Text, Tooltip } from '@mantine/core';
import React, { useMemo, useState } from 'react';
import { useFetchCategories } from 'app/pages/admin/CourseCategory/CourseCategoryList/hooks/useFetchCategories';
import './styles.scss';
import { useFilter } from 'app/pages/admin/CourseCategory/CourseCategoryList/context/FilterContext';
import { PencilSimple, Trash } from '@phosphor-icons/react';
import { useDisclosure } from '@mantine/hooks';
import { ICategory } from 'app/shared/model/category.model';
import { useMutateCategory } from 'app/pages/admin/CourseCategory/CourseCategoryList/hooks/useMutateCategory';
import { SectionCourseCategoryModal } from 'app/pages/admin/CourseCategory/CourseCategoryList/components/SectionCourseCategoryModal';

export const SectionCourseCategoryList = () => {
  const { debouncedSearch, page, setPage } = useFilter();
  const { data: categories, isLoading, size, total, refetch } = useFetchCategories({ search: debouncedSearch, page });

  const [opened, { open, close, toggle }] = useDisclosure(false);
  const [selectedCategory, setSelectedCategory] = useState<ICategory>({
    title: '',
  });

  const { onDelete } = useMutateCategory({
    onSuccess() {
      refetch();
      close();
    },
    onFailed() {},
  });

  const rows = useMemo(
    () =>
      categories?.map(element => (
        <Table.Tr key={element?.id} h={50}>
          <Table.Td>
            <Tooltip label={element.id}>
              <Text>{element.id}</Text>
            </Tooltip>
          </Table.Td>
          <Table.Td>
            <Tooltip label={element.title}>
              <Text>{element.title}</Text>
            </Tooltip>
          </Table.Td>
          <Table.Td>
            <Text>{element.createdBy}</Text>
          </Table.Td>
          <Table.Td>
            <Group>
              <ActionIcon
                variant={'outline'}
                onClick={() => {
                  setSelectedCategory(element);
                  open();
                }}
              >
                <PencilSimple />
              </ActionIcon>
              <ActionIcon
                variant={'outline'}
                onClick={() => {
                  onDelete(element.id);
                }}
              >
                <Trash />
              </ActionIcon>
            </Group>
          </Table.Td>
        </Table.Tr>
      )),
    [categories]
  );

  return (
    <Box pos="relative">
      <LoadingOverlay style={{ borderRadius: '8px' }} zIndex={10} visible={isLoading} loaderProps={{ children: <Loader /> }} />
      <Table verticalSpacing={'md'} highlightOnHover>
        <Table.Thead bg="#F9FAFB">
          <Table.Tr>
            <Table.Th>Id danh mục</Table.Th>
            <Table.Th>Tên danh mục</Table.Th>
            <Table.Th>Người tạo</Table.Th>
            <Table.Th>Thao tác</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
      {Math.ceil(total / size) > 1 && (
        <Pagination
          total={Math.round(total / size)}
          value={page}
          disabled={isLoading}
          hideWithOnePage
          radius="50%"
          onChange={setPage}
          mt="sm"
        />
      )}
      {opened && (
        <SectionCourseCategoryModal
          params={[opened, { open, close, toggle }]}
          title={'Tạo danh mục mới'}
          initialValues={selectedCategory}
          buttonSubmitContent={'Cập nhật'}
        />
      )}
    </Box>
  );
};
