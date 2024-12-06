import React from 'react';
import { Box, Container, Flex, Grid, Loader, LoadingOverlay, Pagination, Text } from '@mantine/core';
import { CourseCard } from 'app/pages/user/Course/CourseList/components/CourseCard';
import { useFetchCourseList } from 'app/pages/user/Course/CourseList/hooks/useFetchCourseList';
import { Empty } from 'app/shared/components/Empty';

export const SectionCourseInProgress = () => {
  const {
    isLoading: isLoadingCoursesEmployees,
    data: courseEmployees,
    size: sizeCoursesEmployees,
    page: pageCoursesEmployees,
    total: totalCoursesEmployees,
    handleNextPage: handleNextPageCourseList,
  } = useFetchCourseList({
    withStatus: true,
    withCategory: false,
  });

  return (
    <>
      <Text color="black" size="20px" fw="700" mb="20px">
        Khóa học đang diễn ra
      </Text>
      <Box pos="relative" bg="white" p="20px" className="course-in-progress-box">
        <LoadingOverlay
          style={{ borderRadius: '8px' }}
          zIndex={10}
          visible={isLoadingCoursesEmployees && courseEmployees && courseEmployees?.length > 0}
          loaderProps={{ children: <Loader /> }}
        />
        <Grid>
          {isLoadingCoursesEmployees && (!courseEmployees || courseEmployees?.length <= 0) ? (
            Array.from({ length: 4 }).map((_, i) => (
              <Grid.Col key={i} span={3}>
                <CourseCard loading />
              </Grid.Col>
            ))
          ) : !courseEmployees || courseEmployees?.length <= 0 ? (
            <Container py={30}>
              <Empty description="Không có dữ liệu khóa học" />
            </Container>
          ) : (
            courseEmployees?.map(item => (
              <Grid.Col key={item.id} span={3}>
                <CourseCard course={item.course} />
              </Grid.Col>
            ))
          )}
        </Grid>
        {Math.ceil(totalCoursesEmployees / sizeCoursesEmployees) > 1 && (
          <Flex justify="flex-end" mt="20px">
            <Pagination
              value={pageCoursesEmployees}
              disabled={isLoadingCoursesEmployees}
              withControls={false}
              hideWithOnePage
              size="sm"
              total={Math.ceil(totalCoursesEmployees / sizeCoursesEmployees)}
              onChange={handleNextPageCourseList}
            />
          </Flex>
        )}
      </Box>
    </>
  );
};
