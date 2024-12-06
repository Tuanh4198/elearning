import { useConfigFlow } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useConfigFlow';
import { AddNodeRequest, useCreateNodes } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useCreateNode';
import { TNodeExpand } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IEdge } from 'app/shared/model/edge.model';
import { INode } from 'app/shared/model/node.model';
import React, { createContext, MutableRefObject, ReactElement, useRef, useState } from 'react';
import { Edge, EdgeChange, NodeChange } from 'reactflow';

export type TNavigatorUpsertContext = {
  reactFlowRef: MutableRefObject<HTMLDivElement | null>;
  loading: boolean;
  creatingNode: boolean;
  nodesRef: React.MutableRefObject<INode[]>;
  edgesRef: React.MutableRefObject<IEdge[]>;
  nodes: TNodeExpand[];
  edges: Edge[];
  setNodes: React.Dispatch<React.SetStateAction<TNodeExpand[]>>;
  setEdges: React.Dispatch<React.SetStateAction<Edge[]>>;
  onNodesChange: (changes: NodeChange[]) => void;
  onEdgesChange: (changes: EdgeChange[]) => void;
  createNodes: (variables: AddNodeRequest) => Promise<void>;
  createdNodeId: string | number | undefined;
  setCreatedNodeId: React.Dispatch<React.SetStateAction<string | number | undefined>>;
  openedDrawer: string | number | undefined;
  setOpenedDrawer: React.Dispatch<React.SetStateAction<string | number | undefined>>;
  refetchNodes: () => Promise<void>;
};

export const NavigatorUpsertContext = createContext<TNavigatorUpsertContext>({
  reactFlowRef: { current: null },
  loading: false,
  creatingNode: false,
  nodesRef: { current: [] },
  edgesRef: { current: [] },
  nodes: [],
  edges: [],
  setNodes() {},
  setEdges() {},
  onNodesChange() {},
  onEdgesChange() {},
  async createNodes() {},
  createdNodeId: undefined,
  setCreatedNodeId() {},
  openedDrawer: undefined,
  setOpenedDrawer() {},
  async refetchNodes() {},
});

export const NavigatorUpsertContextView = ({ children }: { children: ReactElement }) => {
  const reactFlowRef = useRef<HTMLDivElement | null>(null);

  const [createdNodeId, setCreatedNodeId] = useState<string | number>();
  const [openedDrawer, setOpenedDrawer] = useState<string | number>();

  const { loading, nodesRef, edgesRef, nodes, edges, setNodes, setEdges, onNodesChange, onEdgesChange, refetchNodes } = useConfigFlow();

  const { createNodes, creatingNode } = useCreateNodes({ nodes, edges, setNodes, setEdges, reactFlowRef, nodesRef });

  return (
    <NavigatorUpsertContext.Provider
      value={{
        reactFlowRef,
        createdNodeId,
        setCreatedNodeId,
        openedDrawer,
        setOpenedDrawer,
        loading,
        creatingNode,
        nodesRef,
        edgesRef,
        nodes,
        edges,
        setNodes,
        setEdges,
        onNodesChange,
        onEdgesChange,
        createNodes,
        refetchNodes,
      }}
    >
      {children}
    </NavigatorUpsertContext.Provider>
  );
};
