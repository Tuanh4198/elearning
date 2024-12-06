import {
  ActionIcon,
  Box,
  Button,
  Divider,
  Flex,
  Grid,
  Input,
  Loader,
  LoadingOverlay,
  Pagination,
  ScrollArea,
  Tabs,
  Text,
} from '@mantine/core';
import { useDebouncedValue } from '@mantine/hooks';
import { MagnifyingGlass, PlusSquare, X } from '@phosphor-icons/react';
import { useFetchQuizzes } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchQuizzes';
import { QuizzTypeEnum } from 'app/shared/model/enumerations/quizz-type-enum.model';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';
import { IQuizz } from 'app/shared/model/quizz.model';
import axios from 'axios';
import React, { Fragment, useEffect, useMemo, useState } from 'react';

const tabType = [
  {
    label: 'Câu trắc nghiệm',
    value: QuizzTypeEnum.MULTIPLE_CHOICE,
  },
  {
    label: 'Câu tự luận',
    value: QuizzTypeEnum.ESSAY,
  },
  {
    label: 'Câu điền vào chỗ trống',
    value: QuizzTypeEnum.BLANK,
    disable: true,
  },
];

interface ModalSelectQuizzProps {
  onClose: () => void;
  onSubmit: (quizzs: IQuizz[]) => void;
  category?: IQuizzCategory;
  defaultSelectedIds?: (string | number)[];
  disabledUpdate: boolean;
}

export const ModalSelectQuizz = ({ onClose, onSubmit, category, defaultSelectedIds, disabledUpdate }: ModalSelectQuizzProps) => {
  const [activeTab, setActiveTab] = useState<string | null>(tabType[0].value);

  const [selectedQuizzs, setSelectedQuizzs] = useState<IQuizz[]>([]);
  const [fetchingDefault, setFetchingDefault] = useState<boolean>(false);

  const selectedQuizzIds = useMemo(() => selectedQuizzs.map(i => i.id), [selectedQuizzs]);

  const fetchCategories = async () => {
    if (!defaultSelectedIds || defaultSelectedIds?.length <= 0) return;
    setFetchingDefault(true);
    const res = await axios.get<IQuizz[]>(`/api/quizzes`, {
      params: {
        quizzIds: defaultSelectedIds,
        size: defaultSelectedIds?.length,
      },
    });
    setSelectedQuizzs(res.data);
    setFetchingDefault(false);
  };

  useEffect(() => {
    fetchCategories();
  }, [defaultSelectedIds]);

  return (
    <Fragment>
      <Grid>
        <Grid.Col span={8}>
          <Tabs defaultValue={activeTab} onChange={value => setActiveTab(value)}>
            <Tabs.List>
              {tabType.map(tab => (
                <Tabs.Tab disabled={tab.disable} key={tab.value} value={tab.value}>
                  {tab.label}
                </Tabs.Tab>
              ))}
            </Tabs.List>
            {tabType.map(tab => (
              <TableQuizzType
                disabledUpdate={disabledUpdate}
                tabValue={tab.value}
                key={tab.value}
                category={category}
                selectedQuizzIds={selectedQuizzIds}
                setSelectedQuizzs={setSelectedQuizzs}
              />
            ))}
          </Tabs>
        </Grid.Col>
        <Grid.Col span={4}>
          <ListQuizzSelected
            disabledUpdate={disabledUpdate}
            selectedQuizzs={selectedQuizzs}
            setSelectedQuizzs={setSelectedQuizzs}
            isLoading={fetchingDefault}
          />
        </Grid.Col>
      </Grid>
      <Divider my="md" />
      <Flex w="100%" justify="flex-end" mt="md" gap={20}>
        <Button variant="outline" onClick={onClose}>
          Hủy
        </Button>
        <Button
          disabled={selectedQuizzs.length <= 0}
          onClick={() => {
            onClose();
            if (disabledUpdate) return;
            onSubmit(selectedQuizzs);
          }}
        >
          Xác nhận
        </Button>
      </Flex>
    </Fragment>
  );
};

