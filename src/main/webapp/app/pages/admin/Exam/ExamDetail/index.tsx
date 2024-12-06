import React, { Fragment, useMemo, useState } from 'react';
import { IFilterConditionExam } from 'app/pages/admin/Exam/ExamList/hooks/useFilterConditionExam';
import { Container, Flex, Pagination, Space, Table, Text, TextInput } from '@mantine/core';
import { CaretDoubleLeft, CaretDoubleRight, MagnifyingGlass } from '@phosphor-icons/react';
import { Empty } from 'app/shared/components/Empty';
import { useParams } from 'react-router-dom';
import { useDebouncedValue } from '@mantine/hooks';
import { useFetchExamEmployeeList } from 'app/pages/admin/Exam/ExamDetail/hooks/useFetchExamEmployeeList';

const convertStatus = (status?: string) => {
  if (status === 'NOT_ATTENDED') {
    return 'Chưa tham gia';
  }
  if (status === 'PASS') {
    return 'Đạt';
  }
  if (status === 'NOT_PASS') {
    return 'Chưa đạt';
  }
};

export const ExamDetail = () => {
  const { id } = useParams<{ id: string }>();
  const [conditions, setConditions] = useState<IFilterConditionExam>({
    rootId: id,
  });
  const {
    isLoading: isLoadingExams,
    data: exams,
    size: sizeExams,
    total: totalExams,
    handleNextPage: handleNextPageExamList,
  } = useFetchExamEmployeeList({
    conditions,
  });

  const [searchValue, setSearchValue] = useState<string>('');
  const [debouncedSearchValue] = useDebouncedValue(searchValue, 300);
  React.useEffect(() => {
    setConditions(prevConditions => ({
      ...prevConditions,
      search: debouncedSearchValue,
    }));
  }, [debouncedSearchValue]);

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.currentTarget.value);
  };

  const rows = useMemo(() => {
    if (!exams) return [];
    return exams?.map(element => (
      <Table.Tr key={element?.id}>
        <Table.Td>
          <Text fz={14} fw={600} fs="normal" c="#1F2A37">
            {element.code}
          </Text>
        </Table.Td>
        <Table.Td>
          <Text fz={14} fw={600} fs="normal" c="#1F2A37">
            {convertStatus(element.status)}
          </Text>
        </Table.Td>
      </Table.Tr>
    ));
  }, [exams]);

  const renderTable = useMemo(() => {
    if (!exams || exams?.length <= 0) {
      return (
        <Container py={30}>
          <Empty description="Không có dữ liệu kì thi" />
        </Container>
      );
    }
    return (
      <Table highlightOnHover borderColor="#E5E7EB">
        <Table.Thead bg="#F9FAFB">
          <Table.Tr>
            <Table.Th fw={400} fz={14} w={150}>
              Mã nhân sự
            </Table.Th>
            <Table.Th fw={400} fz={14}>
              Trạng thái
            </Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
    );
  }, [exams]);

  return (
    <Fragment>
      <Flex align="center" justify="space-between">
        <Text fz={24} fw={700} c="#1F2A37">
          Danh sách kì thi theo nhân sự
        </Text>
      </Flex>
      <Container style={{ borderRadius: '8px' }} p={20} mt={16} fluid size="responsive" bg="#fff">
        <Flex gap={12} justify="space-between" mb={20}>
          <TextInput
            flex={7}
            radius={4}
            placeholder="Tìm YD"
            leftSectionPointerEvents="none"
            leftSection={<MagnifyingGlass />}
            onChange={handleSearchChange}
          />
        </Flex>
        {renderTable}
        {Math.ceil(totalExams / sizeExams) > 1 && (
          <Flex justify="flex-end">
            <Pagination
              disabled={isLoadingExams}
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
      <Space h="60" />
    </Fragment>
  );
};
