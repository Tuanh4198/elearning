import React, { Fragment } from 'react';
import { Box, Button, Container, Flex, Loader, LoadingOverlay, Pagination, Space, Text } from '@mantine/core';
import { CaretDoubleLeft, CaretDoubleRight, Plus } from '@phosphor-icons/react';
import { SectionFilterGroup } from 'app/pages/admin/Exam/ExamList/components/SectionFilterGroup';
import { SectionTableExam } from 'app/pages/admin/Exam/ExamList/components/SectionTableExam';
import { useFetchExamList } from 'app/pages/admin/Exam/ExamList/hooks/useFetchExamList';
import { useFilterConditionExam } from 'app/pages/admin/Exam/ExamList/hooks/useFilterConditionExam';
import { adminPathName } from 'app/config/constants';
import { AdminRoutes } from 'app/pages/admin/routes';

export const ExamList = () => {
  const { conditions } = useFilterConditionExam();

  const {
    isLoading: isLoadingExams,
    data: exams,
    size: sizeExams,
    page: pageExams,
    total: totalExams,
    handleNextPage: handleNextPageExamList,
  } = useFetchExamList({
    conditions,
  });

  return (
    <Fragment>
      <Box pos="relative">
        <LoadingOverlay style={{ borderRadius: '8px' }} zIndex={10} visible={isLoadingExams} loaderProps={{ children: <Loader /> }} />
        <Flex align="center" justify="space-between">
          <Text fz={24} fw={700} c="#1F2A37">
            Kì thi
          </Text>
          <Button component="a" href={`${adminPathName}${AdminRoutes.EXAM}/add`} w={180} h={32} rightSection={<Plus />}>
            Thêm mới kì thi
          </Button>
        </Flex>
        <Container style={{ borderRadius: '8px' }} p={20} mt={16} fluid size="responsive" bg="#fff">
          <SectionFilterGroup />
          <SectionTableExam exams={exams} />
          {Math.ceil(totalExams / sizeExams) > 1 && (
            <Flex justify="flex-end">
              <Pagination
                value={pageExams}
                disabled={isLoadingExams}
                hideWithOnePage
                size="sm"
                mt={10}
                radius="50%"
                nextIcon={CaretDoubleRight}
                previousIcon={CaretDoubleLeft}
                total={Math.ceil(totalExams / sizeExams)}
                onChange={handleNextPageExamList}
              />
            </Flex>
          )}
        </Container>
      </Box>
      <Space h="60" />
    </Fragment>
  );
};
