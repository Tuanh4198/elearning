import React from 'react';
import { Translate } from 'react-jhipster';

const PageNotFound = () => {
  return (
    <div>
      <Translate contentKey="error.http.404">The page does not exist.</Translate>
    </div>
  );
};

export default PageNotFound;
