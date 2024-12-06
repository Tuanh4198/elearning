/* eslint-disable @typescript-eslint/no-misused-promises */
import {
  Grid,
  Modal,
  Image,
  Flex,
  Button,
  AspectRatio,
  Overlay,
  Skeleton,
  Container,
  Pagination,
  Divider,
  FileButton,
  LoadingOverlay,
  Loader,
  Box,
} from '@mantine/core';
import { CaretDoubleLeft, CaretDoubleRight, CheckCircle } from '@phosphor-icons/react';
import { useFetchAttachment } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchAttachment';
import { useUploadAttachment } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUploadAttachment';
import { Empty } from 'app/shared/components/Empty';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import React, { useState } from 'react';

export const ModalSampleBanner = ({
  onOk,
  opened,
  onClose,
  type,
}: {
  onOk: ({ url, thumbUrl }: { url: string; thumbUrl: string }) => void;
  opened: boolean;
  onClose: () => void;
  type: NodeType;
}) => {
  const { data, isLoading, total, page, size, handleNextPage, refetch } = useFetchAttachment(type);

  const { onUploadAttachment, isUploading } = useUploadAttachment(type, () => {
    if (page === 1) {
      refetch();
    } else {
      handleNextPage(1);
    }
  });

  const [selected, setSelected] = useState<number>();

  const onSelected = index => {
    if (index === selected) {
      setSelected(undefined);
    } else {
      setSelected(index);
    }
  };

  return (
    <Modal size="xl" opened={opened} onClose={onClose} title={<b>Thêm ảnh bìa</b>}>
      <Box pos="relative">
        <LoadingOverlay
          style={{ borderRadius: '8px' }}
          zIndex={10}
          visible={isLoading || isUploading}
          loaderProps={{ children: <Loader /> }}
        />
        <Flex mb="md">
          <FileButton disabled={isUploading} onChange={onUploadAttachment}>
            {props => <Button {...props}>Upload file</Button>}
          </FileButton>
        </Flex>
        <Grid gutter="12px">
          {isLoading && (!data || data?.length <= 0) ? (
            Array.from({ length: 4 }).map((_, i) => (
              <Grid.Col span={3} key={i}>
                <AspectRatio ratio={1 / 1} pos="relative">
                  <Skeleton w="100%" h="100%" radius="8px" />
                </AspectRatio>
              </Grid.Col>
            ))
          ) : !data || data?.length <= 0 ? (
            <Container py={30}>
              <Empty description="Không có dữ liệu ảnh bìa" />
            </Container>
          ) : (
            data?.map(item => (
              <Grid.Col span={3} key={item.id} onClick={() => onSelected(item.id)}>
                <AspectRatio ratio={1 / 1} pos="relative" bd="1px dashed #E5E7EB">
                  <Image src={item.thumbUrl} w="100%" h="100%" />
                  {selected === item.id && (
                    <Overlay
                      color="#000"
                      backgroundOpacity={0.5}
                      style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
                    >
                      <CheckCircle color="white" size={32} />
                    </Overlay>
                  )}
                </AspectRatio>
              </Grid.Col>
            ))
          )}
        </Grid>
        {Math.ceil(total / size) > 1 && (
          <Flex justify="flex-end" mt={15}>
            <Pagination
              value={page}
              disabled={isLoading}
              hideWithOnePage
              size="sm"
              mt={10}
              radius="50%"
              nextIcon={CaretDoubleRight}
              previousIcon={CaretDoubleLeft}
              total={Math.ceil(total / size)}
              onChange={handleNextPage}
            />
          </Flex>
        )}
        <Divider mt={24} />
        <Flex w="100%" justify="flex-end" mt={24} gap={20}>
          <Button variant="outline" onClick={onClose}>
            Hủy
          </Button>
          <Button
            onClick={() => {
              if (selected) {
                const selectedThumb = data?.find(i => i.id === selected);
                if (selectedThumb)
                  onOk({
                    url: selectedThumb.url,
                    thumbUrl: selectedThumb.thumbUrl,
                  });
              }
              onClose();
            }}
            disabled={selected == null}
          >
            Xác nhận
          </Button>
        </Flex>
      </Box>
    </Modal>
  );
};
