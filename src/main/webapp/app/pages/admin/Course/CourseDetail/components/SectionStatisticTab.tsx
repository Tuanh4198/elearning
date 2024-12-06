import { Image, Text, Button, Container, Flex, Pagination, Select, Table, TextInput, Box, Checkbox, Space } from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { CalendarBlank, CaretDoubleLeft, CaretDoubleRight, Export, MagnifyingGlass, Plus, Trash } from '@phosphor-icons/react';
import { adminPathName } from 'app/config/constants';
import { AdminRoutes } from 'app/pages/admin/routes';
import { courseEmployees } from 'app/shared/model/user.model';
import React, { useState } from 'react';

export const SectionStatisticTab = () => {
  const [selectedRows, setSelectedRows] = useState<number[]>([]);

  const employeeIds = courseEmployees.map((_, id) => id);

  const rows = courseEmployees.map((element, index) => {
    return (
      <Table.Tr key={index} bg={selectedRows.includes(index) ? 'var(--mantine-color-blue-light)' : undefined}>
        <Table.Td>
          <Checkbox
            aria-label="Select row"
            checked={selectedRows.includes(index)}
            onChange={event =>
              setSelectedRows(event.currentTarget.checked ? [...selectedRows, index] : selectedRows.filter(id => id !== index))
            }
          />
        </Table.Td>
        <Table.Td>{element.id}</Table.Td>
        <Table.Td>
          {element.lastName} {element.firstName}
        </Table.Td>
        <Table.Td>{element.positionName}</Table.Td>
        <Table.Td w={146}>
          <Text
            fz={12}
            fw={500}
            style={{
              textAlign: 'center',
              justifySelf: 'center',
              borderRadius: 24,
              padding: '2px 8px',
              margin: 'unset',
              marginInline: 'unset',
              color: `${element.activated ? '#099268' : '#6B7280'}`,
              backgroundColor: `${element.activated ? '#C3FAE8' : '#E5E7EB'}`,
            }}
          >
            {element.activated ? 'Đã tham gia' : 'Chưa tham gia'}
          </Text>
        </Table.Td>
        <Table.Td align="center">
          <Trash
            style={{
              width: '20px',
              height: '20px',
            }}
          />
        </Table.Td>
      </Table.Tr>
    );
  });

  return (
    <Flex direction="column">
      <Box pos="relative" h={72} w={360} mb={20}>
        <Image
          style={{
            borderRadius: 16,
          }}
          src={'../../../../content/images/bg-total-course-employee.svg'}
          alt="CourseEmp"
        />
        <Box pos="absolute" left="12px" right="12px" top="20px" bottom="20px">
          <Flex align="center" c="#FFF" justify="space-between">
            <Text fz={12} lh={12 / 14}>
              Tổng số người tham gia
            </Text>
            <Flex direction="column">
              <Flex gap={3} align="center">
                <Text fz={18} fw={600} lh={18 / 20}>
                  1000/1023
                </Text>
                <Text c="#12B886" fz={14} fw={600}>
                  98%
                </Text>
              </Flex>
              <Text fz={12} lh={12 / 14}>
                Nhân sự
              </Text>
            </Flex>
          </Flex>
        </Box>
      </Box>

      <Flex gap={12} justify="space-between" mb={20}>
        <TextInput
          maw={390}
          flex={7}
          style={{
            borderRadius: '4px',
          }}
          placeholder="Tìm kiếm nhân sự"
          leftSectionPointerEvents="none"
          leftSection={<MagnifyingGlass />}
        />
        <Select mr="auto" maw={216} flex={3} placeholder="Trạng thái" data={['React', 'Angular', 'Vue', 'Svelte']} />
        <Button rightSection={<Export size={12} />} variant="outline" lh={12 / 14} fz={12} fs="normal" fw={400}>
          Xuất Excel
        </Button>
        <Button rightSection={<Plus size={12} />} lh={12 / 14} fz={12} fs="normal" fw={400}>
          Thêm nhân sự
        </Button>
      </Flex>
      <Table highlightOnHover borderColor="#E5E7EB">
        <Table.Thead bg="#F9FAFB">
          <Table.Tr>
            <Table.Th>
              <Checkbox
                aria-label="Select rows"
                checked={selectedRows.every(item => employeeIds.includes(item)) && selectedRows.length > 0}
                indeterminate={selectedRows.length !== employeeIds.length && selectedRows.length > 0}
                onClick={event => {
                  setSelectedRows(_ => (event.currentTarget.checked ? employeeIds : []));
                }}
              />
            </Table.Th>
            <Table.Th fw={400} fz={14}>
              YD
            </Table.Th>
            <Table.Th fw={400} fz={14}>
              Tên nhân sự
            </Table.Th>
            <Table.Th fw={400} fz={14}>
              Vị trí
            </Table.Th>
            <Table.Th fw={400} fz={14}>
              Trạng thái
            </Table.Th>
            <Table.Th w={80} fw={400} fz={14}>
              Thao tác
            </Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
      <Pagination
        size="sm"
        style={{
          marginTop: '10px',
          display: 'flex',
          justifyContent: 'end',
        }}
        styles={{
          control: {
            borderRadius: '50%',
          },
        }}
        nextIcon={CaretDoubleRight}
        previousIcon={CaretDoubleLeft}
        total={10}
      />
    </Flex>
  );
};
