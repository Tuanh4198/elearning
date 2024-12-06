import {
  convertNodeItemToTCustomNodeData,
  defaultRule,
  enrichConditionExam,
  enrichConditionsDepartmentAndPosition,
  enrichConditionsEmployee,
  enrichConditionTime,
  enrichEdgeItem,
  gatherNodeItem,
  maxItemNodeSeaMap,
  mergeConditonEmployee,
  nodeWith,
  renderLabelContainer,
} from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { TCustomNodeData, TNodeExpand } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IDocument } from 'app/shared/model/document.model';
import { IEdge } from 'app/shared/model/edge.model';
import { NodeStatus } from 'app/shared/model/enumerations/node-status-enum.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { ExamStrategyEnum, INode, IQuizzPool, NodeMetafieldKey, NodeRuleNamespace, PoolStrategyEnum } from 'app/shared/model/node.model';
import { notiError, notiWarning } from 'app/shared/notifications';
import axios, { AxiosResponse } from 'axios';
import React, { MutableRefObject } from 'react';
import { useState } from 'react';
import { Edge, useReactFlow } from 'reactflow';

export interface AddNodeRequest {
  nodeDraff: TCustomNodeData;
  parentNodeId: string;
  parentItemId: string;
  parentNodeType?: NodeType;
  onAddSucceed: (item: TCustomNodeData) => void;
  onAddFailed: () => void;
  banner?: string;
  selectedDepartments?: string[];
  selectedPositions?: string[];
  employeeCodes?: string[];
  documents?: Array<IDocument>;
  quizzPools?: Array<IQuizzPool>;
}

interface NodeBodyRequest {
  node: TCustomNodeData;
  banner?: string;
  selectedDepartments?: string[];
  selectedPositions?: string[];
  employeeCodes?: string[];
  documents?: Array<IDocument>;
  quizzPools?: Array<IQuizzPool>;
}

interface UseCreateNodesProps {
  nodes: TNodeExpand[];
  edges: Edge[];
  setNodes: React.Dispatch<React.SetStateAction<TNodeExpand[]>>;
  setEdges: React.Dispatch<React.SetStateAction<Edge[]>>;
  reactFlowRef: MutableRefObject<HTMLDivElement | null>;
  nodesRef: React.MutableRefObject<INode[]>;
}

