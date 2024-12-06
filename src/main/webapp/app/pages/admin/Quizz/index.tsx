import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';
import { AddQuizz } from 'app/pages/admin/Quizz/AddQuizz';
import { QuizzList } from 'app/pages/admin/Quizz/QuizzList';
import { UpdateQuizz } from 'app/pages/admin/Quizz/UpdateQuizz';

export const QuizzRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QuizzList />} />
    <Route path="add" element={<AddQuizz />} />
    <Route path=":id" element={<UpdateQuizz />} />
  </ErrorBoundaryRoutes>
);
