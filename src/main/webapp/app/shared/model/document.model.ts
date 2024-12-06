import { IMetafield } from 'app/shared/model/metafields.model';
import { INodeRules } from 'app/shared/model/node.model';

export enum DocumentMetafieldKey {
  mime_type = 'mime_type',
  type = 'type',
}

export enum DocumentMetafieldType {
  mobile = 'mobile',
  web = 'web',
}

export interface IDocument {
  id?: number;
  rootId?: number;
  content?: string;
  metafields?: IMetafield<DocumentMetafieldKey, DocumentMetafieldType>[];
  name?: string;
  type?: string;
  rules?: Array<INodeRules>;
}
