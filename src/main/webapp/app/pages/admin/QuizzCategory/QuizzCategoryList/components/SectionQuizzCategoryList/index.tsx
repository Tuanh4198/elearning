import React, { useMemo } from 'react';
import './styles.scss';
import { ActionIcon, Box, Group, Loader, LoadingOverlay, Pagination, Table, Text, Tooltip } from '@mantine/core';
import { useFilter } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/context/FilterContext';
import { PencilSimple, Trash } from '@phosphor-icons/react';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';
import { useMutateCategory } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/hooks/useMutateCategory';
import { QueryObserverResult, RefetchOptions } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import { useModalUpsert } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/components/ModalUpsert';

export const SectionQuizzCategoryList = ({
  categories,
  isLoading,
  size,
  total,
  refetch,
}: {
  categories: IQuizzCategory[] | undefined;
  isLoading: boolean;
  size: number;
  total: number;
  refetch: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IQuizzCategory[], any>, Error>>;
}) => {
  const { page, setPage } = useFilter();

  const rows = useMemo(
    () => categories?.map(element => <RowCategory key={element?.id} element={element} refetch={refetch} />),
    [categories, refetch]
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
    </Box>
  );
};

const RowCategory = ({
  element,
  refetch,
}: {
  element: IQuizzCategory;
  refetch: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IQuizzCategory[], any>, Error>>;
}) => {
  const { onDelete } = useMutateCategory({
    onSuccess() {
      refetch();
      close();
    },
    onFailed() {},
  });

  const { modal, openModal } = useModalUpsert({
    initialValues: element,
    onSuccessed() {
      refetch();
    },
  });

  return (
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
          <ActionIcon variant={'outline'} onClick={openModal}>
            <PencilSimple />
          </ActionIcon>
          {modal}
          <ActionIcon variant={'outline'} onClick={() => onDelete(element.id)}>
            <Trash />
          </ActionIcon>
        </Group>
      </Table.Td>
    </Table.Tr>
  );
};
