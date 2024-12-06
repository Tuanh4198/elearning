import React, { useMemo, useState } from 'react';
import { Box, Flex, Group, Pill, Radio, Text, Input, Button } from '@mantine/core';
import { RichTextEditorCustom } from 'app/pages/admin/Quizz/components/RichTextEditorCustom';
import { v4 as uuidv4 } from 'uuid';
import { IQuizzAnswer } from 'app/shared/model/quizz-answer.model';
import { Plus } from '@phosphor-icons/react';

const space = '_____';

const maxLength = 15;

const defaultAnswers: IQuizzAnswer[] = [
  {
    id: uuidv4(),
    title: `option${String.fromCharCode(65)}`,
    content: '',
  },
];

type TSetState = React.Dispatch<React.SetStateAction<IQuizzAnswer[]>>;

export const BlankQuizz = () => {
  const [repType, setRepType] = useState<string>();
  const [answers, setAnswers] = useState<IQuizzAnswer[]>(defaultAnswers);
  const [fakeAnswers, setFakeAnswers] = useState<IQuizzAnswer[]>([]);

  const handleChangeAnswer = (setState: TSetState, value: string, updateId?: number | string) => {
    setState(old => {
      const newAnswers = [...(old || [])].map(a => {
        if (a.id?.toString() === updateId?.toString()) {
          return {
            ...a,
            content: value,
          };
        }
        return a;
      });
      return newAnswers;
    });
  };

  const handleAddAnswer = (setState: TSetState) => {
    setState(old => {
      const newAnswers = [
        ...(old || []),
        {
          id: uuidv4(),
          title: `option${String.fromCharCode(65 + (old?.length || 0))}`,
          content: '',
        },
      ];
      return newAnswers;
    });
  };

  const handleRemoveAnswer = (setState: TSetState, deleteId?: string | number) => {
    setState(old => {
      const newAnswers = [...(old?.filter(a => a.id?.toString() !== deleteId?.toString()) ?? [])].map((a, idx) => ({
        ...a,
        title: `option${String.fromCharCode(65 + idx)}`,
      }));
      return newAnswers;
    });
  };

  const pillInput = (a: IQuizzAnswer) => (
    <Pill
      h="auto"
      mih="unset"
      key={a.id}
      bg="#E5E7EB"
      radius={24}
      bd="1px solid #E5E7EB"
      withRemoveButton
      onRemove={() => handleRemoveAnswer(setFakeAnswers, a.id)}
    >
      <Input
        styles={{
          input: {
            background: 'transparent',
            border: 'none',
            textDecoration: a.content ? 'underline' : 'unset',
          },
        }}
        miw={120}
        p={0}
        placeholder="Nhập cụm từ"
        value={a.content}
        maxLength={maxLength}
        onChange={e => handleChangeAnswer(setFakeAnswers, e.target.value, a.id)}
      />
    </Pill>
  );

  return (
    <Flex direction="column" w="100%" gap={20}>
      <Text c="#4B5563">Hãy sử dụng các khoảng trống {space} để xác định từ cần điền.</Text>
      <Box pos="relative">
        <RichTextEditorCustom />
        <Pill c="#4B5563" bg="#E5E7EB" pos="absolute" bottom={10} right={10}>
          (Shift + S) để thêm {space}
        </Pill>
      </Box>
      <Radio.Group
        onChange={setRepType}
        value={repType}
        label={
          <Text display="inline-block" c="#1F2A37" fw={600}>
            Hình thức trả lời
          </Text>
        }
        withAsterisk
      >
        <Group mt={15}>
          <Radio value="react" label="Đáp án có sẵn" />
          <Radio value="svelte" label="Nhập đáp án" />
        </Group>
      </Radio.Group>
      <Flex direction="column">
        <Input.Label required>
          <Text display="inline-block" c="#1F2A37" fw={600}>
            Đáp án
          </Text>
        </Input.Label>
        <Input.Description>Bạn hãy thêm các cụm từ đúng cần điền lần lượt vào các chỗ trống.</Input.Description>
      </Flex>
      <Flex direction="column">
        <Input.Label>
          <Text display="inline-block" c="#1F2A37" fw={600}>
            Cụm từ nhiễu
          </Text>
        </Input.Label>
        <Input.Description>Bạn hãy thêm các cụm từ khác để làm nhiễu đáp án.</Input.Description>
      </Flex>
      <Flex gap={10} wrap="wrap">
        {fakeAnswers.map(a => pillInput(a))}
        <Button onClick={() => handleAddAnswer(setFakeAnswers)} radius={25} color="#1F2A37" c="#F9F9FF" leftSection={<Plus />}>
          Thêm
        </Button>
      </Flex>
    </Flex>
  );
};
