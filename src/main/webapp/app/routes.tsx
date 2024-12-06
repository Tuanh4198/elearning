import React from 'react';
import { Route } from 'react-router-dom';
import LoginRedirect from 'app/modules/login/login-redirect';
import Admin from 'app/modules/administration';
import Logout from 'app/modules/login/logout';
import AdminRoutes from 'app/pages/admin/routes';
import UserRoutes from 'app/pages/user/routes';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { adminPathName, AUTHORITIES, loginUrl, logoutUrl, userPathName } from 'app/config/constants';

const AppRoutes = () => {
  return (
    <ErrorBoundaryRoutes>
      <Route path={logoutUrl} element={<Logout />} />
      <Route path="docs" element={<Admin />} />
      {/* Admin routes */}
      <Route
        path={`${adminPathName}/*`}
        element={
          // <PrivateRoute hasAnyAuthorities={[AUTHORITIES.manage]}>
          <PrivateRoute>
            <AdminRoutes />
          </PrivateRoute>
        }
      />
      {/* User routes */}
      <Route
        path={`${userPathName}*`}
        element={
          <PrivateRoute>
            <UserRoutes />
          </PrivateRoute>
        }
      />
      <Route path={loginUrl} element={<LoginRedirect />} />
      <Route path="*" element={<PageNotFound />} />
    </ErrorBoundaryRoutes>
  );
};

export default AppRoutes;
