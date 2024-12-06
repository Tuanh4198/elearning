import React, { useEffect, useMemo } from 'react';
import { Helmet } from 'react-helmet';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import ErrorBoundary from 'app/shared/error/error-boundary';
import AppRoutes from 'app/routes';
import MainNavigation from './menus/main';
import { AppShell, ScrollArea, Text, Flex, Avatar, Box, LoadingOverlay, Button } from '@mantine/core';
import { useDisclosure, useHeadroom } from '@mantine/hooks';
import { generateAvatar } from 'app/shared/util';
import Header from 'app/shared/layout/user/header/header';
import { useNavigate } from 'react-router-dom';
import { adminPathName, AUTHORITIES, logoutUrl } from 'app/config/constants';
import { getLoginUrl } from 'app/shared/util/url-utils';
import { SignIn, SignOut, UserCircleGear } from '@phosphor-icons/react';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const UserAppFrame = ({ title }: { title: string }) => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);

  const account = useAppSelector(state => state.authentication.account);

  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

  const pinned = useHeadroom({ fixedAt: 120 });

  const [mobileNavigationActive, { toggle: toggleMobileNavigationActive }] = useDisclosure(false);

  const [desktopNavigationActive, { toggle: toggleDesktopNavigationActive }] = useDisclosure(true);

  const authButton = useMemo(
    () => (
      <Button
        justify="flex-start"
        variant="subtle"
        radius="8px"
        p="12px"
        color={'#1F2A37'}
        fullWidth
        leftSection={account && account.login ? <SignOut size="16px" /> : <SignIn size="16px" />}
        size="md"
        onClick={() => {
          if (account && account.login) {
            navigate(logoutUrl);
          } else {
            navigate(getLoginUrl());
          }
        }}
      >
        {account && account.login ? 'Đăng Xuất' : 'Đăng Nhập'}
      </Button>
    ),
    [account]
  );

  const adminButton = useMemo(() => {
    // const isAdmin = hasAnyAuthority(account.authorities, [AUTHORITIES.manage]);
    const isAdmin = true;
    return (
      isAdmin && (
        <Button
          justify="flex-start"
          variant="subtle"
          radius="8px"
          p="12px"
          color={'#1F2A37'}
          fullWidth
          leftSection={<UserCircleGear size="16px" />}
          size="md"
          onClick={() => navigate(adminPathName)}
        >
          Admin
        </Button>
      )
    );
  }, [account]);

  const navigationMarkup = useMemo(() => <MainNavigation />, []);

  const actualPageMarkup = useMemo(
    () => (
      <ErrorBoundary>
        <AppRoutes />
      </ErrorBoundary>
    ),
    []
  );

  const helmet = useMemo(
    () => (
      <Helmet>
        <title>{title}</title>
      </Helmet>
    ),
    [title]
  );

  const fullname = useMemo(() => `${account?.lastName} ${account?.firstName}`, [account?.lastName, account?.firstName]);

  return (
    <Box pos="relative">
      <LoadingOverlay visible={!isAuthenticated} zIndex={1000} overlayProps={{ radius: 'sm', blur: 2 }} />
      {helmet}
      <AppShell
        layout="alt"
        padding="20px"
        header={{ height: 60, collapsed: !pinned }}
        navbar={{
          width: 300,
          breakpoint: 'sm',
          collapsed: { mobile: !mobileNavigationActive, desktop: !desktopNavigationActive },
        }}
      >
        <AppShell.Header>
          <Header
            mobileNavigationActive={mobileNavigationActive}
            toggleMobileNavigationActive={toggleMobileNavigationActive}
            desktopNavigationActive={desktopNavigationActive}
            toggleDesktopNavigationActive={toggleDesktopNavigationActive}
          />
        </AppShell.Header>
        <AppShell.Navbar p="md">
          <Flex align="center" justify="center" w="100%" mb="40px">
            <img className="support-icon" src={'../../../../../content/images/logo.svg'} alt="empty" />
          </Flex>
          <Flex
            align="center"
            justify="center"
            direction="column"
            w="100%"
            mb="40px"
            p="20px"
            gap="20px"
            style={{
              border: '1px solid #E5E7EB',
              borderRadius: '16px',
            }}
          >
            <Avatar color="#2A2A86" size="80px" radius="100%">
              {account && account.login ? generateAvatar(fullname) : 'U'}
            </Avatar>
            <Flex align="center" justify="center" direction="column" gap="5px">
              <Text fw="600" color="#1F2A37">
                {account?.lastName} {account?.firstName}
              </Text>
              <Text fw="400" color="#1F2A37">
                {account?.login}
              </Text>
            </Flex>
          </Flex>
          <AppShell.Section grow my="md" component={ScrollArea}>
            {navigationMarkup}
          </AppShell.Section>
          {adminButton}
          {authButton}
        </AppShell.Navbar>
        <AppShell.Main bg="#F6F6F6" pb="70px">
          {actualPageMarkup}
        </AppShell.Main>
      </AppShell>
    </Box>
  );
};
