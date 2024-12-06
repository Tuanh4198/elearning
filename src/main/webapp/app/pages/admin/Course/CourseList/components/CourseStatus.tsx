import { Box, Progress, Text } from '@mantine/core';
import { calculateDays, convertDateTimeFromServer, displayDefaultDateTime, formatDateTime } from 'app/shared/util/date-utils';
import React from 'react';
import '../styles.scss';
import { APP_TIME_DATE_FORMAT } from 'app/config/constants';

type Props = {
  start?: Date | null;
  end?: Date | null;
};
export default function CourseDeadline(props: Props) {
  const today = new Date();
  const start = props.start;
  const end = props.end;
  let displayText = '';
  let backgroundColor = '';
  let textColor = '';
  let progressColor = '';
  let progress = 0;

  if (!start) return null;

  if (end && end < today) {
    displayText = 'Đã hết hạn';
    backgroundColor = '#FFE3E3';
    textColor = '#FA5252';
  } else if (start > today) {
    displayText = `Bắt đầu vào ${formatDateTime(start, APP_TIME_DATE_FORMAT)}`;
    backgroundColor = '#E5E7EB';
    textColor = '#4B5563';
  } else if (end) {
    displayText = `${formatDateTime(end)} sẽ hết hạn`;
    backgroundColor = '#E7F5FF';
    textColor = '#2A2A86';
    progressColor = '#D0EBFF';
    const totalDuration = calculateDays(start, end);
    const elapsedDuration = calculateDays(today, start);
    progress = (elapsedDuration / totalDuration) * 100;
  } else {
    displayText = 'Không có hạn';
    backgroundColor = '#E5E7EB';
    textColor = '#6B7280';
  }
  const containerStyles = {
    '--background-color': backgroundColor,
    '--progress-color': progressColor,
    '--text-color': textColor,
  } as React.CSSProperties;

  return (
    <Box className="progress-container" style={containerStyles}>
      <Box className="progress-bar" style={{ width: `${progress}%` }}></Box>
      <Text className="progress-text">{displayText}</Text>
    </Box>
  );
}
