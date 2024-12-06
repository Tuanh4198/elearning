import React, { ReactNode, useMemo } from 'react';
import { Button, Flex, Modal, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';

interface UseModalConfirmProps {
  loading?: boolean;
  title: string;
  content: ReactNode;
  onOk: () => void;
}

export const useModalConfirm = ({ loading, title, content, onOk }: UseModalConfirmProps) => {
  const [opened, { open, close }] = useDisclosure(false);

  const modalConfirm = useMemo(
    () => (
      <Modal
        closeOnEscape={false}
        opened={opened}
        onClose={close}
        closeButtonProps={{ disabled: loading }}
        title={
          <Text color="#111928" fw={600} fz={18}>
            {title}
          </Text>
        }
        size="sm"
        radius={8}
      >
        <Text color="#4B5563" mb={24}>
          {content}
        </Text>
        <Flex justify="flex-end" gap={20}>
          <Button disabled={loading} variant="outline" onClick={close}>
            Hủy
          </Button>
          <Button
            loading={loading}
            variant="filled"
            onClick={() => {
              close();
              onOk();
            }}
          >
            Xác nhận
          </Button>
        </Flex>
      </Modal>
    ),
    [opened, title, content, onOk, close]
  );

  return {
    openModal: open,
    closeModal: close,
    modalConfirm,
  };
};
