import { IDocument } from 'app/shared/model/document.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { INodeCourse, INodeExam, INodeRules, IQuizzPool, PointStrategyEnum, PoolStrategyEnum } from 'app/shared/model/node.model';
import { Node, NodeProps } from 'reactflow';

export type TCustomNodeData = {
  id?: string | number;
  rootId?: string | number;
  position?: number;
  label?: string;
  description?: string;
  thumbUrl?: string;
  erThumbUrl?: string;
  status?: boolean;
  type?: NodeType;
  positionX?: number;
  positionY?: number;
  width?: number;
  height?: number;
  items?: TCustomNodeData[];
  flowId?: number;
  startTime?: Date;
  endTime?: Date;
  rules?: INodeRules[];
  departments?: string[];
  roles?: string[];
  employeeCodes?: string[];
  // course
  course?: INodeCourse;
  courseTitle?: string;
  courseRequireJoin?: boolean;
  courseRequireAttend?: boolean;
  courseMeetingUrl?: string;
  courseMeetingPassword?: string;
  courseDescription?: string;
  courseDocuments?: Array<IDocument>;
  // exam
  exam?: INodeExam;
  examRequireJoin?: boolean;
  examMinPointToPass?: number;
  examNumberOfQuestion?: number;
  examPoolStrategy?: PoolStrategyEnum;
  examPointStrategy?: PointStrategyEnum;
  examStrategy?: boolean;
  examQuizzPools?: Array<IQuizzPool>;
  examRules?: Array<INodeRules>;
  examWorkingTime?: number;
  examMaxNumberOfTest?: number;
  examDocuments?: Array<IDocument>;
};

export type TCustomNode = Node<TCustomNodeData, NodeType | string>;

export type TCustomNodeProps = NodeProps<TCustomNodeData>;

export type TNodeExpand = Node<TCustomNodeData, string | undefined>;

export interface TempCategory {
  tempId: string;
  categoryId?: number;
  categoryName?: string;
  tempQuizzPools: IQuizzPool[];
}
