import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';
import { AddExam } from 'app/pages/admin/Exam/AddExam';
import { ExamList } from 'app/pages/admin/Exam/ExamList';
import { ExamDetail } from 'app/pages/admin/Exam/ExamDetail';

export const ExamRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExamList />} />
    <Route path="add" element={<AddExam />} />
    <Route path=":id" element={<ExamDetail />} />
  </ErrorBoundaryRoutes>
);
