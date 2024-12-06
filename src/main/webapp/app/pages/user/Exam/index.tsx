import { ExamDetailCanEnjoy, ExamDetailCanNotEnjoy } from 'app/pages/user/Exam/ExamDetail';
import { ExamList } from 'app/pages/user/Exam/ExamList';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';

export const ExamRoutes = () => {
  return (
    <ErrorBoundaryRoutes>
      <Route index element={<ExamList />} />
      <Route path=":id" element={<ExamDetailCanNotEnjoy />} />
      <Route path="employee/:id" element={<ExamDetailCanEnjoy />} />
    </ErrorBoundaryRoutes>
  );
};
