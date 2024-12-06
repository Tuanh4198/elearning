import React from 'react';
import './styles.scss';
import { Flex, Grid, Box, Text } from '@mantine/core';
import { BlockTimetable } from 'app/pages/user/Home/components/BlockTimetable';
import { BlockReport } from 'app/pages/user/Home/components/BlockReport';
import { BlockCourseCategories } from 'app/pages/user/Home/components/BlockCourseCategories';
import { BlockExamCategories } from 'app/pages/user/Home/components/BlockExamCategories';
import { BlockEmployeeCapacity } from 'app/pages/user/Home/components/BlockEmployeeCapacity';

export const Home = () => {
  return (
    <Grid gutter="20px">
      <Grid.Col span={8}>
        <Flex direction="column" gap="20px">
          <BlockReport />
          <BlockTimetable />
          <Grid gutter="20px">
            <Grid.Col span={6}>
              <BlockCourseCategories />
            </Grid.Col>
            <Grid.Col span={6}>
              <BlockExamCategories />
            </Grid.Col>
          </Grid>
        </Flex>
      </Grid.Col>
      <Grid.Col span={4}>
        <Box bg="white" p="20px" className="dashboard-block">
          <Text color="#1F2A37" size="20px" fw="700" mb="20px">
            Khung năng lực
          </Text>
          <BlockEmployeeCapacity />
        </Box>
      </Grid.Col>
    </Grid>
  );
};
