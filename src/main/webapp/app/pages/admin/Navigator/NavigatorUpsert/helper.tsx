import { NodeRoot } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeRoot';
import { NodeEmpty } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeEmpty';
import { NodeContainerSeaMap } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaMap';
import { NodeContainerSeaRegion } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaRegion';
import { NodeContainerIslandArea } from 'app/pages/admin/Navigator/NavigatorUpsert/components/IslandArea';
import { NodeContainerIslandOrBoss } from 'app/pages/admin/Navigator/NavigatorUpsert/components/IslandOrBoss';
import { Edge, MarkerType } from 'reactflow';
import Dagre from '@dagrejs/dagre';
import { TCustomNode, TCustomNodeData, TempCategory, TNodeExpand } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import {
  ExamStrategyEnum,
  INode,
  INodeRules,
  IQuizzPool,
  NodeMetafieldKey,
  NodeRuleNamespace,
  PointStrategyEnum,
} from 'app/shared/model/node.model';
import { NodeStatus } from 'app/shared/model/enumerations/node-status-enum.model';
import { IEdge } from 'app/shared/model/edge.model';
import { dateUTCToLocaltime, formatDateTimeUtc } from 'app/shared/util/date-utils';
import React from 'react';
import dayjs from 'dayjs';
import { APP_LOCAL_DATETIME_FORMAT_2 } from 'app/config/constants';

export const nodeTypes = {
  [NodeType.ROOT]: NodeRoot,
  [NodeType.CONTAINER]: NodeEmpty,
  [NodeType.SEA_MAP]: NodeContainerSeaMap,
  [NodeType.SEA_REGION]: NodeContainerSeaRegion,
  [NodeType.ISLAND_AREA]: NodeContainerIslandArea,
  [NodeType.ISLAND]: NodeContainerIslandOrBoss,
  [NodeType.CREEP]: NodeContainerIslandOrBoss,
};

export const nodeContainerLabel = {
  [NodeType.ROOT]: 'Đại Hải Trình',
  [NodeType.SEA_MAP]: 'Hải Trình',
  [NodeType.SEA_REGION]: 'Vùng Biển',
  [NodeType.ISLAND_AREA]: 'Quần Đảo',
  [NodeType.ISLAND]: 'Đảo',
  [NodeType.CREEP]: 'Quái',
};

export const PointStrategyTitle = {
  [PointStrategyEnum.PERCENTAGE]: 'Phần trăm',
};

export const defaultRule: INodeRules = {
  condition: '',
  action: 'output.setValid(true)',
  priority: 1,
  description: 'node permission',
};

export const errorPermission = 'permission';
export const errorDocument = 'document';
export const errorQuizzPool = 'quizz_pool';

export const regSplitCondition = /&&|\|\|/;

export const regEmployeeCode = /^(yd|fgg|dl|ht|mt)\d{4,}$/i;

export const separatorCharacter = '||';

export const maxItemNodeSeaMap = 14;

export const nodeWith = 300;

export const nodeRootHeight = 50;

export const nodeItemHeight = 122;

export const nodeItemGap = 12;

export const nodeContainerHeadHeight = 70;

export const defaultConfigEdge: Partial<Edge> = {
  zIndex: 1000,
  style: { strokeWidth: 2, stroke: '#FAB005' },
  markerEnd: {
    type: MarkerType.ArrowClosed,
    color: '#FAB005',
  },
};

// function

export const miniMapNodeColor = node => {
  switch (node.type) {
    case NodeType.ROOT:
      return '#909090';
    case NodeType.CONTAINER:
      return '#000000';
    case NodeType.SEA_MAP:
      return '#E18D96';
    case NodeType.SEA_REGION:
      return '#C1CD97';
    case NodeType.ISLAND_AREA:
      return '#F3DD83';
    case NodeType.ISLAND:
      return '#A0E3F2';
    case NodeType.CREEP:
      return '#A0E3F2';
    default:
      return '#D7D7D9';
  }
};

export const confirmDeleteLabel = ({ nodeName, nodeType }: { nodeName?: string; nodeType: string }) => (
  <>
    Bạn chắc chắn muốn xóa {nodeType}: <b>{nodeName}</b>
  </>
);

export const validateDeleteLabel = ({ nodeChildName, nodeType }: { nodeChildName?: string; nodeType: string }) => (
  <>
    Cần xóa hết <b>{nodeChildName}</b> con trước khi xóa <b>{nodeType}</b>
  </>
);

export const renderLabelContainer = (type?: NodeType | undefined) => {
  if (type === NodeType.CREEP || type === NodeType.ISLAND) return nodeContainerLabel[NodeType.ISLAND];
  return type && nodeContainerLabel[type];
};

