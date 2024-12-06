import { AnyAction, configureStore, ThunkAction } from '@reduxjs/toolkit';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import sharedReducers from 'app/shared/reducers';
import errorMiddleware from './error-middleware';
import notificationMiddleware from './notification-middleware';
import loggerMiddleware from './logger-middleware';

export type IRootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export type AppThunk<ReturnType = void> = ThunkAction<ReturnType, IRootState, unknown, AnyAction>;

const store = configureStore({
  reducer: sharedReducers,
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActionPaths: ['payload.headers', 'payload.config', 'payload.request', 'error', 'meta.arg'],
      },
    }).concat(errorMiddleware, notificationMiddleware),
  // }).concat(errorMiddleware, notificationMiddleware, loggerMiddleware),
});

const getStore = () => store;

export const useAppSelector: TypedUseSelectorHook<IRootState> = useSelector;
export const useAppDispatch = () => useDispatch<AppDispatch>();
export default getStore;
