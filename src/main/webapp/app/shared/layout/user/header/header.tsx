import React from 'react';
import { Burger, Group } from '@mantine/core';

const Header = ({
  mobileNavigationActive,
  toggleMobileNavigationActive,
  desktopNavigationActive,
  toggleDesktopNavigationActive,
}: {
  mobileNavigationActive: boolean;
  toggleMobileNavigationActive: () => void;
  desktopNavigationActive: boolean;
  toggleDesktopNavigationActive: () => void;
}) => {
  return (
    <Group h="100%" px="20px" justify="space-between">
      <Group h="100%">
        <Burger opened={mobileNavigationActive} onClick={toggleMobileNavigationActive} hiddenFrom="sm" size="sm" />
        <Burger opened={desktopNavigationActive} onClick={toggleDesktopNavigationActive} visibleFrom="sm" size="sm" p="0" />
      </Group>
      <Group ml="xl" gap="md" visibleFrom="sm"></Group>
    </Group>
  );
};

export default Header;
