import React, { Fragment } from 'react';
import './styles.scss';
import { CourseUpsertFormContextView } from 'app/pages/admin/Course/CourseUpsert/context/CourseUpsertFormContext';
import { useParams } from 'react-router-dom';
import { BreadcrumbsUpdate } from 'app/pages/admin/Course/CourseUpsert/components/BreadcrumbsUpdate';
import { BreadcrumbsCreate } from 'app/pages/admin/Course/CourseUpsert/components/BreadcrumbsCreate';
import { Box, Container, Loader, LoadingOverlay, Space } from '@mantine/core';
import { FormUpsert } from 'app/pages/admin/Course/CourseUpsert/components/FormUpsert';

const CourseUpsert = () => {
  return (
    <Fragment>
      <Box pos="relative">
        <LoadingOverlay style={{ borderRadius: '8px' }} zIndex={10} visible={false} loaderProps={{ children: <Loader /> }} />
        <Container style={{ borderRadius: '8px' }} p={20} fluid size="responsive" bg="#fff">
          <FormUpsert />
        </Container>
      </Box>
      <Space h="60" />
    </Fragment>
  );
};

const withCourseUpdate = WrappedComponent => {
  return props => {
    const { id } = useParams();

    // logic fetch course detail
    const initialValues = {};

    if (!id) return null;

    return (
      <CourseUpsertFormContextView initialValues={initialValues}>
        <>
          <BreadcrumbsUpdate />
          <WrappedComponent {...props} />
        </>
      </CourseUpsertFormContextView>
    );
  };
};

export const CourseUpdate = withCourseUpdate(CourseUpsert);

const withCourseCreate = WrappedComponent => {
  return props => {
    // logic khởi tạo form data
    const initialValues = {};

    return (
      <CourseUpsertFormContextView initialValues={initialValues}>
        <>
          <BreadcrumbsCreate />
          <WrappedComponent {...props} />
        </>
      </CourseUpsertFormContextView>
    );
  };
};

export const CourseCreate = withCourseCreate(CourseUpsert);
