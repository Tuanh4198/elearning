import React, { memo, useContext, useMemo } from 'react';
import { Droppable, Draggable, DraggableProvided, DraggableStateSnapshot } from 'react-beautiful-dnd';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { NodeHead } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeHead';
import { TCustomNodeData, TCustomNodeProps } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { DrawerUpdateSeaRegion } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaRegion/DrawerUpdateSeaRegion';
import { useUpdateNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { DrawerIslandArea } from 'app/pages/admin/Navigator/NavigatorUpsert/components/IslandArea/DrawerIslandArea';
import { NodeItemWithDrag } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeItemWithDrag';

export const NodeContainerSeaRegion = memo(({ id, data, isConnectable, selected }: TCustomNodeProps) => {
  const { createdNodeId } = useContext(NavigatorUpsertContext);

  const items = useMemo(() => data.items || [], [data.items]);

  const itemIds = useMemo(() => items.map(i => i.id?.toString()), [items]);

  return (
    <div className={`node-item ${selected && 'active'} ${itemIds.includes(createdNodeId?.toString()) && 'created'}`}>
      <NodeHead id={id} data={data} isConnectable={isConnectable} />
      <Droppable droppableId={id} type={NodeType.SEA_REGION}>
        {droppableProvided => (
          <div className="droppable-wrapper" {...droppableProvided.droppableProps} ref={droppableProvided.innerRef}>
            {items.map((item, index) => (
              <Draggable key={item.id} draggableId={item.id?.toString() || ''} index={index}>
                {(draggableProvided, draggableSnapshot) => (
                  <NodeSeaRegion
                    id={id}
                    item={item}
                    key={item.id}
                    parentId={data.rootId || ''}
                    parentNodeType={data.type || NodeType.SEA_REGION}
                    isConnectable={isConnectable}
                    draggableProvided={draggableProvided}
                    draggableSnapshot={draggableSnapshot}
                  />
                )}
              </Draggable>
            ))}
            {droppableProvided.placeholder}
          </div>
        )}
      </Droppable>
    </div>
  );
});

interface NodeSeaRegionProps {
  id: string | number;
  item: TCustomNodeData;
  isConnectable: boolean;
  parentNodeType: NodeType;
  parentId: string | number;
  draggableProvided: DraggableProvided;
  draggableSnapshot: DraggableStateSnapshot;
}

const NodeSeaRegion = ({ id, item, parentId, parentNodeType, isConnectable, draggableProvided, draggableSnapshot }: NodeSeaRegionProps) => {
  const { setOpenedDrawer } = useContext(NavigatorUpsertContext);

  const { updatetingNode, updateNode } = useUpdateNode();

  return (
    <NodeItemWithDrag
      id={id}
      parentId={id}
      item={item}
      isConnectable={isConnectable}
      updatetingNode={updatetingNode}
      updateNode={updateNode}
      draggableProvided={draggableProvided}
      draggableSnapshot={draggableSnapshot}
      drawerCreateContent={
        <DrawerIslandArea
          initialValues={undefined}
          parentNodeId={parentId?.toString()}
          parentItemId={item.id?.toString() || ''}
          parentNodeType={parentNodeType}
          onClose={() => setOpenedDrawer(undefined)}
        />
      }
      drawerDetailContent={
        <DrawerUpdateSeaRegion
          initialValues={item}
          updatetingNode={updatetingNode}
          updateNode={updateNode}
          onClose={() => setOpenedDrawer(undefined)}
        />
      }
    />
  );
};
