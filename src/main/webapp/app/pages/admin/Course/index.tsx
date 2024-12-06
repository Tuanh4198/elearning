import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';
import { CourseCreate, CourseUpdate } from 'app/pages/admin/Course/CourseUpsert';
import { CourseList } from 'app/pages/admin/Course/CourseList';
import { CourseDetail } from 'app/pages/admin/Course/CourseDetail';

export const CourseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CourseList />} />
    <Route path="create" element={<CourseCreate />} />
    <Route path=":id" element={<CourseDetail />}>
      <Route path="update" element={<CourseUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);
