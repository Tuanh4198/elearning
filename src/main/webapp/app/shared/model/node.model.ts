import { IDocument } from 'app/shared/model/document.model';
import { NodeStatus } from 'app/shared/model/enumerations/node-status-enum.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { IMetafield } from 'app/shared/model/metafields.model';

export enum NodeMetafieldKey {
  description = 'description',
  position = 'position',
}

export interface INode {
  id: string | number;
  flowId?: number;
  rootId?: string | number;
  type: NodeType;
  status?: NodeStatus;
  label?: string;
  positionX?: number;
  positionY?: number;
  width?: number;
  height?: number;
  metafields?: IMetafield<NodeMetafieldKey>[];
  rules?: Array<INodeRules>;
  course?: INodeCourse;
  exam?: INodeExam;
  thumbUrl?: string;
  erThumbUrl?: string;
}

export enum NodeRuleNamespace {
  NODE_TIME = 'NODE_TIME',
  NODE_EMPLOYEE = 'NODE_EMPLOYEE',
  DOCUMENT = 'DOCUMENT',
  EXAM = 'EXAM',
}

export interface INodeRules {
  namespace?: NodeRuleNamespace;
  id?: number;
  rootId?: number;
  condition?: string;
  action?: string;
  priority?: number;
  description?: string;
  ruleNamespace?: string;
}

export interface INodeCourse {
  id?: number;
  title?: string;
  requireJoin?: boolean;
  requireAttend?: boolean;
  meetingUrl?: string;
  meetingPassword?: string;
  description?: string;
  applyTime?: string;
  expireTime?: string;
  documents?: Array<IDocument>;
  categoryId?: number;
  examId?: number;
  assignStrategy?: any;
  assignStrategyJson?: any;
  thumbUrl?: string;
  category?: any;
  metafields?: IMetafield<any>[];
  nodeId?: number;
}

export enum PointStrategyEnum {
  PERCENTAGE = 'PERCENTAGE',
}

export enum PoolStrategyEnum {
  WEIGHT = 'WEIGHT',
  MANUAL = 'MANUAL',
}

export enum ExamStrategyEnum {
  RANDOM = 'RANDOM',
  ALL = 'ALL',
}

export interface INodeExam {
  id?: number;
  title?: string;
  requireJoin?: boolean;
  description?: string;
  nodeid?: number;
  categoryId?: string | number;
  applyTime?: string;
  expireTime?: string;
  thumbUrl?: string;
  nodeId?: number;
  pointStrategy?: PointStrategyEnum;
  minPointToPass?: number;
  courseId?: string | number;
  numberOfQuestion?: number;
  poolStrategy?: PoolStrategyEnum;
  examStrategy?: ExamStrategyEnum;
  rules?: Array<INodeRules>;
  quizzPools?: IQuizzPool[];
  documents?: Array<IDocument>;
  assignStrategy?: any;
  assignStrategyJson?: any;
  metafields?: IMetafield<any>[];
  category?: any;
}

export enum QuizzPoolMetafieldKey {
  weight = 'weight',
}

export interface IQuizzPool {
  id?: string | number;
  sourceId?: string | number;
  categoryId?: number;
  categoryName?: string;
  metafields?: IMetafield<QuizzPoolMetafieldKey>[];
}
