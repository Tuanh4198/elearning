import React from 'react';
import { Box, Button, Chip, Container, Flex, Grid, Loader, LoadingOverlay, Pagination, Skeleton, Text, Tooltip } from '@mantine/core';
import { ExamCard } from 'app/pages/user/Exam/ExamList/components/ExamCard';
import { useFetchCategories } from 'app/pages/user/Exam/ExamList/hooks/useFetchCategories';
import { Empty } from 'app/shared/components/Empty';
import { CategoryTypeEnum } from 'app/shared/model/category.model';
import { useFetchExamEmployees } from 'app/pages/user/Exam/ExamList/hooks/useFetchExamEmployees';

export const SectionExamBank = () => {
  const {
    isLoading: isLoadingCategories,
    data: categories,
    total: totalCategories,
    handleNextPage: handleNextPageCategories,
  } = useFetchCategories({ type: CategoryTypeEnum.EXAM });

  const {
    isLoading: isLoadingExams,
    data: exams,
    size: sizeExams,
    total: totalExams,
    defaultCategoryId,
    categoryId,
    handleNextPage: handleNextPageExamList,
    handleChangeCategoryId,
  } = useFetchExamEmployees({
    withCategory: true,
  });

  return (
    <Box className="exam-list-box">
      <Text color="black" size="20px" fw="700" my="20px">
        Ngân hàng kì thi
      </Text>
      <Flex align="center" justify="flex-start" gap="8px" mb="20px" wrap="wrap" w="100%">
        {isLoadingCategories && (!categories || categories?.length <= 0) ? (
          Array.from({ length: 4 }).map((_, i) => <Skeleton key={i} height={24.5} width={100} radius={16} />)
        ) : (
          <Chip.Group multiple={false} value={categoryId} onChange={handleChangeCategoryId}>
            <Chip
              value={defaultCategoryId}
              color="#2A2A86"
              defaultChecked
              bg="transparent"
              variant={categoryId === defaultCategoryId ? 'filled' : 'outline'}
            >
              <Text color={categoryId === defaultCategoryId ? 'white' : '#2A2A86'}>Tất cả</Text>
            </Chip>
            {categories?.map(cate => (
              <Chip
                key={cate.id}
                value={cate?.id}
                color="#2A2A86"
                defaultChecked
                bg="transparent"
                maw="100%"
                styles={{ label: { maxWidth: '100%' } }}
                variant={categoryId === cate?.id?.toString() ? 'filled' : 'outline'}
              >
                <Tooltip label={cate.title} multiline maw="500px">
                  <Text lineClamp={1} maw="100%" color={categoryId === cate?.id?.toString() ? 'white' : '#2A2A86'}>
                    {cate.title}
                  </Text>
                </Tooltip>
              </Chip>
            ))}
          </Chip.Group>
        )}
        {isLoadingCategories && categories && categories?.length > 0 ? (
          <Loader size={16} />
        ) : (
          Math.ceil(totalCategories - (categories?.length || 0)) > 0 && (
            <Button color="#2A2A86" variant="filled" radius={16} size="compact-sm" onClick={handleNextPageCategories}>
              + {totalCategories - (categories?.length || 0)}
            </Button>
          )
        )}
      </Flex>
      <Box pos="relative">
        <LoadingOverlay
          style={{ borderRadius: '8px' }}
          zIndex={10}
          visible={isLoadingExams && exams && exams?.length > 0}
          loaderProps={{ children: <Loader /> }}
        />
        <Grid>
          {isLoadingExams && (!exams || exams?.length <= 0) ? (
            Array.from({ length: 4 }).map((_, i) => (
              <Grid.Col key={i} span={3}>
                <ExamCard loading />
              </Grid.Col>
            ))
          ) : !exams || exams?.length <= 0 ? (
            <Container py={30}>
              <Empty description="Không có dữ liệu kì thi" />
            </Container>
          ) : (
            exams?.map(item => (
              <Grid.Col key={item.id} span={3}>
                <ExamCard exam={item.exam} />
              </Grid.Col>
            ))
          )}
        </Grid>
      </Box>
      {Math.ceil(totalExams / sizeExams) > 1 && (
        <Flex justify="flex-end" mt="20px">
          <Pagination
            disabled={isLoadingExams}
            withControls={false}
            hideWithOnePage
            size="sm"
            total={Math.ceil(totalExams / sizeExams)}
            onChange={handleNextPageExamList}
          />
        </Flex>
      )}
    </Box>
  );
};
