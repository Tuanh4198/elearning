import { ActionIcon, Group, Pagination, Table, Text, Tooltip } from '@mantine/core';
import React, { Fragment, useMemo, useState } from 'react';
import './styles.scss';
import { useFilter } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/context/FilterContext';
import { useFetchQuizzes } from 'app/pages/admin/Quizz/QuizzList/hooks/useFetchQuizzes';
import { useMutateQuizzes } from 'app/pages/admin/Quizz/QuizzList/hooks/useMutateQuizzes';
import { IQuizz } from 'app/shared/model/quizz.model';
import { useModalConfirm } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useModalConfirm';
import { QueryObserverResult, RefetchOptions } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import { PencilSimple, Trash } from '@phosphor-icons/react';
import { Link } from 'react-router-dom';
import { AdminRoutes } from 'app/pages/admin/routes';
import { adminPathName } from 'app/config/constants';

export const SectionQuizzList = () => {
  const { debouncedSearch, page, setPage } = useFilter();
  const { data: quizzes, size, total, refetch } = useFetchQuizzes({ search: debouncedSearch, page });

  const rows = useMemo(() => quizzes?.map(element => <RowQuizz key={element.id} element={element} refetch={refetch} />), [quizzes]);

  return (
    <Fragment>
      <Table verticalSpacing={'md'} highlightOnHover>
        <Table.Thead bg="#F9FAFB">
          <Table.Tr>
            <Table.Th>Tên câu hỏi</Table.Th>
            <Table.Th>Danh mục</Table.Th>
            <Table.Th>Loại câu hỏi</Table.Th>
            <Table.Th>Thao tác</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
      <Pagination total={Math.round(total / size)} value={page} onChange={setPage} mt="sm" />
    </Fragment>
  );
};

const RowQuizz = ({
  element,
  refetch,
}: {
  element: IQuizz;
  refetch: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IQuizz[], any>, Error>>;
}) => {
  const { modalConfirm, openModal, closeModal } = useModalConfirm({
    onOk() {
      onDelete(element.id);
    },
    content: 'Bạn chắc chắn muốn xóa câu hỏi này?',
    title: 'Xác nhận',
  });

  const { onDelete } = useMutateQuizzes({
    onSuccess() {
      refetch();
      closeModal();
    },
    onFailed() {},
  });

  return (
    <Table.Tr key={element?.id} h={50}>
      <Table.Td>
        <Tooltip label={element.content}>
          <Link to={`${adminPathName}${AdminRoutes.QUIZZ}/${element.id}`}>
            <Text>{element.content}</Text>
          </Link>
        </Tooltip>
      </Table.Td>
      <Table.Td>
        <Text>{element.category}</Text>
      </Table.Td>
      <Table.Td>
        <Text>{element.type}</Text>
      </Table.Td>
      <Table.Td>
        <Group>
          <Link to={`${adminPathName}${AdminRoutes.QUIZZ}/${element.id}`}>
            <ActionIcon variant={'outline'}>
              <PencilSimple />
            </ActionIcon>
          </Link>
          {modalConfirm}
          <ActionIcon color="red" variant={'outline'} onClick={openModal}>
            <Trash />
          </ActionIcon>
        </Group>
      </Table.Td>
    </Table.Tr>
  );
};
