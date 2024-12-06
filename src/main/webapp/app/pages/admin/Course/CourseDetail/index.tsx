import React, { Fragment, useCallback, useMemo, useState } from 'react';
import './styles.scss';
import { Breadcrumbs, Container, Space, Tabs, Text } from '@mantine/core';
import { AdminRoutes } from 'app/pages/admin/routes';
import { adminPathName } from 'app/config/constants';
import { Link } from 'react-router-dom';
import { SectionInfoTab } from 'app/pages/admin/Course/CourseDetail/components/SectionInfoTab';
import { SectionStatisticTab } from 'app/pages/admin/Course/CourseDetail/components/SectionStatisticTab';

enum Tab {
  InfoTab = 'InfoTab',
  StatisticTab = 'StatisticTab',
}

export const CourseDetail = () => {
  const [activeTab, setActiveTab] = useState<Tab | null>(Tab.InfoTab);

  const breadcrumbItems = useMemo(
    () => [
      { title: 'Khóa học', href: `${adminPathName}${AdminRoutes.COURSE}` },
      { title: 'Chi tiết khóa học', href: '#' },
    ],
    []
  );

  const tabLableColor = useCallback(
    (tab: Tab) => {
      return activeTab === tab ? '#2A2A86' : '#111928';
    },
    [activeTab]
  );

  return (
    <Fragment>
      <Text fz={24} fw={700} c="#1F2A37" mb={12}>
        Khoá học
      </Text>
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
      <Container
        style={{
          borderRadius: '8px',
        }}
        p={20}
        pt={8}
        mt={16}
        fluid
        size="responsive"
        bg="#fff"
      >
        <Tabs value={activeTab} onChange={value => setActiveTab(value as Tab)}>
          <Tabs.List mb={20}>
            <Tabs.Tab p="12px" value={Tab.InfoTab}>
              <Text c={tabLableColor(Tab.InfoTab)} fz="12px">
                Thông tin khoá học
              </Text>
            </Tabs.Tab>
            <Tabs.Tab p="12px" value={Tab.StatisticTab}>
              <Text c={tabLableColor(Tab.StatisticTab)} fz="12px">
                Thống kê
              </Text>
            </Tabs.Tab>
          </Tabs.List>
          <Tabs.Panel value={Tab.InfoTab}>
            <SectionInfoTab />
          </Tabs.Panel>
          <Tabs.Panel value={Tab.StatisticTab}>
            <SectionStatisticTab />
          </Tabs.Panel>
        </Tabs>
      </Container>
      <Space h="60" />
    </Fragment>
  );
};
