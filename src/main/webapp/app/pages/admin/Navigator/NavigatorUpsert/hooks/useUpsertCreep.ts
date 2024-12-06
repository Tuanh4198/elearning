import { isNotEmpty, useForm } from '@mantine/form';
import { useDisclosure } from '@mantine/hooks';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { errorPermission, errorQuizzPool, onUpdateNodeSuccess } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IDocument } from 'app/shared/model/document.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { IQuizzPool, PointStrategyEnum, PoolStrategyEnum } from 'app/shared/model/node.model';
import { notiWarning } from 'app/shared/notifications';
import { useContext, useMemo, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

interface UseCreateCreepProps {
  initialValues: undefined;
  parentNodeId?: string;
  parentItemId?: string;
  parentNodeType?: NodeType;
  closeDrawer: () => void;
}

interface UseUpdateCreepProps {
  initialValues: TCustomNodeData;
  closeDrawer: () => void;
  updateNode?: (props: UpdateNodeParams) => Promise<void>;
}

export const useUpsertCreep = (props: UseCreateCreepProps | UseUpdateCreepProps) => {
  const { initialValues, closeDrawer } = props;
  let updateNode: ((props: UpdateNodeParams) => Promise<void>) | undefined = undefined;
  let parentNodeId, parentItemId, parentNodeType;
  if (initialValues == null) {
    parentNodeId = props.parentNodeId;
    parentItemId = props.parentItemId;
    parentNodeType = props.parentNodeType;
  } else {
    updateNode = props.updateNode;
  }

  const { nodesRef, setCreatedNodeId, createNodes, nodes, setNodes } = useContext(NavigatorUpsertContext);

  const [parentRootId, setParentRootId] = useState<string | null>(
    initialValues
      ? nodesRef.current.find(n => n.id?.toString() === initialValues.rootId?.toString())?.rootId?.toString()
      : parentItemId?.toString()
  );

  const validatePermission = (selectedDepartments?: string[], selectedPositions?: string[], employeeCodes?: string[]) => {
    if (isAssignAllEmployee) return false;
    if (
      (!selectedDepartments || selectedDepartments?.length <= 0) &&
      (!selectedPositions || selectedPositions.length <= 0) &&
      (!employeeCodes || employeeCodes.length <= 0)
    ) {
      const msg = 'Vui lòng chọn phân quyền cho vùng biển';
      notiWarning({ message: msg });
      form.setErrors({ [errorPermission]: msg });
      return true;
    }
    form.clearFieldError(errorPermission);
    return false;
  };

  const validateEndTime = (value, values) => {
    if (value && new Date(value) <= new Date(values.startTime)) {
      return 'Thời gian đóng phải lớn hơn thời gian mở';
    }
    return null;
  };

  const validateDocuments = (documents?: Array<IDocument>) => {
    const newDocs = documents?.filter(i => i.content != null || i.content !== '');
    return newDocs ?? [];
  };

  const validateQuizzPools = (quizzPools?: Array<IQuizzPool>) => {
    if (!quizzPools || quizzPools.length <= 0 || quizzPools?.some(i => i.categoryId == null || i.sourceId == null || i.sourceId === '')) {
      const msg = 'Vui lòng chọn danh sách câu hỏi';
      notiWarning({ message: msg });
      form.setErrors({ [errorQuizzPool]: msg });
      return true;
    }
    form.clearFieldError(errorQuizzPool);
    return false;
  };

  const onSubmit = ({
    value,
    employeeCodes,
    selectedDepartments,
    selectedPositions,
    documents,
    quizzPools,
  }: {
    value: TCustomNodeData;
    selectedDepartments?: string[];
    selectedPositions?: string[];
    employeeCodes?: string[];
    documents?: Array<IDocument>;
    quizzPools?: Array<IQuizzPool>;
  }) => {
    if (validatePermission(selectedDepartments, selectedPositions, employeeCodes)) return;

    if (validateQuizzPools(quizzPools)) return;

    const newDocuments = validateDocuments(documents);

    if (initialValues == null && parentRootId) {
      if (parentRootId.toString() !== parentItemId?.toString()) {
        value = {
          ...value,
          position: (nodes.find(n => n?.data?.rootId?.toString() === parentRootId.toString())?.data?.items?.length || 0) + 1,
        };
      }
      createNodes({
        quizzPools,
        documents: newDocuments,
        selectedDepartments: isAssignAllEmployee ? undefined : selectedDepartments,
        selectedPositions: isAssignAllEmployee ? undefined : selectedPositions,
        employeeCodes: isAssignAllEmployee ? undefined : employeeCodes,
        nodeDraff: value,
        parentNodeId,
        parentItemId: parentRootId,
        parentNodeType,
        onAddSucceed(item) {
          setCreatedNodeId(item.id);
          closeDrawer();
        },
        onAddFailed() {
          closeDrawer();
        },
      });
    } else {
      updateNode &&
        updateNode({
          nodes: [value],
          isSimpleUpdate: true,
          nodeType: value.type,
          quizzPools,
          documents: newDocuments,
          selectedDepartments: isAssignAllEmployee ? undefined : selectedDepartments,
          selectedPositions: isAssignAllEmployee ? undefined : selectedPositions,
          employeeCodes: isAssignAllEmployee ? undefined : employeeCodes,
          onSucceed() {
            onUpdateNodeSuccess({
              newValue: value,
              nodeContainerParent,
              nodes,
              setNodes,
            });
          },
          onFailed() {},
        });
    }
  };

  const nodeContainerParent = useMemo(
    () =>
      nodes.find(i =>
        initialValues ? i.data.id?.toString() === initialValues.rootId?.toString() : i.data.rootId?.toString() === parentItemId?.toString()
      ),
    [nodes]
  );

  const currentNodePosition = useMemo(
    () => (initialValues ? initialValues?.position : nodeContainerParent?.data.items ? nodeContainerParent?.data.items?.length + 1 : 1),
    [initialValues, nodeContainerParent?.data.items]
  );

  const _initialValues = useMemo((): TCustomNodeData => {
    if (initialValues) return initialValues;
    else
      return {
        id: uuidv4(),
        label: '',
        rootId: '',
        status: false,
        description: '',
        position: currentNodePosition,
        type: NodeType.CREEP,
        examRequireJoin: false,
        examPointStrategy: PointStrategyEnum.PERCENTAGE,
        examPoolStrategy: PoolStrategyEnum.WEIGHT,
        examNumberOfQuestion: undefined,
        examMaxNumberOfTest: undefined,
        examMinPointToPass: undefined,
      };
  }, [initialValues, currentNodePosition]);

  const [isAssignAllEmployee, { toggle: toggleIsAssignAllEmployee }] = useDisclosure(
    initialValues ? !initialValues?.departments && !initialValues?.roles && !initialValues?.employeeCodes : false
  );

  const form = useForm<TCustomNodeData>({
    mode: 'uncontrolled',
    initialValues: _initialValues,
    validate: {
      label: isNotEmpty('Tên bắt buộc nhập'),
      description: isNotEmpty('Mô tả bắt buộc nhập'),
      startTime: isNotEmpty('Thời gian mở bắt buộc nhập'),
      endTime: validateEndTime,
      thumbUrl: isNotEmpty('Hình ảnh đảo bắt buộc nhập'),
      // examMaxNumberOfTest: isNotEmpty('Số lần làm tối đa bắt buộc nhập'),
      examMinPointToPass: isNotEmpty('Điểm tối thiểu bắt buộc nhập'),
      examPointStrategy: isNotEmpty('Cách chấm điểm bắt buộc nhập'),
      examWorkingTime: isNotEmpty('Thời gian làm bài bắt buộc nhập'),
    },
  });

  return {
    form,
    onSubmit,
    setParentRootId,
    parentRootId,
    currentNodePosition,
    nodeContainerParent,
    toggleIsAssignAllEmployee,
    isAssignAllEmployee,
  };
};
