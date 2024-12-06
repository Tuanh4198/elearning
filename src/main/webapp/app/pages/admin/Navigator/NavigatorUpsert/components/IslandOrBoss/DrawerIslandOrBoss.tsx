import { Box, Loader, LoadingOverlay, Radio, Group, Skeleton, Flex } from '@mantine/core';
import { CreepForm } from 'app/pages/admin/Navigator/NavigatorUpsert/components/IslandOrBoss/CreepForm';
import { IslandForm } from 'app/pages/admin/Navigator/NavigatorUpsert/components/IslandOrBoss/IslandForm';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { convertNodeItemToTCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { useFetchNodeDetail } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchNodeDetail';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import React, { useContext, useMemo, useState } from 'react';

interface DrawerCreateIslandOrBossProps {
  onClose: () => void;
  parentNodeId: string;
  parentItemId: string;
  parentNodeType: NodeType;
  initialValues: undefined;
}

interface DrawerUpdateIslandOrBossProps {
  onClose: () => void;
  initialValues: TCustomNodeData;
  updatetingNode: boolean;
  updateNode: (props: UpdateNodeParams) => Promise<void>;
}

export const DrawerIslandOrBoss = (props: DrawerCreateIslandOrBossProps | DrawerUpdateIslandOrBossProps) => {
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

  return <DrawerIslandOrBossContent {...rest} initialValues={initialValuesFull as any} />;
};

const DrawerIslandOrBossContent = (props: DrawerCreateIslandOrBossProps | DrawerUpdateIslandOrBossProps) => {
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

  const { creatingNode } = useContext(NavigatorUpsertContext);

  const [type, setType] = useState<string>(initialValues ? initialValues.type ?? NodeType.ISLAND : NodeType.ISLAND);

  return (
    <Box pos="relative">
      <LoadingOverlay zIndex={10} visible={creatingNode || updatetingNode} loaderProps={{ children: <Loader /> }} />
      <Radio.Group value={type} onChange={setType}>
        <Group mt="xs" mb="20px" gap={30}>
          <Radio disabled={!!initialValues} value={NodeType.ISLAND} label="Thêm đảo" />
          <Radio disabled={!!initialValues} value={NodeType.CREEP} label="Đánh quái" />
        </Group>
      </Radio.Group>
      {type === NodeType.ISLAND ? (
        <IslandForm
          initialValues={initialValues}
          onClose={onClose}
          parentNodeId={parentNodeId}
          parentItemId={parentItemId}
          parentNodeType={parentNodeType}
          updateNode={updateNode}
        />
      ) : (
        <CreepForm
          initialValues={initialValues}
          onClose={onClose}
          parentNodeId={parentNodeId}
          parentItemId={parentItemId}
          parentNodeType={parentNodeType}
          updateNode={updateNode}
        />
      )}
    </Box>
  );
};