export const getNodeHeight = (itemsLength: number) => {
  return itemsLength <= 0 ? nodeRootHeight : nodeContainerHeadHeight + nodeItemGap * (itemsLength - 1) + nodeItemHeight * itemsLength;
};

export const getLayoutedElements = ({ nodes, edges }: { nodes: TCustomNode[]; edges: Edge[] }) => {
  const g = new Dagre.graphlib.Graph().setDefaultEdgeLabel(() => ({}));
  g.setGraph({ rankdir: 'LR' });
  edges.forEach(edge => g.setEdge(edge.source, edge.target));
  nodes.forEach(node =>
    g.setNode(node.id, {
      ...node,
      width: nodeWith,
      height: getNodeHeight(node.data.items ? node.data.items?.length : 0),
    })
  );
  Dagre.layout(g);
  return {
    nodes: nodes.map(node => {
      const position = g.node(node.id);
      const x = position.x - nodeWith / 2;
      const y = position.y - getNodeHeight(node.data.items ? node.data.items?.length : 0) / 2;
      return { ...node, position: { x, y } };
    }),
    edges,
  };
};

export const mergeConditonEmployee = ({
  conditionsEmployee,
  conditionsDepartmentAndPosition,
}: {
  conditionsEmployee?: string | undefined;
  conditionsDepartmentAndPosition: string[];
}) => {
  if (conditionsDepartmentAndPosition.length > 0) {
    return conditionsDepartmentAndPosition.join(' && ') + `${conditionsEmployee ? ` || ${conditionsEmployee}` : ''}`;
  }
  return `${conditionsEmployee ? conditionsEmployee : ''}`;
};

export const arrToString = (arr: (string | number)[]) => {
  return arr.map(i => `'${i}'`).join(' ,');
};

export const getValueInCondition = ({ condition, withFormatDate }: { condition?: string; withFormatDate?: boolean }) => {
  if (condition) {
    const regex = /'([^']+)'/;
    const match = condition.match(regex);
    if (match) {
      const value = match[1];
      return withFormatDate ? dateUTCToLocaltime(value) : value;
    }
  }
  return undefined;
};