const TableQuizzType = ({
  disabledUpdate,
  tabValue,
  category,
  selectedQuizzIds,
  setSelectedQuizzs,
}: {
  disabledUpdate: boolean;
  tabValue: QuizzTypeEnum;
  category?: IQuizzCategory;
  selectedQuizzIds: (number | undefined)[];
  setSelectedQuizzs: React.Dispatch<React.SetStateAction<IQuizz[]>>;
}) => {
  const [search, setSearch] = useState<string>('');
  const [page, setPage] = useState<number>(1);
  const [debounced] = useDebouncedValue(search, 200);

  const { data: quizzes, size, total, isLoading } = useFetchQuizzes({ search: debounced, page, type: tabValue, categoryId: category?.id });

  return (
    <Tabs.Panel key={tabValue} value={tabValue}>
      <Box pos="relative">
        <LoadingOverlay style={{ borderRadius: '8px' }} zIndex={10} visible={isLoading} loaderProps={{ children: <Loader /> }} />
        <Flex gap={12} my={20} align="center">
          <Input
            flex={1}
            value={search}
            placeholder="Tìm kiếm câu hỏi"
            leftSection={<MagnifyingGlass />}
            onChange={e => {
              setSearch(e.target.value);
            }}
          />
        </Flex>
        <Box p={8} bg="#E5E7EB" c="#1F2A37">
          Nội dung câu hỏi
        </Box>
        {quizzes && quizzes?.length > 0 ? (
          <ScrollArea h="400px" scrollbars="y">
            {quizzes?.map(i => {
              const isSelected = selectedQuizzIds.includes(i.id);
              return (
                <Flex key={i.id} justify="space-between" gap={15} py={15} px={8} style={{ borderBottom: '1px solid #E5E7EB' }}>
                  <Text fw={600} c={isSelected ? '#2A2A86' : '#1F2A37'}>
                    {i.content}
                  </Text>
                  <ActionIcon disabled={isSelected || disabledUpdate} bg="white" onClick={() => setSelectedQuizzs(old => [...old, i])}>
                    <PlusSquare color="#343330" size={32} weight="fill" opacity={isSelected || disabledUpdate ? 0.5 : 1} />
                  </ActionIcon>
                </Flex>
              );
            })}
          </ScrollArea>
        ) : (
          <Box ta="center" c="#1F2A37" p="sm" bd="1px solid #E5E7EB">
            Không có dữ liệu
          </Box>
        )}
        {Math.ceil(total / size) > 1 && (
          <Flex justify="flex-end" mt="sm">
            <Pagination
              total={Math.round(total / size)}
              value={page}
              disabled={isLoading}
              hideWithOnePage
              radius="50%"
              onChange={setPage}
              mt="sm"
            />
          </Flex>
        )}
      </Box>
    </Tabs.Panel>
  );
};

const ListQuizzSelected = ({
  disabledUpdate,
  isLoading,
  selectedQuizzs,
  setSelectedQuizzs,
}: {
  disabledUpdate: boolean;
  isLoading?: boolean;
  selectedQuizzs: IQuizz[];
  setSelectedQuizzs: React.Dispatch<React.SetStateAction<IQuizz[]>>;
}) => {
  return (
    <Box pos="relative" bd="1px solid #E5E7EB" h="100%" style={{ borderRadius: '8px', padding: '5px 20px' }}>
      <LoadingOverlay style={{ borderRadius: '8px' }} zIndex={10} visible={isLoading} loaderProps={{ children: <Loader /> }} />
      <Text fw={600} c="#111928">
        Danh sách câu hỏi đã chọn ({selectedQuizzs.length})
      </Text>
      <Divider my="sm" />
      <ScrollArea h="470px" scrollbars="y">
        {selectedQuizzs.map((i, index) => (
          <Flex key={i.id} align="center" pl={5} gap={12} mt={index > 0 ? 'sm' : undefined}>
            <Text c="#111928" flex={1}>
              {i.content}
            </Text>
            {!disabledUpdate && (
              <ActionIcon bg="white" onClick={() => setSelectedQuizzs(old => old.filter(iOld => iOld.id !== i.id))}>
                <X color="#343330" size={32} />
              </ActionIcon>
            )}
          </Flex>
        ))}
      </ScrollArea>
    </Box>
  );
};
