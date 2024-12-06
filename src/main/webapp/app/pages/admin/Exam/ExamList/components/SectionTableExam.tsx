import React, { useMemo } from 'react';
import { Container, Flex, Table, Text } from '@mantine/core';
import { adminPathName } from 'app/config/constants';
import { Link, useNavigate } from 'react-router-dom';
import { IExam } from 'app/shared/model/exam.model';
import { AdminRoutes } from 'app/pages/admin/routes';
import CourseDeadline from 'app/pages/admin/Course/CourseList/components/CourseStatus';
import { Empty } from 'app/shared/components/Empty';

export const SectionTableExam = ({ exams }: { exams?: IExam[] }) => {
  const navigate = useNavigate();

  const rows = useMemo(
    () =>
      exams?.map(element => (
        <Table.Tr key={element?.id} onClick={() => navigate(`${adminPathName}${AdminRoutes.EXAM}/${element?.id}`)}>
          <Table.Td>
            <Flex flex="wrap" align="center">
              {/*<Container*/}
              {/*  style={{*/}
              {/*    width: 68,*/}
              {/*    height: 44,*/}
              {/*    borderRadius: 8,*/}
              {/*    marginInline: 'unset',*/}
              {/*    marginRight: '8px',*/}
              {/*    background: `url('${element.thumbUrl}')`,*/}
              {/*  }}*/}
              {/*/>*/}
              <Link to={`${AdminRoutes.EXAM}/${element.id}`}>
                <Text fz={14} fw={600} fs="normal" c="#1F2A37">
                  {element.title}
                </Text>
              </Link>
            </Flex>
          </Table.Td>
          {/*<Table.Td>*/}
          {/*  <Flex*/}
          {/*    style={{*/}
          {/*      alignContent: 'space-between',*/}
          {/*      alignItems: 'center',*/}
          {/*      justifyContent: 'space-between',*/}
          {/*    }}*/}
          {/*  >*/}
          {/*    <Text fz={12} fw={500}>*/}
          {/*      {0}/{0}*/}
          {/*    </Text>*/}
          {/*    <ProgressStatus current={0} max={0} />*/}
          {/*  </Flex>*/}
          {/*</Table.Td>*/}
          <Table.Td>
            <CourseDeadline
              end={element.expireTime ? new Date(element.expireTime) : null}
              start={element.applyTime ? new Date(element.applyTime) : null}
            />
          </Table.Td>
        </Table.Tr>
      )),
    [exams]
  );

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
          <Table.Th fw={400} fz={14}>
            Tên Kì thi
          </Table.Th>
          <Table.Th fw={400} fz={14} w={150}>
            Tiến độ
          </Table.Th>
          <Table.Th fw={400} fz={14}>
            Thời gian
          </Table.Th>
        </Table.Tr>
      </Table.Thead>
      <Table.Tbody>{rows}</Table.Tbody>
    </Table>
  );
};
