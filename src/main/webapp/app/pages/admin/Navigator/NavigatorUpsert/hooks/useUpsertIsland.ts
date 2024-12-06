import { isNotEmpty, useForm } from '@mantine/form';
import { useDisclosure } from '@mantine/hooks';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { errorDocument, errorPermission, onUpdateNodeSuccess } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IDocument } from 'app/shared/model/document.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { notiWarning } from 'app/shared/notifications';
import { useContext, useEffect, useMemo, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

interface UseCreateIslandProps {
  initialValues: undefined;
  parentNodeId?: string;
  parentItemId?: string;
  parentNodeType?: NodeType;
  closeDrawer: () => void;
}

interface UseUpdateIslandProps {
  initialValues: TCustomNodeData;
  closeDrawer: () => void;
  updateNode?: (props: UpdateNodeParams) => Promise<void>;
}

export const useUpsertIsland = (props: UseCreateIslandProps | UseUpdateIslandProps) => {
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

  const validateDocuments = (documents?: Array<IDocument>) => {
    if (!documents || documents?.some(i => i.content == null || i.content === '' || i.rules == null)) {
      const msg = 'Vui lòng chọn đủ nội dung đào tạo và thời gian học tối thiểu';
      notiWarning({ message: msg });
      form.setErrors({ [errorDocument]: msg });
      return true;
    }
    form.clearFieldError(errorDocument);
    return false;
  };

  const validateEndTime = (value, values) => {
    if (value && new Date(value) <= new Date(values.startTime)) {
      return 'Thời gian đóng phải lớn hơn thời gian mở';
    }
    return null;
  };

  const onSubmit = ({
    value,
    employeeCodes,
    selectedDepartments,
    selectedPositions,
    documents,
  }: {
    value: TCustomNodeData;
    selectedDepartments?: string[];
    selectedPositions?: string[];
    employeeCodes?: string[];
    documents?: Array<IDocument>;
  }) => {
    if (validatePermission(selectedDepartments, selectedPositions, employeeCodes)) return;

    if (validateDocuments(documents)) return;

    if (initialValues == null && parentRootId) {
      if (parentRootId.toString() !== parentItemId?.toString()) {
        value = {
          ...value,
          position: (nodes.find(n => n?.data?.rootId?.toString() === parentRootId.toString())?.data?.items?.length || 0) + 1,
        };
      }
      createNodes({
        documents,
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
          documents,
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
        type: NodeType.ISLAND,
      };
  }, [initialValues, currentNodePosition]);

  const [isUseZoom, { toggle: toggleIsUseZoom }] = useDisclosure(!!initialValues?.courseMeetingUrl);

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
      courseMeetingUrl: isUseZoom ? isNotEmpty('Mô tả bắt buộc nhập') : undefined,
    },
  });

  useEffect(() => {
    if (!isUseZoom) {
      form.setFieldValue('courseMeetingUrl', undefined);
      form.setFieldValue('courseMeetingPassword', undefined);
    }
  }, [isUseZoom]);

  return {
    form,
    onSubmit,
    setParentRootId,
    parentRootId,
    currentNodePosition,
    nodeContainerParent,
    toggleIsUseZoom,
    isUseZoom,
    toggleIsAssignAllEmployee,
    isAssignAllEmployee,
  };
};
