import { isNotEmpty, useForm } from '@mantine/form';
import { useDisclosure } from '@mantine/hooks';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { errorPermission, onUpdateNodeSuccess } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { notiWarning } from 'app/shared/notifications';
import { useContext, useMemo, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

interface UseCreateSeaRegionProps {
  initialValues: undefined;
  parentNodeId: string;
  parentItemId: string;
  parentNodeType: NodeType;
  closeDrawer: () => void;
}

interface UseUpdateSeaRegionProps {
  initialValues: TCustomNodeData;
  closeDrawer: () => void;
  updateNode?: (props: UpdateNodeParams) => Promise<void>;
}

export const useUpsertSeaRegion = (props: UseCreateSeaRegionProps | UseUpdateSeaRegionProps) => {
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

  const onSubmit = ({
    value,
    employeeCodes,
    selectedDepartments,
    selectedPositions,
  }: {
    value: TCustomNodeData;
    selectedDepartments?: string[];
    selectedPositions?: string[];
    employeeCodes?: string[];
  }) => {
    if (validatePermission(selectedDepartments, selectedPositions, employeeCodes)) return;

    if (initialValues == null && parentRootId) {
      if (parentRootId.toString() !== parentItemId?.toString()) {
        value = {
          ...value,
          position: (nodes.find(n => n?.data?.rootId?.toString() === parentRootId.toString())?.data?.items?.length || 0) + 1,
        };
      }
      createNodes({
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
        type: NodeType.SEA_REGION,
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
