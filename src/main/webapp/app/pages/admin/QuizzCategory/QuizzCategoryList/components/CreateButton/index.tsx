import React, { Fragment } from 'react';
import { Button } from '@mantine/core';
import { Plus } from '@phosphor-icons/react';
import { useModalUpsert } from 'app/pages/admin/QuizzCategory/QuizzCategoryList/components/ModalUpsert';
import { useFilter } from 'app/pages/admin/Quizz/QuizzList/context/FilterContext';
import { RefetchOptions, QueryObserverResult } from '@tanstack/react-query';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';
import { AxiosResponse } from 'axios';

export const CreateButton = ({
  refetch,
}: {
  refetch: (options?: RefetchOptions) => Promise<QueryObserverResult<AxiosResponse<IQuizzCategory[], any>, Error>>;
}) => {
  const { search, page, setPage } = useFilter();

  const { modal, openModal } = useModalUpsert({
    onSuccessed() {
      if (search != null && search?.trim() !== '') return;
      if (page > 0) {
        setPage(0);
      } else {
        refetch();
      }
    },
  });

  return (
    <Fragment>
      <Button onClick={openModal} h={32} rightSection={<Plus />}>
        Thêm mới danh mục câu hỏi
      </Button>
      {modal}
    </Fragment>
  );
};
