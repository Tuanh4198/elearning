import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import {
  enrichConditionExam,
  enrichConditionsDepartmentAndPosition,
  enrichConditionsEmployee,
  enrichConditionTime,
  mergeConditonEmployee,
} from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IDocument } from 'app/shared/model/document.model';
import { NodeStatus } from 'app/shared/model/enumerations/node-status-enum.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { ExamStrategyEnum, INode, IQuizzPool, NodeMetafieldKey, NodeRuleNamespace, PoolStrategyEnum } from 'app/shared/model/node.model';
import { notiError, notiSuccess } from 'app/shared/notifications';
import axios from 'axios';
import React, { useContext } from 'react';
import { useState } from 'react';

export interface UpdateNodeParams {
  nodes: TCustomNodeData[];
  isSimpleUpdate?: boolean;
  nodeType?: NodeType;
  banner?: string;
  selectedDepartments?: string[];
  selectedPositions?: string[];
  employeeCodes?: string[];
  documents?: Array<IDocument>;
  quizzPools?: Array<IQuizzPool>;
  onSucceed: () => void;
  onFailed: () => void;
}

export const useUpdateNode = () => {
  const { nodesRef } = useContext(NavigatorUpsertContext);

  const [updatetingNode, setUpdatetingNode] = useState(false);

  const buildBody = ({ nodes, banner, selectedDepartments, selectedPositions, employeeCodes, documents }: Partial<UpdateNodeParams>) => {
    return [...(nodes || [])]?.map((node): INode => {
      const oldNode = nodesRef.current.find(nodeRef => nodeRef.id.toString() === node.id?.toString());
      let body: any = {
        id: node.id || '',
        flowId: node.flowId,
        rootId: node.rootId,
        type: node.type,
        status: node.status ? NodeStatus.INACTIVE : NodeStatus.ACTIVE,
        label: node?.label?.trim().toUpperCase(),
        positionX: node.positionX,
        positionY: node.positionY,
        width: node.width,
        height: node.height,
        metafields:
          oldNode?.metafields?.map(metafield => {
            if (metafield.key === NodeMetafieldKey.description) {
              return {
                ...metafield,
                value: node?.description?.trim(),
              };
            }
            if (metafield.key === NodeMetafieldKey.position) {
              return {
                ...metafield,
                value: node?.position?.toString(),
              };
            }
            return metafield;
          }) ?? [],
        rules: [],
      };
      if (node.type === NodeType.SEA_REGION) {
        const conditionsDepartmentAndPosition = enrichConditionsDepartmentAndPosition({ selectedDepartments, selectedPositions });
        const conditionsEmployee = enrichConditionsEmployee({ employeeCodes });
        body = {
          ...body,
          rules: node.rules?.map(r => ({
            ...r,
            condition: mergeConditonEmployee({ conditionsEmployee, conditionsDepartmentAndPosition }),
          })),
        };
      }
      if (node.type === NodeType.ISLAND_AREA) {
        const conditionsTime = enrichConditionTime({
          startTime: node.startTime,
        });
        body = {
          ...body,
          thumbUrl: banner,
          rules: node.rules?.map(r => ({
            ...r,
            condition: conditionsTime.join(' && '),
          })),
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
          ...body,
          thumbUrl: node.thumbUrl,
          rules: node.rules?.map(r => {
            if (r.namespace === NodeRuleNamespace.NODE_EMPLOYEE) {
              return {
                ...r,
                condition: mergeConditonEmployee({ conditionsEmployee, conditionsDepartmentAndPosition }),
              };
            }
            if (r.namespace === NodeRuleNamespace.NODE_TIME) {
              return {
                ...r,
                condition: conditionsTime.join(' && '),
              };
            }
            return r;
          }),
          course: {
            ...node.course,
            title: node.label,
            requireJoin: node.courseRequireJoin,
            requireAttend: node.courseRequireAttend,
            meetingUrl: node.courseMeetingUrl,
            meetingPassword: node.courseMeetingPassword,
            description: node.description,
            applyTime: node.startTime,
            expireTime: node.endTime,
            documents: documents?.map(({ id, ...rest }) => {
              if (typeof id === 'number') return { id, ...rest };
              return rest;
            }),
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
          ...body,
          thumbUrl: node.thumbUrl,
          rules: node.rules?.map(r => {
            if (r.namespace === NodeRuleNamespace.NODE_EMPLOYEE) {
              return {
                ...r,
                condition: mergeConditonEmployee({ conditionsEmployee, conditionsDepartmentAndPosition }),
              };
            }
            if (r.namespace === NodeRuleNamespace.NODE_TIME) {
              return {
                ...r,
                condition: conditionsTime.join(' && '),
              };
            }
            return r;
          }),
          exam: {
            ...node.exam,
            applyTime: node.startTime,
            expireTime: node.endTime,
            thumbUrl: node.thumbUrl,
            requireJoin: node.examRequireJoin,
            pointStrategy: node.examPointStrategy,
            minPointToPass: node.examMinPointToPass,
            numberOfQuestion: node.examPoolStrategy === PoolStrategyEnum.WEIGHT ? node.examNumberOfQuestion : node.examQuizzPools?.length,
            poolStrategy: node.examPoolStrategy,
            examStrategy: node.examStrategy ? ExamStrategyEnum.RANDOM : ExamStrategyEnum.ALL,
            rules: node.examRules?.map(r => {
              if (r.namespace === NodeRuleNamespace.EXAM) {
                return {
                  ...r,
                  condition: conditionExam.join(' && '),
                };
              }
              return r;
            }),
            documents: documents
              ?.filter(d => d.content != null)
              ?.map(({ id, metafields, ...rest }) => {
                const newMetafields = metafields != null ? metafields : [];
                if (typeof id === 'number') return { id, metafields: newMetafields, rules: [], ...rest };
                return {
                  ...rest,
                  metafields: newMetafields,
                  rules: [],
                };
              }),
            quizzPools: node.examQuizzPools?.map(({ metafields, ...rest }) => ({
              ...rest,
              metafields: metafields != null ? metafields : [],
            })),
          },
        };
      }
      return body;
    });
  };

  const updateNode = async ({
    nodes,
    isSimpleUpdate = false,
    nodeType,
    banner,
    selectedDepartments,
    selectedPositions,
    employeeCodes,
    documents,
    onSucceed,
    onFailed,
  }: UpdateNodeParams) => {
    setUpdatetingNode(true);
    try {
      const nodesRequest = buildBody({
        nodes,
        banner,
        selectedDepartments,
        selectedPositions,
        employeeCodes,
        documents,
      });
      if (nodeType === NodeType.ISLAND) {
        for (const { course, ...rest } of nodesRequest) {
          await axios.put(`/api/node-course/${rest.id}`, { course, node: rest });
        }
      } else if (nodeType === NodeType.CREEP) {
        for (const { exam, ...rest } of nodesRequest) {
          await axios.put(`/api/node-exam/${rest.id}`, { exam, node: rest });
        }
      } else {
        if (isSimpleUpdate) {
          await axios.put(`/api/nodes/${nodesRequest[0].id}`, nodesRequest[0]);
        } else {
          await axios.put(`/api/nodes-multiple`, { nodesRequest });
        }
      }
      const nodesRequestMapper: { [key: string | number]: INode } = nodesRequest.reduce((acc, node) => {
        if (node.id) {
          acc = { ...acc, [node.id]: node };
        }
        return acc;
      }, {});
      nodesRef.current = nodesRef.current.map(node => {
        if (nodesRequestMapper[node.id]) {
          return { ...node, ...nodesRequestMapper[node.id] };
        }
        return node;
      });
      onSucceed();
      notiSuccess({ message: `Cập nhật thành công` });
    } catch (error: any) {
      console.error('Update error: ', error);
      const errMsg = error?.response?.data?.errors?.message?.[0];
      notiError({
        message: (
          <>
            Sửa thất bại, vui lòng thử lại sau! <br />
            {errMsg}
          </>
        ),
      });
      onFailed();
    }
    setUpdatetingNode(false);
  };

  return {
    updateNode,
    updatetingNode,
  };
};
