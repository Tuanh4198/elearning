import React, { Fragment, useCallback, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Anchor, BookOpen, CaretDown, CaretUp, DotOutline, GearSix, GridFour, ListDashes, PencilCircle } from '@phosphor-icons/react';
import { AdminRoutes } from 'app/pages/admin/routes';
import { Button, Collapse, Flex, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { adminPathName } from 'app/config/constants';

interface NavigationItems {
  url: string;
  label: string;
  icon: React.JSX.Element | null;
  subMenu?: {
    url: string;
    label: string;
  }[];
}

const navigationItems: NavigationItems[] = [
  {
    url: `${adminPathName}${AdminRoutes.DASHBOARD}`,
    label: 'Dashboard',
    icon: <GridFour size="16px" />,
  },
  {
    url: '#',
    label: 'Danh sách',
    icon: null,
  },
  {
    url: '#',
    label: 'Khóa học',
    icon: <BookOpen size="16px" />,
    subMenu: [
      {
        url: `${adminPathName}${AdminRoutes.COURSE}`,
        label: 'Danh sách khóa học',
      },
      {
        url: `${adminPathName}${AdminRoutes.COURSE_CATEGORY}`,
        label: 'Danh mục khóa học',
      },
    ],
  },
  {
    url: '#',
    label: 'Kì thi',
    icon: <PencilCircle size="16px" />,
    subMenu: [
      {
        url: `${adminPathName}${AdminRoutes.EXAM}`,
        label: 'Danh sách kì thi',
      },
      {
        url: `${adminPathName}${AdminRoutes.EXAM_CATEGORY}`,
        label: 'Danh mục kì thi',
      },
    ],
  },
  {
    url: '#',
    label: 'Thiết lập',
    icon: null,
  },
  {
    url: `${adminPathName}${AdminRoutes.NAVIGATOR}`,
    label: 'Đại Hải Trình',
    icon: <Anchor size="16px" />,
  },
  {
    url: '#',
    label: 'Thư viện câu hỏi',
    icon: <ListDashes size="16px" />,
    subMenu: [
      {
        url: `${adminPathName}${AdminRoutes.QUIZZ}`,
        label: 'Danh sách câu hỏi',
      },
      {
        url: `${adminPathName}${AdminRoutes.QUIZZ_CATEGORY}`,
        label: 'Danh mục câu hỏi',
      },
    ],
  },
  {
    url: `${adminPathName}${AdminRoutes.SETTING}`,
    label: 'Cài đặt',
    icon: <GearSix size="16px" />,
  },
];

const MainNavigation = () => {
  return (
    <Flex direction="column" gap="12px" w="100%">
      {navigationItems?.map(item => (
        <ItemNavigation key={JSON.stringify(item)} item={item} />
      ))}
    </Flex>
  );
};

const ItemNavigation = ({ item }: { item: NavigationItems }) => {
  const location = useLocation();
  const navigate = useNavigate();

  const [opened, { toggle, open }] = useDisclosure(false);

  const handleComparePath = useCallback(
    (path: string) => {
      let comparePath = '';
      const adminDashboard = `${adminPathName}${AdminRoutes.DASHBOARD}`;
      if (location.pathname === adminDashboard) {
        comparePath = adminDashboard;
      } else {
        Object.values(AdminRoutes).forEach(i => {
          const adminPageRoute = `${adminPathName}${i}`;
          if (adminPageRoute !== adminDashboard) {
            if (location.pathname.startsWith(adminPageRoute)) comparePath = adminPageRoute;
          }
        });
      }
      return comparePath === path;
    },
    [location]
  );

  const handleCheckChildActive = () => {
    return item.subMenu?.some(i => i.url === location.pathname);
  };

  useEffect(() => {
    if (handleCheckChildActive()) {
      open();
    }
  }, []);

  if (item.icon == null) {
    return (
      <Text c="#1F2A37" fw={600} fz={16}>
        {item.label}
      </Text>
    );
  }

  return (
    <Fragment>
      <Button
        justify="flex-start"
        variant="subtle"
        radius="8px"
        size="md"
        p="12px"
        color={handleComparePath(item.url) || handleCheckChildActive() ? '#2A2A86' : '#1F2A37'}
        bg={handleComparePath(item.url) || handleCheckChildActive() ? '#F9F9FF' : undefined}
        key={JSON.stringify(item)}
        fullWidth
        leftSection={item.icon}
        rightSection={item.subMenu && (opened ? <CaretUp size="16px" /> : <CaretDown size="16px" />)}
        styles={{ label: { flex: 1 } }}
        onClick={() => {
          if (item.subMenu) {
            toggle();
          } else {
            navigate(item.url);
          }
        }}
      >
        <Text fw={handleComparePath(item.url) || handleCheckChildActive() ? '600' : 'normal'}>{item.label}</Text>
      </Button>
      {item.subMenu && (
        <Collapse in={opened}>
          <Flex direction="column" gap="12px" w="100%" pl="8px">
            {item.subMenu.map(subItem => (
              <Button
                key={JSON.stringify(subItem)}
                justify="flex-start"
                variant="subtle"
                radius="8px"
                p="12px"
                color={handleComparePath(subItem.url) ? '#2A2A86' : '#1F2A37'}
                bg={handleComparePath(subItem.url) ? '#F9F9FF' : undefined}
                fullWidth
                leftSection={<DotOutline size="16px" style={{ opacity: handleComparePath(subItem.url) ? 1 : 0 }} />}
                size="md"
                onClick={() => {
                  navigate(subItem.url);
                }}
              >
                <Text fw={handleComparePath(subItem.url) ? '600' : 'normal'}>{subItem.label}</Text>
              </Button>
            ))}
          </Flex>
        </Collapse>
      )}
    </Fragment>
  );
};

export default MainNavigation;
