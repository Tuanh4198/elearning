import React, { useMemo, useState } from 'react';
import { FileWithPath } from '@mantine/dropzone';
import { Image } from '@mantine/core';

export const useChooseFile = () => {
  const [files, setFiles] = useState<FileWithPath[]>();

  const previewFiles = useMemo(() => {
    if (files && files?.length > 0) {
      return files.map((file, index) => {
        const imageUrl = URL.createObjectURL(file);
        return (
          <Image
            key={index}
            width="100%"
            height={120}
            radius={8}
            fit="cover"
            flex={1}
            src={imageUrl}
            onLoad={() => URL.revokeObjectURL(imageUrl)}
          />
        );
      });
    }
    return null;
  }, [files]);

  return {
    setFiles,
    previewFiles,
  };
};
