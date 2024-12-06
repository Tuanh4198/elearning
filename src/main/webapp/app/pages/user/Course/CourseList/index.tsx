import React, { Fragment } from 'react';
import './styles.scss';
import { SectionCourseList } from 'app/pages/user/Course/CourseList/components/SectionCourseList';
import { SectionCourseInProgress } from 'app/pages/user/Course/CourseList/components/SectionCourseInProgress';

export const CourseList = () => {
  return (
    <Fragment>
      <SectionCourseInProgress />
      <SectionCourseList />
    </Fragment>
  );
};
