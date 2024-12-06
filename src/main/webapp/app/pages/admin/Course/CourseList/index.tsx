import {
  Button,
  Container,
  Flex,
  Pagination,
  Select,
  Space,
  Table,
  Text,
  TextInput,
  Image,
  Box,
  LoadingOverlay,
  Loader,
} from '@mantine/core';
import { CalendarBlank, CaretDoubleLeft, CaretDoubleRight, MagnifyingGlass, Plus } from '@phosphor-icons/react';
import React, { Fragment } from 'react';
import { DateInput } from '@mantine/dates';
import { useNavigate } from 'react-router-dom';
import { AdminRoutes } from 'app/pages/admin/routes';
import { adminPathName } from 'app/config/constants';
import { useFetchCourseList } from 'app/pages/user/Course/CourseList/hooks/useFetchCourseList';
import CourseDeadline from 'app/pages/admin/Course/CourseList/components/CourseStatus';
import dayjs from 'dayjs';

export const CourseList = () => {
  const navigate = useNavigate();

  const {
    isLoading: isLoadingCoursesEmployees,
    data: courseEmployees,
    page: pageCoursesEmployees,
    size: sizeCoursesEmployees,
    total: totalCoursesEmployees,
    handleNextPage: handleNextPageCourseList,
  } = useFetchCourseList({
    withCategory: true,
  });

  const rows = courseEmployees?.map((element, id) => (
    <Table.Tr key={id} onClick={() => navigate(`${adminPathName}${AdminRoutes.COURSE}/${id}`)}>
      <Table.Td
        style={{
          display: 'flex',
          flex: 'wrap',
          alignItems: 'center',
        }}
        p={8}
      >
        <Image
          w="68px"
          h="44px"
          flex="none"
          src={element.course?.thumbUrl}
          fallbackSrc="../../../../../../content/images/defaultBanner.png"
          fit="cover"
          radius="8px"
          mr={8}
        />
        <Text fz={14} fw={600} fs="normal" c="#1F2A37">
          {element?.course?.title}
        </Text>
      </Table.Td>
      <Table.Td>
        <CourseDeadline
          end={element.course?.expireTime ? dayjs(element.course.expireTime).toDate() : undefined}
          start={element.course?.applyTime ? dayjs(element.course.applyTime).toDate() : undefined}
        />
      </Table.Td>
      <Table.Td>{element.course?.category?.title}</Table.Td>
    </Table.Tr>
  ));

  return (
    <Fragment>
      <Box pos="relative">
        <LoadingOverlay
          style={{ borderRadius: '8px' }}
          zIndex={10}
          visible={isLoadingCoursesEmployees}
          loaderProps={{ children: <Loader /> }}
        />
        <Flex align="center" justify="space-between">
          <Text fz={24} fw={700} c="#1F2A37">
            Khoá học
          </Text>
          <Button onClick={() => navigate(`${adminPathName}${AdminRoutes.COURSE}/create`)}>
            <Text>Thêm mới khoá học</Text>
            <Space w={8} />
            <Plus size={12} />
          </Button>
        </Flex>
        <Container style={{ borderRadius: '8px' }} p={20} mt={16} fluid size="responsive" bg="#fff">
          <Flex gap={12} justify="space-between" mb={20}>
            <TextInput
              flex={7}
              style={{
                borderRadius: '4px',
              }}
              placeholder="Tìm kiếm khoá học"
              leftSectionPointerEvents="none"
              leftSection={<MagnifyingGlass />}
            />
            <Select flex={3} placeholder="Danh mục" data={['React', 'Angular', 'Vue', 'Svelte']} />
            <Select flex={3} placeholder="Trạng thái" data={['React', 'Angular', 'Vue', 'Svelte']} />
            <DateInput rightSection={<CalendarBlank />} flex={4} placeholder="Chọn thời gian" />
          </Flex>
          <Table highlightOnHover borderColor="#E5E7EB">
            <Table.Thead bg="#F9FAFB">
              <Table.Tr>
                <Table.Th fw={400} fz={14}>
                  Tên khoá học
                </Table.Th>
                <Table.Th fw={400} fz={14}>
                  Thời gian
                </Table.Th>
                <Table.Th fw={400} fz={14}>
                  Danh mục
                </Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>{rows}</Table.Tbody>
          </Table>
          {Math.ceil(totalCoursesEmployees / sizeCoursesEmployees) > 1 && (
            <Pagination
              value={pageCoursesEmployees}
              disabled={isLoadingCoursesEmployees}
              hideWithOnePage
              size="sm"
              mt={10}
              radius="50%"
              nextIcon={CaretDoubleRight}
              previousIcon={CaretDoubleLeft}
              total={Math.ceil(totalCoursesEmployees / sizeCoursesEmployees)}
              onChange={handleNextPageCourseList}
            />
          )}
        </Container>
      </Box>
      <Space h="60" />
    </Fragment>
  );
};
