import {
  Box,
  Button,
  Flex,
  Loader,
  LoadingOverlay,
  Switch,
  Textarea,
  TextInput,
  ActionIcon,
  Select,
  ComboboxData,
  Skeleton,
  Tooltip,
} from '@mantine/core';
import { DateTimePicker } from '@mantine/dates';
import { Trash, Clock, CalendarBlank, CaretDown } from '@phosphor-icons/react';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { BtnSelectBanner } from 'app/pages/admin/Navigator/NavigatorUpsert/components/BtnSelectBanner';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import {
  confirmDeleteLabel,
  convertNodeItemToTCustomNodeData,
  nodeContainerLabel,
  validateDeleteLabel,
} from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { useDeleteNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDeleteNode';
import { useFetchNodeDetail } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchNodeDetail';
import { useModalConfirm } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useModalConfirm';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { useUpsertIslandArea } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpsertIslandArea';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import React, { useContext, useMemo, useRef, useState } from 'react';

interface DrawerCreateIslandAreaProps {
  onClose: () => void;
  parentNodeId: string;
  parentItemId: string;
  parentNodeType: NodeType;
  initialValues: undefined;
}

interface DrawerUpdateIslandAreaProps {
  onClose: () => void;
  initialValues: TCustomNodeData;
  updatetingNode: boolean;
  updateNode: (props: UpdateNodeParams) => Promise<void>;
}

const now = new Date();
const minTime = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;

export const DrawerIslandArea = (props: DrawerCreateIslandAreaProps | DrawerUpdateIslandAreaProps) => {
  const { initialValues, ...rest } = props;

  const { data, isLoading: isLoadingDetail } = useFetchNodeDetail(initialValues?.id);

  const initialValuesFull = useMemo(() => {
    if (initialValues?.id && data) {
      return {
        ...initialValues,
        ...convertNodeItemToTCustomNodeData(data),
      };
    }
    return initialValues;
  }, [initialValues, data]);

  if (isLoadingDetail)
    return (
      <Flex w="100%" gap={10} direction="column">
        {Array.from({ length: 3 }).map((_, i) => (
          <Skeleton key={i} w="100%" h="25" radius="8px" />
        ))}
      </Flex>
    );

  return <DrawerIslandAreaContent {...rest} initialValues={initialValuesFull as any} />;
};

const DrawerIslandAreaContent = (props: DrawerCreateIslandAreaProps | DrawerUpdateIslandAreaProps) => {
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

  const applyDateTime = useRef<HTMLInputElement>(null);

  const [banner, setbanner] = useState<{ url?: string; thumbUrl?: string }>({
    thumbUrl: initialValues?.erThumbUrl,
    url: initialValues?.thumbUrl,
  });

  const { edges, nodesRef, creatingNode, refetchNodes } = useContext(NavigatorUpsertContext);

  const { form, onSubmit, setParentRootId, parentRootId, nodeContainerParent } = useUpsertIslandArea({
    initialValues,
    parentNodeId,
    parentItemId,
    parentNodeType,
    closeDrawer: onClose,
    updateNode,
    banner,
  });

  const { deletingNode, deleteNode } = useDeleteNode();

  const { closeModal, openModal, modalConfirm } = useModalConfirm({
    loading: deletingNode,
    title: `Xóa ${nodeContainerLabel[NodeType.ISLAND_AREA]}`,
    content: confirmDeleteLabel({ nodeType: nodeContainerLabel[NodeType.ISLAND_AREA], nodeName: initialValues?.label }),
    onOk() {
      if (initialValues && initialValues.id) {
        const withDeleteNodeContainer = nodeContainerParent?.data.items && nodeContainerParent?.data.items?.length <= 1;
        const deletedIds = withDeleteNodeContainer ? [initialValues.id, nodeContainerParent.id] : [initialValues.id];
        deleteNode({
          ids: deletedIds,
          nodeType: initialValues?.type || NodeType.ISLAND_AREA,
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

  const parentRootOption = (): ComboboxData =>
    nodesRef.current
      .filter(n => n.type === NodeType.SEA_REGION)
      .map(n => ({
        label: n.label || '',
        value: n.id.toString(),
      }));

  const hasChilds = useMemo(() => edges.find(e => e.sourceHandle === initialValues?.id?.toString()), [edges, initialValues]);

  return (
    <Box pos="relative">
      <LoadingOverlay zIndex={10} visible={creatingNode || updatetingNode} loaderProps={{ children: <Loader /> }} />
      <form onSubmit={form.onSubmit(onSubmit)}>
        <Flex direction="column" gap={20}>
          <TextInput
            maxLength={50}
            withAsterisk
            label="Tên Quần Đảo"
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
          <BtnSelectBanner type={NodeType.ISLAND_AREA} banner={banner} setbanner={setbanner} />
          <Select
            w="100%"
            withAsterisk
            allowDeselect={false}
            disabled={!!initialValues?.id}
            searchable
            onChange={setParentRootId}
            value={parentRootId}
            data={parentRootOption()}
            label="Vùng biển"
            placeholder="Chọn vùng biển"
            rightSection={<CaretDown size={16} />}
          />
          <DateTimePicker
            w="100%"
            withAsterisk
            highlightToday
            withSeconds={false}
            label="Thời gian mở"
            placeholder="Chọn thời gian"
            valueFormat={APP_DATE_FORMAT}
            minDate={new Date()}
            rightSection={<CalendarBlank size={16} />}
            timeInputProps={{
              ref: applyDateTime,
              rightSection: (
                <ActionIcon variant="subtle" onClick={() => applyDateTime.current?.showPicker()}>
                  <Clock size={16} />
                </ActionIcon>
              ),
              minTime,
            }}
            key={form.key('startTime')}
            {...form.getInputProps('startTime')}
          />
          {initialValues ? (
            <Flex gap={12} justify="flex-end">
              {modalConfirm}
              <Tooltip
                hidden={!hasChilds}
                label={validateDeleteLabel({
                  nodeType: nodeContainerLabel[NodeType.ISLAND_AREA],
                  nodeChildName: `${nodeContainerLabel[NodeType.ISLAND]} và ${nodeContainerLabel[NodeType.CREEP]}`,
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
