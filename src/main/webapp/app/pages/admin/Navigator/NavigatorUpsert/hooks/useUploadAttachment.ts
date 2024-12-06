import { FileWithPath } from '@mantine/dropzone';
import { IDocument } from 'app/shared/model/document.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { notiError } from 'app/shared/notifications';
import axios from 'axios';
import { useState } from 'react';

export interface StagedUploadsRespon {
  parameters: Array<string>;
  resourceUrl: '';
  url: '';
}

export const useUploadAttachment = (nodeType: NodeType, onOk: () => void) => {
  const [isUploading, setIsUploading] = useState<boolean>(false);

  const handleUploadFile = async ({ apiFileUploadUrl, files }: { apiFileUploadUrl: string; files?: FileWithPath[] | File[] }) => {
    if (files && files.length > 0) {
      const file = files[0];
      const fileBlob = new Blob([file]);
      await axios.put(apiFileUploadUrl, fileBlob, {
        headers: {
          'Content-Type': file.type,
        },
      });
    }
  };

  const onUploadAttachment = async (file?: File | null) => {
    if (file == null) return;
    setIsUploading(true);
    try {
      const res = await axios.post<Array<StagedUploadsRespon>>(`/api/staged-uploads`, [
        {
          filename: file.name,
          mime_type: file.type,
          resource: 'IMAGE',
        },
      ]);
      if (res?.data?.[0].url && res?.data?.[0].resourceUrl) {
        await handleUploadFile({
          apiFileUploadUrl: res?.data?.[0].resourceUrl,
          files: [file],
        });
      }
      await axios.post<IDocument>(`/api/attachments`, {
        source: nodeType,
        url: res?.data?.[0].url,
        attachmentType: file.type,
        attachmentName: file.name,
      });
      onOk();
    } catch (error) {
      console.error('Staged uploads error: ', error);
      notiError({ message: 'Tải file lỗi, vui lòng chọn file khác.' });
    }
    setIsUploading(false);
  };

  return { onUploadAttachment, isUploading };
};
