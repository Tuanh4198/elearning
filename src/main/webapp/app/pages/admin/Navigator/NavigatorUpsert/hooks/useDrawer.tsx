import { Drawer, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import React, { useContext, useEffect, useRef } from 'react';

export const useDrawer = (id: string) => {
  const { nodesRef, openedDrawer, setOpenedDrawer } = useContext(NavigatorUpsertContext);

  const [opened, { open, close }] = useDisclosure(false);

  const openedRef = useRef(opened);

  useEffect(() => {
    openedRef.current = opened;
  }, [opened]);

  useEffect(() => {
    if (openedDrawer === id) {
      open();
    } else if (openedRef.current) {
      close();
    }
  }, [openedDrawer]);

  const onClose = () => {
    setOpenedDrawer(undefined);
    close();
  };

  return {
    opened,
    drawer({ content, title }: { content: React.JSX.Element; title: string }) {
      return (
        <Drawer
          opened={opened}
          onClose={onClose}
          position="right"
          className="navigator-drawer"
          withOverlay={false}
          title={
            <Text c="black" fz={16} fw={600}>
              {title}
            </Text>
          }
        >
          {opened && content}
        </Drawer>
      );
    },
  };
};
