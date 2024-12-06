import { CourseCategoryList } from 'app/pages/admin/CourseCategory/CourseCategoryList';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';

export const CourseCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CourseCategoryList />} />
  </ErrorBoundaryRoutes>
);
