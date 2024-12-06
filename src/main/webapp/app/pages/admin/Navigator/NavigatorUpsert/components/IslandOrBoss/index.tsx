import React, { memo, useContext, useMemo } from 'react';
import { Droppable, Draggable, DraggableProvided, DraggableStateSnapshot } from 'react-beautiful-dnd';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { NodeHead } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeHead';
import { TCustomNodeData, TCustomNodeProps } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { useUpdateNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { NodeItemWithDrag } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeItemWithDrag';
import { DrawerIslandOrBoss } from 'app/pages/admin/Navigator/NavigatorUpsert/components/IslandOrBoss/DrawerIslandOrBoss';

export const NodeContainerIslandOrBoss = memo(({ id, data, isConnectable, selected }: TCustomNodeProps) => {
  const { createdNodeId } = useContext(NavigatorUpsertContext);

  const items = useMemo(() => data.items || [], [data.items]);

  const itemIds = useMemo(() => items.map(i => i.id?.toString()), [items]);

  return (
    <div className={`node-item ${selected && 'active'} ${itemIds.includes(createdNodeId?.toString()) && 'created'}`}>
      <NodeHead id={id} data={data} isConnectable={isConnectable} />
      <Droppable droppableId={id}>
        {droppableProvided => (
          <div className="droppable-wrapper" {...droppableProvided.droppableProps} ref={droppableProvided.innerRef}>
            {items.map((item, index) => (
              <Draggable key={`${id}${item.id}`} draggableId={`${id}${item.id}`} index={index}>
                {(draggableProvided, draggableSnapshot) => (
                  <NodeIsland
                    id={id}
                    item={item}
                    key={item.id}
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

interface NodeIslandProps {
  id: string | number;
  item: TCustomNodeData;
  isConnectable: boolean;
  draggableProvided: DraggableProvided;
  draggableSnapshot: DraggableStateSnapshot;
}

const NodeIsland = ({ id, item, isConnectable, draggableProvided, draggableSnapshot }: NodeIslandProps) => {
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
      drawerDetailContent={
        <DrawerIslandOrBoss
          initialValues={item}
          updatetingNode={updatetingNode}
          updateNode={updateNode}
          onClose={() => setOpenedDrawer(undefined)}
        />
      }
    />
  );
};
