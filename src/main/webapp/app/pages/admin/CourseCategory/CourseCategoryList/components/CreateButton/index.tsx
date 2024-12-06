import React, { Fragment } from 'react';
import { Button } from '@mantine/core';
import { Plus } from '@phosphor-icons/react';
import { AdminRoutes } from 'app/pages/admin/routes';
import { adminPathName } from 'app/config/constants';
import { useNavigate } from 'react-router-dom';

export const CreateButton = () => {
  const navigate = useNavigate();

  return (
    <Fragment>
      <Button onClick={() => navigate(`${adminPathName}${AdminRoutes.COURSE_CATEGORY}/add`)} h={32} rightSection={<Plus />}>
        Thêm mới danh mục khóa học
      </Button>
    </Fragment>
  );
};
