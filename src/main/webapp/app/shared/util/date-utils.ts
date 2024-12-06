import dayjs, { ConfigType } from 'dayjs';
import 'dayjs/plugin/utc';
import 'dayjs/plugin/timezone';
import { APP_DATE_FORMAT, APP_DATE_SERVER_FORMAT, APP_LOCAL_DATETIME_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import moment from 'moment/moment';

export const convertDateFromServer = date => (date ? dayjs(date).format(APP_LOCAL_DATE_FORMAT) : null);

export const convertDateToServer = date => (date ? dayjs(date, APP_LOCAL_DATE_FORMAT).format(APP_DATE_SERVER_FORMAT) : null);

export const convertDateTimeFromServer = date => (date ? dayjs(date).format(APP_LOCAL_DATETIME_FORMAT) : null);

export const convertDateTimeToServer = date => (date ? dayjs(date).toDate() : null);

export const displayDefaultDateTime = () => dayjs().startOf('day').format(APP_LOCAL_DATETIME_FORMAT);

export const calculateDays = (start: Date, end: Date) => Math.round(Math.abs(+start - +end) / 8.64e7);

export const formatDateTime = (date: ConfigType, format?: string) => dayjs(date).format(format || APP_DATE_FORMAT);

export const formatDateTimeUtc = (date: ConfigType, format?: string) => dayjs.utc(date).format(format || APP_DATE_FORMAT);

export const formatDateZ = (date: Date) => {
  const originalMoment = moment(date).utcOffset(420);
  const dateZ = originalMoment.utc().format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  return dateZ;
};

export const compareDate = (date?: string) => {
  if (!date) return false;
  const dateToCompare = dayjs(date);
  const currentDate = dayjs();
  return dateToCompare.isAfter(currentDate);
};

export const dateUTCToLocaltime = (dateString?: string) => {
  if (!dateString) return undefined;
  const utcPlus7Date = dayjs.utc(dateString).tz('Asia/Bangkok');
  return utcPlus7Date.toDate();
};
