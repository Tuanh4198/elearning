import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';

export interface IAttachment {
  id: number;
  source: NodeType;
  url: string;
  thumbUrl: string;
  attachmentType: string;
  attachmentName: string;
  createdAt: string;
  updatedAt: string;
}