const decayCondition = (condition: string) => {
  const matches = condition.match(/'([^']+)'/g);
  const result = matches?.map(str => str.replace(/'/g, ''));
  return result ?? [];
};

function extractNumbers(condition: string) {
  const regex = /\d+/g;
  const matches = condition.match(regex);
  return matches ? matches.map(Number) : [];
}

export const convertNodeItemToTCustomNodeData = (node: INode): TCustomNodeData => {
  const position = node.metafields?.find(m => m.key === NodeMetafieldKey.position)?.value;
  const description = node.metafields?.find(m => m.key === NodeMetafieldKey.description)?.value;
  const res: TCustomNodeData = {
    id: node.id,
    flowId: node.flowId,
    rootId: node.rootId || '',
    label: node.label?.trim()?.toUpperCase() || '',
    status: node.status === NodeStatus.INACTIVE,
    type: node.type,
    position: position ? Number(position) : 0,
    description: description || '',
    thumbUrl: node.thumbUrl,
    erThumbUrl: node.erThumbUrl,
    height: node.height,
    width: node.width,
    positionX: node.positionX,
    positionY: node.positionY,
  };
  if (node.type === NodeType.SEA_REGION) {
    res.rules = node?.rules;
    res.departments = undefined;
    res.roles = undefined;
    res.employeeCodes = undefined;
    const conditionEmployee = node.rules?.find(r => r.namespace === NodeRuleNamespace.NODE_EMPLOYEE)?.condition;
    if (conditionEmployee) {
      const splitCondition = conditionEmployee?.split(regSplitCondition);
      splitCondition?.forEach(c => {
        if (c.includes('department')) {
          res.departments = decayCondition(c);
        } else if (c.includes('role')) {
          res.roles = decayCondition(c);
        } else if (c.includes('code')) {
          res.employeeCodes = decayCondition(c);
        }
      });
    }
  }
  if (node.type === NodeType.ISLAND_AREA) {
    res.rules = node?.rules;
    res.startTime = undefined;
    const conditionTime = node.rules?.find(r => r.namespace === NodeRuleNamespace.NODE_TIME)?.condition;
    if (conditionTime) {
      const conditionStartTime = getValueInCondition({ condition: conditionTime });
      res.startTime = dayjs(Number(conditionStartTime) + 7 * 60 * 60 * 1000)
        .tz('Asia/Bangkok')
        .toDate();
    }
  }
  if (node.type === NodeType.ISLAND) {
    res.course = node?.course;
    res.courseTitle = node?.course?.title;
    res.courseRequireJoin = node?.course?.requireJoin;
    res.courseRequireAttend = node?.course?.requireAttend;
    res.courseMeetingUrl = node?.course?.meetingUrl;
    res.courseMeetingPassword = node?.course?.meetingPassword;
    res.courseDescription = node?.course?.description;
    res.courseDocuments = node?.course?.documents;
    res.rules = node?.rules;
    res.departments = undefined;
    res.roles = undefined;
    res.employeeCodes = undefined;
    res.startTime = dateUTCToLocaltime(node?.course?.applyTime);
    res.endTime = dateUTCToLocaltime(node?.course?.expireTime);
    if (node.rules && node.rules?.length > 0) {
      const conditionEmployee = node.rules?.find(r => r.namespace === NodeRuleNamespace.NODE_EMPLOYEE)?.condition;
      if (conditionEmployee) {
        const splitCondition = conditionEmployee?.split(regSplitCondition);
        splitCondition?.forEach(c => {
          if (c.includes('department')) {
            res.departments = decayCondition(c);
          } else if (c.includes('role')) {
            res.roles = decayCondition(c);
          } else if (c.includes('code')) {
            res.employeeCodes = decayCondition(c);
          }
        });
      }
    }
  }
  if (node.type === NodeType.CREEP) {
    res.exam = node?.exam;
    res.rules = node?.rules;
    res.departments = undefined;
    res.roles = undefined;
    res.employeeCodes = undefined;
    res.examRequireJoin = node?.exam?.requireJoin;
    res.examDocuments = node?.exam?.documents;
    res.examMinPointToPass = node?.exam?.minPointToPass;
    res.examNumberOfQuestion = node.exam?.numberOfQuestion;
    res.examStrategy = node.exam?.examStrategy === ExamStrategyEnum.RANDOM;
    res.examPointStrategy = node.exam?.pointStrategy;
    res.examPoolStrategy = node.exam?.poolStrategy;
    res.examQuizzPools = node.exam?.quizzPools;
    res.examRules = node.exam?.rules;
    res.startTime = dateUTCToLocaltime(node?.exam?.applyTime);
    res.endTime = dateUTCToLocaltime(node?.exam?.expireTime);
    if (node.rules && node.rules?.length > 0) {
      const conditionEmployee = node.rules?.find(r => r.namespace === NodeRuleNamespace.NODE_EMPLOYEE)?.condition;
      if (conditionEmployee) {
        const splitCondition = conditionEmployee?.split(regSplitCondition);
        splitCondition?.forEach(c => {
          if (c.includes('department')) {
            res.departments = decayCondition(c);
          } else if (c.includes('role')) {
            res.roles = decayCondition(c);
          } else if (c.includes('code')) {
            res.employeeCodes = decayCondition(c);
          }
        });
      }
    }
    if (node.exam?.rules && node.exam?.rules.length > 0) {
      const conditionExam = node.exam.rules.find(r => r.namespace === NodeRuleNamespace.EXAM)?.condition;
      if (conditionExam) {
        const splitCondition = conditionExam?.split(regSplitCondition);
        splitCondition?.forEach(c => {
          if (c.includes('workingTime')) {
            res.examWorkingTime = extractNumbers(c)?.[0];
          } else if (c.includes('maxNumberOfTest')) {
            res.examMaxNumberOfTest = extractNumbers(c)?.[0];
          }
        });
      }
    }
  }
  return res;
};

export const gatherNodeItem = (node: INode[]): TCustomNode[] => {
  if (node) {
    const groupedByRootId: { [key: string | number]: TCustomNodeData[] } = node.reduce((acc, item) => {
      if (item.rootId && item.type !== NodeType.CONTAINER) {
        if (!acc[item.rootId]) {
          acc[item.rootId] = [];
        }
        acc[item.rootId].push(convertNodeItemToTCustomNodeData(item));
      }
      return acc;
    }, {});
    const result = node
      .filter(container => container.type === NodeType.ROOT || container.type === NodeType.CONTAINER)
      .map(
        (container): TCustomNode => ({
          id: container.id.toString(),
          dragHandle: `._${container.id}`,
          type:
            container.type === NodeType.CONTAINER
              ? groupedByRootId?.[container.id?.toString()]?.[0]?.type ?? NodeType.CONTAINER
              : container.type,
          position: {
            x: container.positionX || 0,
            y: container.positionY || 0,
          },
          data: {
            ...convertNodeItemToTCustomNodeData(container),
            items:
              container.id && groupedByRootId[container.id?.toString()]
                ? groupedByRootId[container.id?.toString()].sort((a, b) => (a.position || 0) - (b.position || 0))
                : [],
          },
        })
      );
    return result;
  }
  return [];
};

export const enrichEdgeItem = ({ nodes, edges, newEdges }: { nodes: TCustomNode[]; edges: Edge[]; newEdges: IEdge[] }): Edge[] => {
  const _edges: Edge[] = structuredClone(edges);
  nodes.forEach(node => {
    if (node.type === NodeType.ROOT) {
      const edge = newEdges.find(e => e.source.toString() === node.id.toString());
      if (edge) {
        _edges.push({
          id: `${edge.source}_${edge.target}`,
          source: edge.source.toString(),
          target: edge.target.toString(),
          type: edge.type,
          ...defaultConfigEdge,
        });
      }
    } else {
      if (node.data.items && node.data.items.length > 0) {
        node.data.items.forEach(childNode => {
          const edge = newEdges.find(e => e.source.toString() === childNode?.id?.toString());
          if (edge) {
            _edges.push({
              id: `${edge.source}_${edge.target}`,
              source: node.id.toString(),
              sourceHandle: edge.source.toString(),
              target: edge.target.toString(),
              type: edge.type,
              ...defaultConfigEdge,
            });
          }
        });
      }
    }
  });
  return _edges;
};

export const onUpdateNodeSuccess = ({
  newValue,
  nodeContainerParent,
  nodes,
  setNodes,
}: {
  newValue: TCustomNodeData;
  nodeContainerParent: TNodeExpand | undefined;
  nodes: TNodeExpand[];
  setNodes: (value: React.SetStateAction<TNodeExpand[]>) => void;
}) => {
  const newNode = [...nodes].map(n => {
    if (nodeContainerParent && n.id.toString() === nodeContainerParent.id.toString()) {
      return {
        ...n,
        data: {
          ...n.data,
          items: n.data.items?.map(i => {
            if (i.id?.toString() === newValue?.id?.toString()) {
              return {
                ...i,
                ...newValue,
                label: newValue?.label?.toUpperCase(),
              };
            }
            return i;
          }),
        },
      };
    }
    return n;
  });
  setNodes(newNode);
};

export const getTimestamp = (dateStr: string) => {
  const dateObject = new Date(dateStr);
  const timestamp = Math.floor(dateObject.getTime());
  return timestamp;
};

export function groupQuizzPoolsByCategory(quizzPools: IQuizzPool[]): TempCategory[] {
  const grouped = quizzPools.reduce((result, item) => {
    const categoryId = item.categoryId;
    const categoryName = item.categoryName || '';
    const tempId = categoryId?.toString() || '';
    if (!result[tempId]) {
      result[tempId] = {
        tempId,
        categoryId,
        categoryName,
        tempQuizzPools: [],
      };
    }
    result[tempId].tempQuizzPools.push(item);
    return result;
  }, {} as { [key: string]: TempCategory });
  return Object.values(grouped);
}

export const enrichConditionTime = ({ startTime, endTime }: { startTime?: Date; endTime?: Date }) => {
  const conditionsTime: string[] = [];
  if (startTime) {
    conditionsTime.push(`input.startTime > '${getTimestamp(formatDateTimeUtc(startTime, APP_LOCAL_DATETIME_FORMAT_2))}'`);
  }
  if (endTime) {
    conditionsTime.push(`input.endTime < '${getTimestamp(formatDateTimeUtc(endTime, APP_LOCAL_DATETIME_FORMAT_2))}'`);
  }
  return conditionsTime;
};

export const enrichConditionsDepartmentAndPosition = ({
  selectedDepartments,
  selectedPositions,
}: {
  selectedDepartments?: string[];
  selectedPositions?: string[];
}) => {
  const conditionsDepartmentAndPosition: string[] = [];
  if (selectedDepartments && selectedDepartments?.length > 0) {
    conditionsDepartmentAndPosition.push(`Arrays.asList(${arrToString(selectedDepartments)}).contains(input.department)`);
  }
  if (selectedPositions && selectedPositions?.length > 0) {
    conditionsDepartmentAndPosition.push(`Arrays.asList(${arrToString(selectedPositions)}).contains(input.role)`);
  }
  return conditionsDepartmentAndPosition;
};

export const enrichConditionsEmployee = ({ employeeCodes }: { employeeCodes?: string[] }) => {
  let conditionsEmployee: string | undefined;
  if (employeeCodes && employeeCodes?.length > 0) {
    conditionsEmployee = `Arrays.asList(${arrToString(employeeCodes)}).contains(input.code)`;
  }
  return conditionsEmployee;
};

export const enrichConditionExam = ({
  examWorkingTime,
  examMaxNumberOfTest,
}: {
  examWorkingTime?: number;
  examMaxNumberOfTest?: number;
}) => {
  const conditionExam: string[] = [];
  if (examWorkingTime) conditionExam.push(`input.workingTime == ${examWorkingTime}`);
  if (examMaxNumberOfTest) conditionExam.push(`input.maxNumberOfTest == ${examMaxNumberOfTest}`);
  return conditionExam;
};
