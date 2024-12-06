import React from 'react';
import { Flex } from '@mantine/core';

export const Empty = ({ description }: { description: string }) => {
  return (
    <Flex align={'center'} justify={'center'} direction={'column'} className={`empty-wrapper`}>
      <p>{description}</p>
    </Flex>
  );
};
