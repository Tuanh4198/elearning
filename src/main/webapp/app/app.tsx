import React from 'react';
import { UserAppFrame } from './shared/layout/user';
import { AdminAppFrame } from './shared/layout/admin';
import { useLocation } from 'react-router-dom';
import { adminPathName } from 'app/config/constants';

export const App = () => {
  const locationReactRouterDom = useLocation();

  if (locationReactRouterDom.pathname.startsWith(adminPathName)) {
    return <AdminAppFrame title={''} />;
  }

  return <UserAppFrame title={''} />;
};

export default App;
