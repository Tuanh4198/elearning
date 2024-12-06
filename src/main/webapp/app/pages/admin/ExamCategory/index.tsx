import { ExamCategoryList } from 'app/pages/admin/ExamCategory/ExamCategoryList';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';

export const ExamCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExamCategoryList />} />
  </ErrorBoundaryRoutes>
);
