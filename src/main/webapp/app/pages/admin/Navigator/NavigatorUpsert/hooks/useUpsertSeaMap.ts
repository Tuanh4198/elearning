import { isNotEmpty, useForm } from '@mantine/form';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { onUpdateNodeSuccess } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { notiWarning } from 'app/shared/notifications';
import { useContext, useMemo } from 'react';
import { v4 as uuidv4 } from 'uuid';

interface UseUpsertSeaMapProps {
  initialValues?: TCustomNodeData;
  parentNodeId: string;
  parentItemId: string;
  parentNodeType: NodeType;
  closeDrawer: () => void;
  updateNode?: (props: UpdateNodeParams) => Promise<void>;
}

export const useUpsertSeaMap = ({
  initialValues,
  parentNodeId,
  parentItemId,
  parentNodeType,
  closeDrawer,
  updateNode,
}: UseUpsertSeaMapProps) => {
  const { setCreatedNodeId, createNodes, nodes, setNodes } = useContext(NavigatorUpsertContext);

  const onSubmit = (value: TCustomNodeData) => {
    if (initialValues) {
      const existNodeContainer = nodes.find(n => n.data.id?.toString() === value?.rootId?.toString());
      // Validate unix node label
      if (
        !existNodeContainer ||
        (existNodeContainer.data.items &&
          existNodeContainer.data.items
            .filter(i => i.id !== value.id)
            .some(i => i?.label?.toUpperCase() === value?.label?.trim().toUpperCase()))
      ) {
        notiWarning({ message: `Tên hải trình "${value?.label?.toUpperCase()}" đã tồn tại, vui lòng nhập tên mới!` });
        return;
      }
      updateNode &&
        updateNode({
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
    } else {
      createNodes({
        nodeDraff: value,
        parentNodeId,
        parentItemId,
        parentNodeType,
        onAddSucceed(item) {
          setCreatedNodeId(item.id);
          closeDrawer();
        },
        onAddFailed() {
          closeDrawer();
        },
      });
    }
  };

  const nodeContainerParent = useMemo(
    () =>
      nodes.find(i =>
        initialValues ? i.data.id?.toString() === initialValues.rootId?.toString() : i.data.rootId?.toString() === parentNodeId?.toString()
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
        type: NodeType.SEA_MAP,
      };
  }, [initialValues, currentNodePosition]);

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
    currentNodePosition,
    nodeContainerParent,
  };
};
