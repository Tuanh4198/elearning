import React, { memo } from 'react';
import { AspectRatio, Box, Flex, Image, Overlay, Text } from '@mantine/core';
import { Trash, PlusCircle, Image as ImageIco } from '@phosphor-icons/react';
import { ModalSampleBanner } from 'app/pages/admin/Navigator/NavigatorUpsert/components/ModalSampleBanner';
import { useDisclosure } from '@mantine/hooks';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';

interface BtnSelectBannerProps {
  banner?: { url?: string; thumbUrl?: string };
  setbanner: React.Dispatch<React.SetStateAction<{ url?: string; thumbUrl?: string }>>;
  type: NodeType;
}

export const BtnSelectBanner = memo(({ banner, setbanner, type }: BtnSelectBannerProps) => {
  const [openedModalSampleBanner, { open: openModalSampleBanner, close: closeModalSampleBanner }] = useDisclosure(false);

  return (
    <>
      {banner?.url && banner?.thumbUrl ? (
        <Box
          pos="relative"
          className="field-image"
          bd="1px dashed #E5E7EB"
          style={{ borderRadius: '8px', width: '150px', height: '150px' }}
        >
          <Image flex={1} radius={8} fit="cover" width="100%" src={banner.thumbUrl} style={{ aspectRatio: '1 / 1' }} />
          <Box pos="absolute" top={0} left={0} right={0} bottom={0} className="field-image-action">
            <AspectRatio ratio={1 / 1} w="100%" mx="auto" pos="relative" className="field-image-overlay">
              <Overlay color="#000" radius={8} backgroundOpacity={0.5} />
            </AspectRatio>
            <Flex gap={10} align="center" justify="center" w="100%" h="100%" className="field-image-btn">
              <Flex
                onClick={openModalSampleBanner}
                direction="column"
                align="center"
                gap={8}
                variant="transparent"
                color="#ffffff"
                className="btn-change"
              >
                <ImageIco size={44} color="#ffffff" />
                <Text fw={600} color="white">
                  Thay ảnh
                </Text>
              </Flex>
              <Flex
                onClick={() => setbanner && setbanner({ thumbUrl: undefined, url: undefined })}
                direction="column"
                align="center"
                gap={8}
                variant="transparent"
                color="#ffffff"
                className="btn-remove"
              >
                <Trash size={44} color="#ffffff" />
                <Text fw={600} color="white">
                  Xoá ảnh
                </Text>
              </Flex>
            </Flex>
          </Box>
        </Box>
      ) : (
        <Box onClick={openModalSampleBanner} bd="1px dashed #E5E7EB" style={{ borderRadius: '8px', width: '150px', height: '150px' }}>
          <Flex w="100%" h="100%" direction="column" align="center" justify="center" gap={4}>
            <PlusCircle size={44} color="#1F2A37" />
            <Text fz={14} c="#000000" ta="center">
              Thêm ảnh bìa
            </Text>
          </Flex>
        </Box>
      )}
      <ModalSampleBanner onOk={setbanner} type={type} onClose={closeModalSampleBanner} opened={openedModalSampleBanner} />
    </>
  );
});
