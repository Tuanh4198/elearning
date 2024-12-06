import React, { useContext, useMemo, useRef, useState } from 'react';
import { Box, LoadingOverlay, Loader, Tabs, Flex, Button, Text, Skeleton, Tooltip, Switch } from '@mantine/core';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { useUpsertSeaRegion } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpsertSeaRegion';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { SeaRegionInformation } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaRegion/SeaRegionInformation';
import { SelectPermission } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SelectPermission';
import {
  confirmDeleteLabel,
  convertNodeItemToTCustomNodeData,
  errorPermission,
  nodeContainerLabel,
  validateDeleteLabel,
} from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { useDeleteNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDeleteNode';
import { useModalConfirm } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useModalConfirm';
import { Trash } from '@phosphor-icons/react';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { useFetchNodeDetail } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchNodeDetail';

enum TabTypeEnum {
  INFORMATION = 'INFORMATION',
  PERMISSION = 'PERMISSION',
}

interface DrawerUpdateSeaRegionProps {
  onClose: () => void;
  initialValues: TCustomNodeData;
  updatetingNode: boolean;
  updateNode: (props: UpdateNodeParams) => Promise<void>;
}

export const DrawerUpdateSeaRegion = (props: DrawerUpdateSeaRegionProps) => {
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

  return <DrawerUpdateSeaRegionContent {...rest} initialValues={initialValuesFull as any} />;
};

const DrawerUpdateSeaRegionContent = ({ onClose, initialValues, updatetingNode, updateNode }: DrawerUpdateSeaRegionProps) => {
  const selectDepartmentRef = useRef<any>();
  const selectedPositionsRef = useRef<string[] | undefined>(initialValues?.roles);

  const { edges, creatingNode, refetchNodes } = useContext(NavigatorUpsertContext);

  const [activeTab, setActiveTab] = useState<string | null>(TabTypeEnum.INFORMATION);

  const [banner, setbanner] = useState<File | null>(null);
  const [filePermission, setFilePermission] = useState<File | null>(null);
  const [employeeCodes, setEmployeeCodes] = useState<string[] | undefined>(initialValues?.employeeCodes);

  const { form, onSubmit, parentRootId, setParentRootId, nodeContainerParent, isAssignAllEmployee, toggleIsAssignAllEmployee } =
    useUpsertSeaRegion({
      initialValues,
      closeDrawer: onClose,
      updateNode,
    });

  const { deletingNode, deleteNode } = useDeleteNode();

  const { closeModal, openModal, modalConfirm } = useModalConfirm({
    loading: deletingNode,
    title: `Xóa ${nodeContainerLabel[NodeType.SEA_REGION]}`,
    content: confirmDeleteLabel({ nodeType: nodeContainerLabel[NodeType.SEA_REGION], nodeName: initialValues?.label }),
    onOk() {
      if (initialValues && initialValues.id) {
        const withDeleteNodeContainer = nodeContainerParent?.data.items && nodeContainerParent?.data.items?.length <= 1;
        const deletedIds = withDeleteNodeContainer ? [initialValues.id, nodeContainerParent.id] : [initialValues.id];
        deleteNode({
          ids: deletedIds,
          nodeType: initialValues?.type || NodeType.SEA_REGION,
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
      <LoadingOverlay zIndex={10} visible={creatingNode} loaderProps={{ children: <Loader /> }} />
      <form
        onSubmit={form.onSubmit((value: TCustomNodeData) =>
          onSubmit({
            value,
            employeeCodes,
            selectedPositions: selectedPositionsRef.current,
            selectedDepartments: selectDepartmentRef.current?.getCheckedNodes(),
          })
        )}
      >
        <Tabs value={activeTab} onChange={setActiveTab}>
          <Tabs.List mb={'md'}>
            <Tabs.Tab value={TabTypeEnum.INFORMATION} c={activeTab === TabTypeEnum.INFORMATION ? '#2A2A86' : undefined}>
              Thông tin
            </Tabs.Tab>
            <Tabs.Tab value={TabTypeEnum.PERMISSION} c={activeTab === TabTypeEnum.PERMISSION ? '#2A2A86' : undefined}>
              Phân quyền
            </Tabs.Tab>
          </Tabs.List>
          <Tabs.Panel value={TabTypeEnum.INFORMATION} pt="xs">
            <SeaRegionInformation
              parentRootId={parentRootId}
              setParentRootId={setParentRootId}
              form={form}
              banner={banner}
              setbanner={setbanner}
              initialValues={initialValues}
            />
          </Tabs.Panel>
          <Tabs.Panel value={TabTypeEnum.PERMISSION} pt="xs">
            <Flex direction="column" gap={20}>
              <Switch defaultChecked={isAssignAllEmployee} onChange={toggleIsAssignAllEmployee} label="Giao bài cho tất cả nhân sự" />
              {!isAssignAllEmployee && (
                <SelectPermission
                  form={form}
                  employeeCodes={employeeCodes}
                  setEmployeeCodes={setEmployeeCodes}
                  filePermission={filePermission}
                  setFilePermission={setFilePermission}
                  selectDepartmentRef={selectDepartmentRef}
                  selectDepartments={initialValues.departments}
                  selectedPositionsRef={selectedPositionsRef}
                />
              )}
            </Flex>
          </Tabs.Panel>
        </Tabs>
        {form.errors?.[errorPermission] && (
          <Text c="#FA5252" mt="sm">
            {form.errors?.[errorPermission]}
          </Text>
        )}
        <Flex gap={12} justify="flex-end" mt={'md'}>
          {modalConfirm}
          <Tooltip
            hidden={!hasChilds}
            label={validateDeleteLabel({
              nodeType: nodeContainerLabel[NodeType.SEA_REGION],
              nodeChildName: nodeContainerLabel[NodeType.ISLAND_AREA],
            })}
          >
            <Button loading={deletingNode} disabled={updatetingNode || !!hasChilds} onClick={openModal} variant="outline" color="#FA5252">
              <Trash size={16} />
            </Button>
          </Tooltip>
          <Button loading={updatetingNode} type="submit" variant="outline" color="#111928">
            Chỉnh sửa
          </Button>
        </Flex>
      </form>
    </Box>
  );
};
