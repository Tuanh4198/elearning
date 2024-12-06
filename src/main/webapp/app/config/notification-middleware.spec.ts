import { createStore, applyMiddleware } from 'redux';
import { TranslatorContext } from 'react-jhipster';

import notificationMiddleware from './notification-middleware';

describe('Notification Middleware', () => {
  let store;

  const SUCCESS_TYPE = 'SUCCESS/fulfilled';
  const ERROR_TYPE = 'ERROR/rejected';

  // Default action for use in local tests
  const DEFAULT = {
    type: SUCCESS_TYPE,
    payload: 'foo',
  };
  const HEADER_SUCCESS = {
    type: SUCCESS_TYPE,
    payload: {
      status: 201,
      statusText: 'Created',
      headers: { 'app-alert': 'foo.created', 'app-params': 'foo' },
    },
  };

  const DEFAULT_ERROR = {
    type: ERROR_TYPE,
    error: new Error('foo'),
  };
  const VALIDATION_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        data: {
          type: 'https://www.jhipster.tech/problem/constraint-violation',
          title: 'Method argument not valid',
          status: 400,
          path: '/api/foos',
          message: 'error.validation',
          fieldErrors: [{ objectName: 'foos', field: 'minField', message: 'Min' }],
        },
        status: 400,
        statusText: 'Bad Request',
        headers: { expires: '0' },
      },
    },
  };
  const HEADER_ERRORS = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        status: 400,
        statusText: 'Bad Request',
        headers: { 'app-error': 'foo.creation', 'app-params': 'foo' },
      },
    },
  };
  const NOT_FOUND_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        data: {
          status: 404,
          message: 'Not found',
        },
        status: 404,
      },
    },
  };
  const NO_SERVER_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        status: 0,
      },
    },
  };
  const GENERIC_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        data: {
          message: 'Error',
        },
      },
    },
  };
  const LOGIN_REJECTED_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        data: {
          title: 'Unauthorized',
          status: 401,
          path: '/api/authenticate',
          message: 'error.http.401',
        },
        status: 401,
      },
    },
  };

  const TITLE_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        data: {
          title: 'Incorrect password',
          status: 400,
          type: 'https://www.jhipster.tech/problem/invalid-password',
        },
        status: 400,
      },
    },
  };

  const STRING_DATA_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        data: 'Incorrect password string',
        status: 400,
      },
    },
  };

  const UNKNON_400_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
      response: {
        status: 400,
      },
    },
  };

  const UNKNON_ERROR = {
    type: ERROR_TYPE,
    error: {
      isAxiosError: true,
    },
  };

  const makeStore = () => applyMiddleware(notificationMiddleware)(createStore)(() => null);

  beforeAll(() => {
    TranslatorContext.registerTranslations('vi', {});
  });

  beforeEach(() => {
    store = makeStore();
  });

  it('should not trigger a toast message but should return action', () => {
    expect(store.dispatch(DEFAULT).payload).toEqual('foo');
  });
});
