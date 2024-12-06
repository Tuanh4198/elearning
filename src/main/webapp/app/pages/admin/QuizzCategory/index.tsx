import { QuizzCategoryList } from 'app/pages/admin/QuizzCategory/QuizzCategoryList';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';

export const QuizzCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuizzCategoryList />} />
  </ErrorBoundaryRoutes>
);
