import { NavigatorUpsert } from 'app/pages/admin/Navigator/NavigatorUpsert';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import React from 'react';
import { Route } from 'react-router-dom';

export const NavigatorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<NavigatorUpsert />} />
  </ErrorBoundaryRoutes>
);
