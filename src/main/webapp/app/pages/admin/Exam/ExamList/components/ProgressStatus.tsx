import { Container, Text } from '@mantine/core';
import React from 'react';

type Props = {
  current: number;
  max: number;
};
export default function ProgressStatus(props: Props) {
  const percent = (props.current / props.max) * 100;
  let color = '#12B886';

  if (percent === 0) {
    color = '#228BE6';
  } else if (percent <= 50) {
    color = '#FA5252';
  } else if (percent < 100) {
    color = '#FD7E14';
  }
  return (
    <div
      style={{
        borderRadius: 24,
        backgroundColor: color,
        color: '#fff',
        padding: '0 8px',
      }}
    >
      <Text fz={12} fw={500}>
        {percent}%
      </Text>
    </div>
  );
}
