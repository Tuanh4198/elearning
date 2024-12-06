import { isNotEmpty, useForm } from '@mantine/form';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { onUpdateNodeSuccess } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { useContext, useMemo, useState } from 'react';
import { v4 as uuidv4 } from 'uuid';

interface UseCreateIslandAreaProps {
  initialValues: undefined;
  banner?: { url?: string; thumbUrl?: string };
  parentNodeId: string;
  parentItemId: string;
  parentNodeType: NodeType;
  closeDrawer: () => void;
}

interface UseUpdateIslandAreaProps {
  initialValues: TCustomNodeData;
  banner?: { url?: string; thumbUrl?: string };
  closeDrawer: () => void;
  updateNode?: (props: UpdateNodeParams) => Promise<void>;
}

export const useUpsertIslandArea = (props: UseCreateIslandAreaProps | UseUpdateIslandAreaProps) => {
  const { initialValues, banner, closeDrawer } = props;
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

  const onSubmit = (value: TCustomNodeData) => {
    if (initialValues == null && parentRootId) {
      if (parentRootId.toString() !== parentItemId?.toString()) {
        value = {
          ...value,
          position: (nodes.find(n => n?.data?.rootId?.toString() === parentRootId.toString())?.data?.items?.length || 0) + 1,
        };
      }
      createNodes({
        banner: banner?.url,
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
          banner: banner?.url,
          nodes: [value],
          isSimpleUpdate: true,
          nodeType: value.type,
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
        type: NodeType.ISLAND_AREA,
      };
  }, [initialValues, currentNodePosition]);

  const form = useForm<TCustomNodeData>({
    mode: 'uncontrolled',
    initialValues: _initialValues,
    validate: {
      label: isNotEmpty('Tên bắt buộc nhập'),
      description: isNotEmpty('Mô tả bắt buộc nhập'),
      startTime: isNotEmpty('Thời gian mở bắt buộc nhập'),
    },
  });

  return {
    form,
    onSubmit,
    setParentRootId,
    parentRootId,
    currentNodePosition,
    nodeContainerParent,
  };
};
