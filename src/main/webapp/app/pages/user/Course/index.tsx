import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { CourseList } from 'app/pages/user/Course/CourseList';
import { CourseDetail } from 'app/pages/user/Course/CourseDetail';

export const CourseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CourseList />} />
    <Route path=":id" element={<CourseDetail />} />
  </ErrorBoundaryRoutes>
);
