import { useContext, type MouseEvent } from 'react';
import { useUpdateNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { Node } from 'reactflow';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';

export const useRepositionNode = () => {
  const { nodes, setNodes } = useContext(NavigatorUpsertContext);

  const { updatetingNode, updateNode } = useUpdateNode();

  const onDragNodeStop = (_event: MouseEvent, node: Node<TCustomNodeData>, _nodes: Node[]) => {
    if (node.position.x === node.data.positionX && node.position.y === node.data.positionY) return;
    const nodeUpdate = nodes
      .filter(n => n.data.id?.toString() === node.data.id?.toString())
      .map(
        (n): TCustomNodeData => ({
          ...n.data,
          positionX: node.position.x,
          positionY: node.position.y,
        })
      );
    updateNode({
      nodes: nodeUpdate,
      nodeType: NodeType.CONTAINER,
      onSucceed() {
        const nodesRequestMapper = nodeUpdate.reduce<{ [key: string]: TCustomNodeData }>((acc, n) => {
          if (n.id) {
            acc[n.id.toString()] = n;
          }
          return acc;
        }, {});
        const newNode = [...nodes].map(n => {
          if (nodesRequestMapper[n.id]) {
            return {
              ...n,
              data: {
                ...n.data,
                ...nodesRequestMapper[n.id],
              },
            };
          }
          return n;
        });
        setNodes(newNode);
      },
      onFailed() {
        // TODO
      },
    });
  };

  return {
    onDragNodeStop,
    repositioningNode: updatetingNode,
  };
};
