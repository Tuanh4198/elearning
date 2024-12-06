import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { nodeContainerLabel } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { useUpdateNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData, TNodeExpand } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { notiWarning } from 'app/shared/notifications';
import { useContext } from 'react';
import { DropResult } from 'react-beautiful-dnd';

export const useReorderNodes = () => {
  const { nodes, setNodes } = useContext(NavigatorUpsertContext);

  const { updatetingNode, updateNode } = useUpdateNode();

  const onDragNodeEnd = (result: DropResult) => {
    if (!result.destination) return;
    const { source, destination, draggableId } = result;
    const oldNode = structuredClone(nodes);
    let newNode = structuredClone(nodes);
    let nodeUpdate: TCustomNodeData[] = [];
    let draggableNode: TCustomNodeData | undefined;
    let destinationNode: TNodeExpand | undefined;
    let isOutsideContainer = true;
    // replace position inside container
    newNode = newNode.map(n => {
      if (n.id === destination.droppableId) destinationNode = n;
      if (n.id === source.droppableId) {
        draggableNode = n.data.items?.find(i => i.id?.toString() === draggableId.toString());
      }
      if (n.id === source.droppableId && n.id === destination.droppableId) {
        isOutsideContainer = false;
        let items = n.data.items;
        if (items) {
          const [movedItem] = items.splice(source.index, 1);
          items.splice(destination.index, 0, movedItem);
          items = items.map((item, index) => ({ ...item, position: index + 1 }));
        }
        nodeUpdate = items || [];
        return { ...n, data: { ...n.data, items } };
      }
      return n;
    });
    // replace position outside container
    if (isOutsideContainer && destinationNode && draggableNode) {
      // check label is exist in new container
      if (
        destinationNode.data.items &&
        destinationNode.data.items.some(i => i?.label?.toUpperCase() === draggableNode?.label?.trim().toUpperCase())
      ) {
        notiWarning({
          message: `Tên ${draggableNode?.type && nodeContainerLabel[draggableNode?.type]} "${
            draggableNode.label
          }" đã tồn tại trong nhóm, không thể kéo vào nhóm này!`,
        });
        return;
      }
      newNode = newNode.map((n): TNodeExpand => {
        // delete item in old container
        if (n.id === source.droppableId) {
          const newOrderItem = n.data.items
            ?.filter(i => i.id?.toString() !== draggableNode?.id?.toString())
            .map((i, index) => ({
              ...i,
              position: index + 1,
            }));
          nodeUpdate = [...nodeUpdate, ...(newOrderItem || [])];
          return {
            ...n,
            data: {
              ...n.data,
              items: newOrderItem,
            },
          };
        }
        // add item to new container
        if (n.id === destination.droppableId && draggableNode) {
          const newItem = {
            ...draggableNode,
            position: n.data.items ? n.data.items.length + 1 : 0,
          };
          nodeUpdate = [...nodeUpdate, newItem];
          return {
            ...n,
            data: {
              ...n.data,
              items: [...(n.data.items || []), newItem],
            },
          };
        }
        return n;
      });
    }
    // don't call api and update state when index not change
    if (!isOutsideContainer && source.index === destination.index) return;
    // update state
    setNodes(newNode);
    // call api update
    updateNode({
      nodes: nodeUpdate,
      onSucceed() {
        // TODO
      },
      onFailed() {
        setNodes(oldNode);
      },
    });
  };

  return {
    onDragNodeEnd,
    reorderingNode: updatetingNode,
  };
};
