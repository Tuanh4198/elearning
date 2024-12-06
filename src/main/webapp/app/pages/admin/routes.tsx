import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Dashboard } from 'app/pages/admin/Dashboard';
import { CourseRoutes } from 'app/pages/admin/Course';
import { CourseCategoryRoutes } from 'app/pages/admin/CourseCategory';
import { ExamRoutes } from 'app/pages/admin/Exam';
import { ExamCategoryRoutes } from 'app/pages/admin/ExamCategory';
import { QuizzRoutes } from 'app/pages/admin/Quizz';
import { QuizzCategoryRoutes } from 'app/pages/admin/QuizzCategory';
import { Setting } from 'app/pages/admin/Setting';
import { NavigatorRoutes } from 'app/pages/admin/Navigator';

export const AdminRoutes = {
  DASHBOARD: '/',
  COURSE: '/course',
  COURSE_CATEGORY: '/course/category',
  EXAM: '/exam',
  EXAM_CATEGORY: '/exam/category',
  QUIZZ: '/quizz',
  QUIZZ_CATEGORY: '/quizz/category',
  NAVIGATOR: '/navigator',
  SETTING: '/setting',
};

export default () => {
  return (
    <ErrorBoundaryRoutes>
      <Route path={`${AdminRoutes.DASHBOARD}/*`} element={<Dashboard />} />
      <Route path={`${AdminRoutes.COURSE}/*`} element={<CourseRoutes />} />
      <Route path={`${AdminRoutes.COURSE_CATEGORY}/*`} element={<CourseCategoryRoutes />} />
      <Route path={`${AdminRoutes.EXAM}/*`} element={<ExamRoutes />} />
      <Route path={`${AdminRoutes.EXAM_CATEGORY}/*`} element={<ExamCategoryRoutes />} />
      <Route path={`${AdminRoutes.QUIZZ}/*`} element={<QuizzRoutes />} />
      <Route path={`${AdminRoutes.QUIZZ_CATEGORY}/*`} element={<QuizzCategoryRoutes />} />
      <Route path={`${AdminRoutes.NAVIGATOR}/*`} element={<NavigatorRoutes />} />
      <Route path={`${AdminRoutes.SETTING}/*`} element={<Setting />} />
    </ErrorBoundaryRoutes>
  );
};
