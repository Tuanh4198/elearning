import React, { ReactNode } from 'react';
import { Button, Flex, Modal, Text, Image } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';

interface IModalTakeAnExamBase {
  close: () => void;
}

interface IModalTakeAnExam {
  onClickOk: () => void;
  okTitle: string;
  content: ReactNode;
  hasClose: boolean;
}

const ModalConfirmExam = ({ onClickOk, okTitle, content, close }: IModalTakeAnExamBase & IModalTakeAnExam) => {
  return (
    <Flex p="20px" align="center" direction="column" gap="24px">
      <Image w="80%" m="auto" src={'../../../../../content/images/confirm-exam.svg'} alt="empty" />
      <Text ta="center" color="#111928" size="16px" fw="600">
        {content}
      </Text>
      <Button
        onClick={() => {
          onClickOk();
          close();
        }}
        fullWidth
        variant="filled"
        color="#2A2A86"
        size="lg"
        radius="16px"
      >
        {okTitle}
      </Button>
    </Flex>
  );
};

export const useModalConfirmExam = (props: IModalTakeAnExam) => {
  const [opened, { open, close }] = useDisclosure(false);

  return {
    openedModalConfirmExam: opened,
    onOpenModalConfirmExam: open,
    renderModalConfirmExam: () => (
      <Modal
        closeOnEscape={false}
        opened={opened}
        onClose={props.hasClose ? close : () => {}}
        title={undefined}
        radius="16px"
        closeButtonProps={{ hidden: true }}
        className={`exam-modal modal-confirm-exam ${opened}`}
      >
        {opened && <ModalConfirmExam close={close} {...props} />}
      </Modal>
    ),
  };
};
