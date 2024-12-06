import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Home } from 'app/pages/user/Home';
import { CourseRoutes } from 'app/pages/user/Course';
import { ExamRoutes } from 'app/pages/user/Exam';

export const UserRoutes = {
  HOME: '/',
  COURSE: '/course',
  EXAM: '/exam',
};

export default () => {
  return (
    <ErrorBoundaryRoutes>
      <Route path={`${UserRoutes.HOME}/*`} element={<Home />} />
      <Route path={`${UserRoutes.COURSE}/*`} element={<CourseRoutes />} />
      <Route path={`${UserRoutes.EXAM}/*`} element={<ExamRoutes />} />
    </ErrorBoundaryRoutes>
  );
};
