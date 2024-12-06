import React from 'react';
import { Box, Button, Container, Flex, Loader, Pill, Skeleton, Text, Tooltip } from '@mantine/core';
import { Link } from 'react-router-dom';
import { UserRoutes } from 'app/pages/user/routes';
import { useFetchCategories } from 'app/pages/user/Home/hooks/useFetchCategories';
import { Empty } from 'app/shared/components/Empty';
import { CategoryTypeEnum } from 'app/shared/model/category.model';

export const BlockCourseCategories = () => {
  const {
    isLoading: isLoadingCategories,
    data: categories,
    total: totalCategories,
    handleNextPage: handleNextPageCategories,
  } = useFetchCategories({ type: CategoryTypeEnum.COURSE });

  return (
    <Box flex={1} bg="white" p="20px" className="dashboard-block">
      <Flex align="center" justify="space-between" mb="20px">
        <Text color="#1F2A37" size="20px" fw="700">
          Thư viện khoá học
        </Text>
        <Link to={UserRoutes.COURSE} target="_blank">
          <Text color="#6B7280" size="14px" fw={600}>
            Xem tất cả
          </Text>
        </Link>
      </Flex>
      <Flex align="center" justify="flex-start" gap="10px" wrap="wrap" w="100%">
        {isLoadingCategories && (!categories || categories?.length <= 0) ? (
          Array.from({ length: 4 }).map((_, i) => <Skeleton key={i} height={24.5} width={100} radius={16} />)
        ) : categories && categories.length > 0 ? (
          categories.map(cate => (
            <Box key={cate.id} maw="100%">
              <Tooltip label={cate.title} multiline maw="500px">
                <Link to={`${UserRoutes.COURSE}?categoryId=${cate.id}`} target="_blank">
                  <Text size="16px" fw="500" maw="100%">
                    <Pill size="xl" bg="#D4D4F0" c="#2A2A86" maw="100%">
                      {cate.title}
                    </Pill>
                  </Text>
                </Link>
              </Tooltip>
            </Box>
          ))
        ) : (
          <Container py={15}>
            <Empty description="Không có dữ liệu" />
          </Container>
        )}
        {isLoadingCategories && categories && categories?.length > 0 ? (
          <Loader size={16} />
        ) : (
          Math.ceil(totalCategories - (categories?.length || 0)) > 0 && (
            <Button color="#2A2A86" variant="filled" radius={16} size="compact-md" onClick={handleNextPageCategories}>
              + {totalCategories - (categories?.length || 0)}
            </Button>
          )
        )}
      </Flex>
    </Box>
  );
};
