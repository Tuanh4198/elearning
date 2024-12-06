import { Box, Loader, LoadingOverlay } from '@mantine/core';
import { BtnCreateNodeChild } from 'app/pages/admin/Navigator/NavigatorUpsert/components/BtnCreateNodeChild';
import { NodeItem } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeItem';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { useDrawer } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDrawer';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import React, { Fragment, ReactNode, useContext, useMemo } from 'react';
import { DraggableProvided, DraggableStateSnapshot } from 'react-beautiful-dnd';

interface NodeItemWithDragProps {
  id: string | number;
  parentId: string | number;
  item: TCustomNodeData;
  isConnectable: boolean;
  updatetingNode: boolean;
  draggableProvided: DraggableProvided;
  draggableSnapshot: DraggableStateSnapshot;
  drawerDetailContent: ReactNode;
  drawerCreateContent?: ReactNode;
  updateNode: (props: UpdateNodeParams) => Promise<void>;
}

export const NodeItemWithDrag = ({
  id,
  item,
  parentId,
  isConnectable,
  updatetingNode,
  draggableProvided,
  draggableSnapshot,
  drawerDetailContent,
  drawerCreateContent,
  updateNode,
}: NodeItemWithDragProps) => {
  const { createdNodeId, setOpenedDrawer } = useContext(NavigatorUpsertContext);

  const detailDrawerID = useMemo(() => item.id?.toString() || '', [item.id]);

  const { opened: openedDetail, drawer: drawerDetail } = useDrawer(detailDrawerID);

  const createDrawerID = useMemo(() => `${parentId}_${item.id}`, [parentId, item.id]);

  const { opened: openedCreate, drawer: drawerCreate } = useDrawer(createDrawerID);

  return (
    <Fragment>
      <div
        onClick={() => setOpenedDrawer(detailDrawerID)}
        className={`draggable-wrapper ${(openedDetail || openedCreate) && 'active'} ${
          createdNodeId?.toString() === item.id?.toString() && 'created'
        } ${draggableSnapshot.isDragging && 'dragging'}`}
        ref={draggableProvided.innerRef}
        {...draggableProvided.draggableProps}
        {...draggableProvided.dragHandleProps}
        style={{ ...draggableProvided.draggableProps.style }}
      >
        <Box pos="relative">
          <LoadingOverlay zIndex={10} visible={updatetingNode} loaderProps={{ children: <Loader size={16} /> }} />
          <NodeItem
            item={item}
            active={openedDetail}
            isConnectable={isConnectable}
            withDragAndDrop
            isDragging={draggableSnapshot.isDragging}
            withHandleSource={!!drawerCreateContent}
            updateNode={updateNode}
          />
        </Box>
        {drawerCreateContent && <BtnCreateNodeChild onClick={() => setOpenedDrawer(createDrawerID)} />}
      </div>
      {drawerDetail({
        title: 'Chi tiết',
        content: <>{drawerDetailContent}</>,
      })}
      {drawerCreateContent &&
        drawerCreate({
          title: 'Tạo mới',
          content: <>{drawerCreateContent}</>,
        })}
    </Fragment>
  );
};