export const useCreateNodes = ({ nodes, edges, setNodes, setEdges, reactFlowRef, nodesRef }: UseCreateNodesProps) => {
  const { project, getViewport } = useReactFlow();

  const [creatingNode, setCreatingNode] = useState(false);

  const createEdgesAPI = async (value: IEdge) => {
    const res = await axios.post<IEdge>(`/api/edges`, value);
    return res.data;
  };

  const buildBody = ({ node, banner, selectedDepartments, selectedPositions, employeeCodes, documents, quizzPools }: NodeBodyRequest) => {
    let body: any = {
      flowId: -1,
      rootId: node.rootId,
      type: node.type,
      status: node.status ? NodeStatus.INACTIVE : NodeStatus.ACTIVE,
      label: node?.label?.trim().toUpperCase(),
      positionX: node.positionX,
      positionY: node.positionY,
      width: nodeWith,
      height: node.type !== NodeType.CONTAINER && node.type !== NodeType.ROOT ? 125 : 0,
      metafields: [
        {
          key: NodeMetafieldKey.description,
          value: node?.description?.trim(),
        },
        {
          key: NodeMetafieldKey.position,
          value: node.position,
        },
      ],
    };
    if (node.type === NodeType.SEA_REGION) {
      const conditionsDepartmentAndPosition = enrichConditionsDepartmentAndPosition({ selectedDepartments, selectedPositions });
      const conditionsEmployee = enrichConditionsEmployee({ employeeCodes });
      body = {
        ...body,
        rules: [
          {
            ...defaultRule,
            namespace: NodeRuleNamespace.NODE_EMPLOYEE,
            condition: mergeConditonEmployee({ conditionsEmployee, conditionsDepartmentAndPosition }),
          },
        ],
      };
    }
    if (node.type === NodeType.ISLAND_AREA) {
      const conditionsTime = enrichConditionTime({
        startTime: node.startTime,
      });
      body = {
        ...body,
        thumbUrl: banner,
        rules: [
          {
            ...defaultRule,
            namespace: NodeRuleNamespace.NODE_TIME,
            condition: conditionsTime.join(' && '),
          },
        ],
      };
    }
    if (node.type === NodeType.ISLAND) {
      const conditionsDepartmentAndPosition = enrichConditionsDepartmentAndPosition({ selectedDepartments, selectedPositions });
      const conditionsEmployee = enrichConditionsEmployee({ employeeCodes });
      const conditionsTime = enrichConditionTime({
        startTime: node.startTime,
        endTime: node.endTime,
      });
      body = {
        node: {
          ...body,
          thumbUrl: node.thumbUrl,
          rules: [
            {
              ...defaultRule,
              namespace: NodeRuleNamespace.NODE_EMPLOYEE,
              condition: mergeConditonEmployee({ conditionsEmployee, conditionsDepartmentAndPosition }),
            },
            {
              ...defaultRule,
              namespace: NodeRuleNamespace.NODE_TIME,
              condition: conditionsTime.join(' && '),
            },
          ],
        },
        course: {
          categoryId: '',
          requireJoin: node.courseRequireJoin ?? false,
          requireAttend: node.courseRequireAttend ?? false,
          meetingUrl: node.courseMeetingUrl ?? '',
          meetingPassword: node.courseMeetingPassword ?? '',
          applyTime: node.startTime ?? '',
          expireTime: node.endTime ?? '',
          documents: documents?.map(({ id, ...rest }) => rest),
        },
      };
    }
    if (node.type === NodeType.CREEP) {
      const conditionsDepartmentAndPosition = enrichConditionsDepartmentAndPosition({ selectedDepartments, selectedPositions });
      const conditionsEmployee = enrichConditionsEmployee({ employeeCodes });
      const conditionsTime = enrichConditionTime({
        startTime: node.startTime,
        endTime: node.endTime,
      });
      const conditionExam = enrichConditionExam({ examWorkingTime: node.examWorkingTime, examMaxNumberOfTest: node.examMaxNumberOfTest });
      body = {
        node: {
          ...body,
          thumbUrl: node.thumbUrl,
          rules: [
            {
              ...defaultRule,
              namespace: NodeRuleNamespace.NODE_EMPLOYEE,
              condition: mergeConditonEmployee({ conditionsEmployee, conditionsDepartmentAndPosition }),
            },
            {
              ...defaultRule,
              namespace: NodeRuleNamespace.NODE_TIME,
              condition: conditionsTime.join(' && '),
            },
          ],
        },
        exam: {
          courseId: '',
          applyTime: node.startTime,
          expireTime: node.endTime,
          thumbUrl: node.thumbUrl,
          requireJoin: node.examRequireJoin ?? false,
          pointStrategy: node.examPointStrategy,
          minPointToPass: node.examMinPointToPass,
          numberOfQuestion: node.examPoolStrategy === PoolStrategyEnum.WEIGHT ? node.examNumberOfQuestion : quizzPools?.length,
          poolStrategy: node.examPoolStrategy,
          examStrategy: node.examStrategy ? ExamStrategyEnum.RANDOM : ExamStrategyEnum.ALL,
          rules: [
            {
              ...defaultRule,
              namespace: NodeRuleNamespace.EXAM,
              condition: conditionExam.join(' && '),
            },
          ],
          documents: documents?.map(({ id, ...rest }) => ({ ...rest, rules: [] })) ?? [],
          quizzPools: quizzPools?.map(({ id, metafields, ...rest }) => ({ ...rest, metafields: metafields != null ? metafields : [] })),
        },
      };
    }
    return body;
  };

  const createNodesAPI = async (request: NodeBodyRequest) => {
    const body = buildBody(request);
    let res: AxiosResponse<INode, any>;
    if (request.node.type === NodeType.ISLAND) {
      res = await axios.post<INode>(`/api/node-course`, body);
    } else if (request.node.type === NodeType.CREEP) {
      res = await axios.post<INode>(`/api/node-exam`, body);
    } else {
      res = await axios.post<INode>(`/api/nodes`, body);
    }
    const resDetail = await axios.get<INode>(`/api/nodes/${res?.data?.id}`);
    return resDetail.data;
  };

  const validateMaxItem = ({ existNodeContainer, onAddFailed }: { existNodeContainer: TNodeExpand; onAddFailed: () => void }) => {
    if (
      existNodeContainer.type === NodeType.SEA_MAP &&
      existNodeContainer.data.items &&
      existNodeContainer.data.items?.length >= maxItemNodeSeaMap
    ) {
      onAddFailed();
      notiWarning({ message: `Số lượng hải trình đã đạt tối đa là ${maxItemNodeSeaMap} và không thể tạo thêm được nữa!` });
      setCreatingNode(false);
      return true;
    }
    return false;
  };

  const validateUnixNodeLabel = ({ existNodeContainer, nodeDraff }: { existNodeContainer: TNodeExpand; nodeDraff: TCustomNodeData }) => {
    if (
      existNodeContainer.data.items &&
      existNodeContainer.data.items.some(i => i?.label?.toUpperCase() === nodeDraff?.label?.trim().toUpperCase())
    ) {
      notiWarning({ message: `Tên hải trình "${nodeDraff.label}" đã tồn tại, vui lòng nhập tên mới!` });
      setCreatingNode(false);
      return true;
    }
    return false;
  };

  const createNodes = async ({
    nodeDraff,
    parentNodeId,
    parentItemId,
    parentNodeType,
    onAddSucceed,
    onAddFailed,
    banner,
    selectedDepartments,
    selectedPositions,
    employeeCodes,
    documents,
    quizzPools,
  }: AddNodeRequest) => {
    let newNode = [...nodes];
    let newEdge: IEdge | undefined = undefined;
    let newNodeConatiner: INode | undefined = undefined;
    let newNodeAdded: TCustomNodeData | undefined = undefined;
    let currentNodeContainer: TNodeExpand | undefined = undefined;
    try {
      setCreatingNode(true);
      const existEdge = edges.find(e =>
        parentNodeType === NodeType.ROOT
          ? e.source?.toString() === parentNodeId?.toString()
          : e.sourceHandle?.toString() === parentItemId?.toString()
      );
      const existNodeContainer = nodes.find(n =>
        parentNodeType === NodeType.ROOT
          ? n.data.rootId?.toString() === parentNodeId.toString()
          : n.data.rootId?.toString() === parentItemId.toString()
      );
      // update node item when current node container exist
      if (existNodeContainer && existEdge) {
        // Validate max item on node type SEARMAP
        if (validateMaxItem({ existNodeContainer, onAddFailed })) return;
        // Validate unix node label
        if (validateUnixNodeLabel({ existNodeContainer, nodeDraff })) return;
        // Request api add node item
        const newNodeItem = await createNodesAPI({
          node: { ...nodeDraff, rootId: existEdge.target },
          banner,
          selectedDepartments,
          selectedPositions,
          employeeCodes,
          documents,
          quizzPools,
        });
        currentNodeContainer = existNodeContainer;
        newNodeAdded = convertNodeItemToTCustomNodeData(newNodeItem);
        newNode = newNode.map(n => {
          if (newNodeAdded?.rootId?.toString() === n.id.toString() && newNodeAdded) {
            return {
              ...n,
              data: {
                ...n.data,
                items: [...(n.data.items || []), newNodeAdded],
              },
            };
          }
          return n;
        });
        nodesRef.current = [...nodesRef.current, newNodeItem];
      }
      // add node item and node container when node container not exist
      else {
        // init coordinate node container
        const wrapperBounds = reactFlowRef?.current?.getBoundingClientRect();
        const screenCenter = {
          x: (wrapperBounds?.left || 0) + (wrapperBounds?.width || 0) / 2,
          y: (wrapperBounds?.top || 0) + (wrapperBounds?.height || 0) / 2,
        };
        const canvasCenter = project(screenCenter);
        const { x, y } = canvasCenter;
        // Request api add node container
        newNodeConatiner = await createNodesAPI({
          node: {
            label: renderLabelContainer(nodeDraff.type),
            description: renderLabelContainer(nodeDraff.type),
            position: 0,
            status: true,
            type: NodeType.CONTAINER,
            rootId: parentNodeType === NodeType.ROOT ? parentNodeId : parentItemId,
            positionX: x,
            positionY: y,
          },
        });
        // Request api add edge
        newEdge = await createEdgesAPI({
          source: parentNodeType === NodeType.ROOT ? parentNodeId : parentItemId,
          target: newNodeConatiner.id,
          type: 'default',
        });
        // Request api add node item
        const newNodeItem = await createNodesAPI({
          node: {
            ...nodeDraff,
            rootId: newNodeConatiner.id,
            label: nodeDraff?.label?.trim().toUpperCase(),
          },
          banner,
          selectedDepartments,
          selectedPositions,
          employeeCodes,
          documents,
          quizzPools,
        });
        // add new edge to state
        if (newEdge) {
          const newSdges = enrichEdgeItem({ nodes, edges, newEdges: [newEdge] });
          setEdges(newSdges);
        }
        // build local new node container and item
        if (newNodeConatiner.id) {
          newNodeAdded = convertNodeItemToTCustomNodeData(newNodeItem);
          currentNodeContainer = gatherNodeItem([newNodeConatiner, newNodeItem])[0];
          newNode = [...nodes, currentNodeContainer];
        }
        nodesRef.current = [...nodesRef.current, newNodeConatiner, newNodeItem];
      }
      // view new node
      if (newNodeAdded) {
        setNodes(newNode);
        onAddSucceed(newNodeAdded);
      }
      setCreatingNode(false);
    } catch (error: any) {
      // add new edge to state
      if (newEdge) {
        const newSdges = enrichEdgeItem({ nodes, edges, newEdges: [newEdge] });
        setEdges(newSdges);
      }
      // build local new node container and item
      if (newNodeConatiner?.id) {
        currentNodeContainer = gatherNodeItem([newNodeConatiner])[0];
        newNode = [...nodes, currentNodeContainer];
        nodesRef.current = [...nodesRef.current, newNodeConatiner];
        setNodes(newNode);
      }
      setCreatingNode(false);
      console.error('Create error: ', error);
      const errMsg = error?.response?.data?.errors?.message?.[0];
      notiError({
        message: (
          <>
            Tạo thất bại, vui lòng thử lại sau! <br />
            {errMsg}
          </>
        ),
      });
    }
  };

  return {
    createNodes,
    creatingNode,
  };
};
