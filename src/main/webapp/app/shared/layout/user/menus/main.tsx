import React, { useMemo } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { BookOpen, House, PencilCircle } from '@phosphor-icons/react';
import { Button, Flex, Text } from '@mantine/core';
import { UserRoutes } from 'app/pages/user/routes';

const navigationItems = [
  {
    url: UserRoutes.HOME,
    label: 'Trang chủ',
    icon: <House size="16px" />,
  },
  {
    url: UserRoutes.COURSE,
    label: 'Khóa học',
    icon: <BookOpen size="16px" />,
  },
  {
    url: UserRoutes.EXAM,
    label: 'Kì thi',
    icon: <PencilCircle size="16px" />,
  },
];

const MainNavigation = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const comparePath = useMemo(() => {
    if (location.pathname === UserRoutes.HOME) {
      return UserRoutes.HOME;
    } else {
      let highlightPath: string | undefined = undefined;
      Object.values(UserRoutes).forEach(item => {
        if (item !== UserRoutes.HOME) {
          if (location.pathname.startsWith(item)) highlightPath = item;
        }
      });
      return highlightPath;
    }
  }, [location]);

  return (
    <Flex direction="column" gap="12px" w="100%">
      {navigationItems?.map(item => (
        <Button
          justify="flex-start"
          variant="subtle"
          radius="8px"
          px="12px"
          py="10px"
          color={comparePath === item.url ? '#2A2A86' : '#1F2A37'}
          bg={comparePath === item.url ? '#F9F9FF' : undefined}
          key={item.url}
          fullWidth
          leftSection={item.icon}
          size="md"
          onClick={() => navigate(item.url)}
        >
          <Text fw={comparePath === item.url ? '600' : 'normal'}>{item.label}</Text>
        </Button>
      ))}
    </Flex>
  );
};

export default MainNavigation;
