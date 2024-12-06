import dayjs from 'dayjs';
import duration from 'dayjs/plugin/duration';

export const parseNumber = (num: number) => {
  if (!isNaN(num) && num % 1 !== 0) {
    return parseFloat(num.toFixed(2));
  } else {
    return num;
  }
};

export const calcDurationTimeFunc = (startAt?: string, finishedAt?: string) => {
  if (!startAt || !finishedAt) return null;
  dayjs.extend(duration);
  const startTime = dayjs(startAt);
  const finishedTime = dayjs(finishedAt);
  const diff = dayjs.duration(finishedTime.diff(startTime));
  const formattedTime = diff.format('mm:ss');
  return formattedTime;
};
