import React from 'react';
import { Box, Flex, Text, Image, Loader } from '@mantine/core';
import { useFetchGeneralReport } from 'app/pages/user/Home/hooks/useFetchGeneralReport';

export const BlockReport = () => {
  const { data: report, isLoading } = useFetchGeneralReport();

  return (
    <Flex align="center" gap="20px" w="100%">
      <Box flex={1} pos="relative" className="dashboard-block with-bg">
        <Image src={'../../../../content/images/bg-total-course.svg'} alt="Norway" height={'120px'} />
        <Box pos="absolute" left="12px" top="16px" bottom="16px">
          <Flex direction="column" justify="space-around" h="100%">
            <Text color="#FFFFFF" size="16px">
              Số khoá học hoàn thành
            </Text>
            {isLoading ? (
              <Loader size={24} />
            ) : (
              <Text color="#FFFFFF" size="24px" fw="700">
                {`${report?.totalCompletedCourse || 0}` + '/' + `${report?.totalCourse || 0}`} <br />
              </Text>
            )}
            <Text color="#FFFFFF" size="16px">
              Khóa học
            </Text>
          </Flex>
        </Box>
      </Box>
      <Box flex={1} pos="relative" className="dashboard-block with-bg">
        <Image src={'../../../../content/images/bg-total-hours.svg'} alt="Norway" height={'120px'} />
        <Box pos="absolute" left="12px" top="16px" bottom="16px">
          <Flex direction="column" justify="space-around" h="100%">
            <Text color="#FFFFFF" size="16px">
              Số kì thi hoàn thành
            </Text>
            {isLoading ? (
              <Loader size={24} />
            ) : (
              <Text color="#FFFFFF" size="24px" fw="700">
                {`${report?.totalCompletedExam || 0}` + '/' + `${report?.totalExam || 0}`} <br />
              </Text>
            )}
            <Text color="#FFFFFF" size="16px">
              Kỳ thi
            </Text>
          </Flex>
        </Box>
      </Box>
    </Flex>
  );
};
