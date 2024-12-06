/* eslint-disable @typescript-eslint/no-misused-promises */
import { Box, Button, FileButton, Flex, Pagination, Radio, ScrollArea, Textarea, TextInput, Text } from '@mantine/core';
import { MagnifyingGlass } from '@phosphor-icons/react';
import { useFetchDocument } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchDocument';
import { useUploadFile } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUploadFile';
import { IDocument } from 'app/shared/model/document.model';
import React, { useRef, useState } from 'react';

export const typeVideo = 'youtube';

enum ContentType {
  VIDEO = 'VIDEO',
  DOCUMENT = 'DOCUMENT',
}

interface ModalSelectDocumentProps {
  onClose: () => void;
  onOk: (selectedDocument: IDocument) => void;
  selectedDocumentContentDefault?: string;
  linkValueDefault?: string;
}

export const ModalSelectDocument = ({ onClose, onOk, selectedDocumentContentDefault, linkValueDefault }: ModalSelectDocumentProps) => {
  const { isLoading, data, size, total, search, setSearch, page, setPage, refetch } = useFetchDocument();

  const { onUploadFile, isUploading } = useUploadFile({
    onUploadSuccessed() {
      if (page === 1) {
        refetch();
      } else {
        setPage(1);
      }
    },
  });

  const [type, setType] = useState<string>(linkValueDefault ? ContentType.VIDEO : ContentType.DOCUMENT);

  const selectedDocumentRef = useRef<IDocument>();

  const [selectedDocumentContent, setSelectedDocumentContent] = useState<string | undefined>(selectedDocumentContentDefault);
  const [linkValue, setLinkValue] = useState<string | undefined>(linkValueDefault);

  const onSubmit = () => {
    if (selectedDocumentRef.current && type === ContentType.DOCUMENT) {
      onOk(
        type === ContentType.DOCUMENT
          ? selectedDocumentRef.current
          : {
              content: linkValue,
              name: 'Link video',
              type: typeVideo,
            }
      );
    }
    if (linkValue && type === ContentType.VIDEO) {
      onOk({
        content: linkValue,
        name: 'Link video',
        type: typeVideo,
      });
    }
    onClose();
  };

  return (
    <Box>
      <Radio.Group value={type} onChange={setType}>
        <Flex mb="xs" w="100%" gap={20}>
          <Radio value={ContentType.DOCUMENT} label="Chọn tài liệu đã tải lên" />
          <Radio value={ContentType.VIDEO} label="Nhập link video" />
        </Flex>
      </Radio.Group>
      {type === ContentType.DOCUMENT ? (
        <>
          <Flex gap={20} mb="sm">
            <TextInput
              value={search}
              onChange={e => setSearch(e.target.value)}
              flex={7}
              radius={4}
              placeholder="Tìm kiếm"
              leftSectionPointerEvents="none"
              leftSection={<MagnifyingGlass />}
            />
            <FileButton disabled={isUploading} onChange={onUploadFile}>
              {props => <Button {...props}>Upload file</Button>}
            </FileButton>
          </Flex>
          {data && data?.length > 0 ? (
            <ScrollArea h="400px" scrollbars="y">
              {data?.map(i => (
                <Flex
                  key={i.id}
                  onClick={() => {
                    setSelectedDocumentContent(i.content);
                    selectedDocumentRef.current = i;
                  }}
                  justify="space-between"
                  gap={15}
                  py={15}
                  px={8}
                  style={{ borderBottom: '1px solid #E5E7EB', cursor: 'pointer' }}
                >
                  <Text fw={400} c={selectedDocumentContent === i.content ? '#2a2a86' : '#000000'}>
                    {i.name}
                  </Text>
                </Flex>
              ))}
            </ScrollArea>
          ) : (
            <Box ta="center" c="#1F2A37" px="sm" py="lg" bd="1px solid #E5E7EB">
              Không có dữ liệu
            </Box>
          )}
          {Math.ceil(total / size) > 1 && (
            <Flex justify="flex-end">
              <Pagination
                disabled={isLoading}
                size="sm"
                mt={10}
                radius="50%"
                total={Math.ceil(total / size)}
                value={page}
                hideWithOnePage
                onChange={setPage}
              />
            </Flex>
          )}
        </>
      ) : (
        <Textarea
          value={linkValue}
          onChange={e => setLinkValue(e.currentTarget.value)}
          label="Nhập link video"
          placeholder="Nhập link video"
          autosize
          minRows={4}
        />
      )}
      <Flex w="100%" justify="flex-end" mt={24} gap={20}>
        <Button variant="outline" onClick={onClose}>
          Hủy
        </Button>
        <Button
          disabled={type === ContentType.DOCUMENT ? selectedDocumentContent == null : linkValue == null && linkValue === ''}
          onClick={onSubmit}
        >
          Xác nhận
        </Button>
      </Flex>
    </Box>
  );
};
