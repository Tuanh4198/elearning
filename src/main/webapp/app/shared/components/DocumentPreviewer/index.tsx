import React, { memo } from 'react';

type DocumentPreviewerProps = {
  file?: File;
  fileUrl?: string;
  mimeType?: string;
};

export const DocumentPreviewer = memo(({ file, fileUrl, mimeType }: DocumentPreviewerProps) => {
  const hasContent = Boolean(fileUrl) || Boolean(file);
  const previewFileUrl = fileUrl || (file && URL.createObjectURL(file)) || undefined;
  const previewMineType = mimeType || file?.type || undefined;
  return (
    <div className="pegasus-previewer" style={{ height: '100%' }}>
      {!hasContent ? (
        <div className="pegasus-previewer-empty">
          <div>Chọn nội dung đào tạo để xem trước</div>
        </div>
      ) : (
        <Content fileUrl={previewFileUrl} mimeType={previewMineType} />
      )}
    </div>
  );
});

function Content({ fileUrl, mimeType }: { fileUrl?: string; mimeType?: string }) {
  if (!fileUrl || !mimeType) return <></>;

  if (fileUrl.startsWith('https://drive.google.com/file/d/')) {
    return (
      <iframe
        style={{ width: '100%', height: '100%', border: 'none' }}
        src={fileUrl.replace('view?usp=sharing', 'preview')}
        allow="autoplay"
      />
    );
  }

  switch (mimeType) {
    case 'image/jpeg':
    case 'image/jpg':
    case 'image/png':
      return <img width="100%" src={fileUrl} />;

    case 'application/pdf':
    case 'false':
      return <iframe src={fileUrl} style={{ width: '100%', height: '100%', border: 'none' }} />;

    case 'text/plain':
      return <>Previewing text....</>;

    case 'video/mp4':
      return <video width="100%" height="100%" controls src={fileUrl} />;

    default:
      return <>Invalid file</>;
  }
}
