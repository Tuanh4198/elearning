import React, { Fragment } from 'react';
import './styles.scss';
import { SectionExamBank } from 'app/pages/user/Exam/ExamList/components/SectionExamBank';
import { SectionExamEmployees } from 'app/pages/user/Exam/ExamList/components/SectionExamEmployees';

export const ExamList = () => {
  return (
    <Fragment>
      <SectionExamEmployees />
      <SectionExamBank />
    </Fragment>
  );
};
