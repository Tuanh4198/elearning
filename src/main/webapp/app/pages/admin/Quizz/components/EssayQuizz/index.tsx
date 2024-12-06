import React, { useState } from 'react';
import { Flex, Radio, Stack, Text } from '@mantine/core';
import { RichTextEditorCustom } from 'app/pages/admin/Quizz/components/RichTextEditorCustom';

export const EssayQuizz = () => {
  const [quizzType, setQuizzType] = useState<string>();

  return (
    <Flex direction="column" w="100%" gap={20}>
      <RichTextEditorCustom />
      <Radio.Group
        onChange={setQuizzType}
        value={quizzType}
        label={
          <Text display="inline-block" c="#1F2A37" fw={600}>
            Định dạng đáp án
          </Text>
        }
        withAsterisk
      >
        <Stack mt={15}>
          <Radio value="react" label="Văn bản" />
          <Radio value="svelte" label="Số" />
          <Radio value="ng" label="Ngày (mm/dd/yyyy)" />
        </Stack>
      </Radio.Group>
    </Flex>
  );
};
