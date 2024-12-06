import React, { Fragment, useMemo } from 'react';
import { Breadcrumbs, Button, Flex, Text } from '@mantine/core';
import { adminPathName } from 'app/config/constants';
import { AdminRoutes } from 'app/pages/admin/routes';
import { Link } from 'react-router-dom';

export const BreadcrumbsCreate = () => {
  const breadcrumbItems = useMemo(
    () => [
      { title: 'Khóa học', href: `${adminPathName}${AdminRoutes.COURSE}` },
      { title: 'Tạo mới khóa học', href: '#' },
    ],
    []
  );

  return (
    <Fragment>
      <Flex align="center" justify="space-between" mb={12}>
        <Text c="#1F2A37" fz={24} fw={700}>
          Tạo mới khóa học
        </Text>
        <Flex gap={20}>
          <Button variant="outline" color="#2A2A86" size="md" radius={4}>
            Hủy
          </Button>
          <Button variant="filled" color="#2A2A86" size="md" radius={4}>
            Tạo khoá học
          </Button>
          <Button variant="filled" color="#2A2A86" size="md" radius={4}>
            Tạo & Giao khoá học
          </Button>
        </Flex>
      </Flex>
      <Breadcrumbs
        mb={16}
        separatorMargin="5px"
        separator={
          <Text size="12px" color="#E5E7EB">
            /
          </Text>
        }
      >
        {breadcrumbItems.map((item, index) => (
          <Link to={item.href} key={index}>
            <Text fz="12px" color={breadcrumbItems.length - 1 === index ? '#2A2A86' : '#4B5563'}>
              {item.title}
            </Text>
          </Link>
        ))}
      </Breadcrumbs>
    </Fragment>
  );
};
