import { Box, Button, Flex, Switch, Textarea, TextInput, Text, LoadingOverlay, Loader, Tooltip } from '@mantine/core';
import React, { useContext, useMemo } from 'react';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { Trash } from '@phosphor-icons/react';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { useUpsertSeaMap } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpsertSeaMap';
import { useModalConfirm } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useModalConfirm';
import { confirmDeleteLabel, nodeContainerLabel, validateDeleteLabel } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { useDeleteNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDeleteNode';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';

interface DrawerCreateSeaMapProps {
  onClose: () => void;
  parentNodeId: string;
  parentItemId: string;
  parentNodeType: NodeType;
  initialValues: undefined;
}

interface DrawerUpdateSeaMapProps {
  onClose: () => void;
  initialValues: TCustomNodeData;
  updatetingNode: boolean;
  updateNode: (props: UpdateNodeParams) => Promise<void>;
}

export const DrawerUpsertSeaMap = (props: DrawerCreateSeaMapProps | DrawerUpdateSeaMapProps) => {
  const { onClose, initialValues } = props;
  let updatetingNode: boolean | undefined = undefined;
  let updateNode: ((props: UpdateNodeParams) => Promise<void>) | undefined = undefined;
  let parentNodeId, parentItemId, parentNodeType;
  if (initialValues) {
    updatetingNode = props.updatetingNode;
    updateNode = props.updateNode;
  } else {
    parentNodeId = props.parentNodeId;
    parentItemId = props.parentItemId;
    parentNodeType = props.parentNodeType;
  }

  const { edges, creatingNode, refetchNodes } = useContext(NavigatorUpsertContext);

  const { form, onSubmit, currentNodePosition, nodeContainerParent } = useUpsertSeaMap({
    initialValues,
    parentNodeId,
    parentItemId,
    parentNodeType,
    closeDrawer: onClose,
    updateNode,
  });

  const { deletingNode, deleteNode } = useDeleteNode();

  const { closeModal, openModal, modalConfirm } = useModalConfirm({
    loading: deletingNode,
    title: `Xóa ${nodeContainerLabel[NodeType.SEA_MAP]}`,
    content: confirmDeleteLabel({ nodeType: nodeContainerLabel[NodeType.SEA_MAP], nodeName: initialValues?.label }),
    onOk() {
      if (initialValues && initialValues.id) {
        const withDeleteNodeContainer = nodeContainerParent?.data.items && nodeContainerParent?.data.items?.length <= 1;
        const deletedIds = withDeleteNodeContainer ? [initialValues.id, nodeContainerParent.id] : [initialValues.id];
        deleteNode({
          ids: deletedIds,
          nodeType: initialValues?.type || NodeType.SEA_MAP,
          onSucceed() {
            refetchNodes();
            closeModal();
            onClose();
          },
          onFailed() {
            closeModal();
          },
        });
      }
    },
  });

  const hasChilds = useMemo(() => edges.find(e => e.sourceHandle === initialValues?.id?.toString()), [edges, initialValues]);

  return (
    <Box pos="relative">
      <LoadingOverlay zIndex={10} visible={creatingNode || updatetingNode} loaderProps={{ children: <Loader /> }} />
      <form onSubmit={form.onSubmit(onSubmit)}>
        <Flex direction="column" gap={20}>
          <TextInput
            maxLength={50}
            withAsterisk
            label="Tên Hải Trình"
            placeholder="Nhập tên"
            className="input-node-title"
            key={form.key('label')}
            {...form.getInputProps('label')}
          />
          <Textarea
            maxLength={256}
            withAsterisk
            label="Mô tả"
            placeholder="Nhập mô tả"
            key={form.key('description')}
            {...form.getInputProps('description')}
            rows={4}
            resize="none"
          />
          <Switch label="Khoá" defaultChecked={initialValues?.status} key={form.key('status')} {...form.getInputProps('status')} />
          <Box p={8} bd="1px solid #E5E7EB" style={{ borderRadius: '8px' }}>
            <Text c="#1F2A37" fw={600}>
              Vị trí {currentNodePosition}
            </Text>
          </Box>
          {initialValues ? (
            <Flex gap={12} justify="flex-end">
              {modalConfirm}
              <Tooltip
                hidden={!hasChilds}
                label={validateDeleteLabel({
                  nodeType: nodeContainerLabel[NodeType.SEA_MAP],
                  nodeChildName: nodeContainerLabel[NodeType.SEA_REGION],
                })}
              >
                <Button
                  loading={deletingNode}
                  disabled={updatetingNode || !!hasChilds}
                  onClick={openModal}
                  variant="outline"
                  color="#FA5252"
                >
                  <Trash size={16} />
                </Button>
              </Tooltip>
              <Button loading={updatetingNode} type="submit" variant="outline" color="#111928">
                Chỉnh sửa
              </Button>
            </Flex>
          ) : (
            <Flex gap={12} justify="flex-end">
              <Button disabled={creatingNode} onClick={onClose} variant="outline" color="#111928">
                Hủy
              </Button>
              <Button loading={creatingNode} type="submit">
                Tạo
              </Button>
            </Flex>
          )}
        </Flex>
      </form>
    </Box>
  );
};
