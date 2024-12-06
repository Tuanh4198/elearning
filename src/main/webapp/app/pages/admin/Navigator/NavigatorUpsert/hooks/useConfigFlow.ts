import { useEffect } from 'react';
import { Edge, useEdgesState, useNodesState, useReactFlow } from 'reactflow';
import { getLayoutedElements } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { TCustomNode } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { useFetchNodes } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchNodes';

export const useConfigFlow = () => {
  const { fitView } = useReactFlow();

  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);

  const onLayoutConfig = ({ _nodes, _edges }: { _nodes: TCustomNode[]; _edges: Edge[] }) => {
    const layouted = getLayoutedElements({ nodes: _nodes, edges: _edges });
    setNodes([...layouted.nodes]);
    setEdges([...layouted.edges]);
    window.requestAnimationFrame(() => {
      fitView();
    });
  };

  useEffect(() => {
    onLayoutConfig({ _nodes: nodes, _edges: edges });
  }, []);

  const { nodesRef, edgesRef, isLoadingNodes, refetchNodes } = useFetchNodes({ setNodes, setEdges, onLayoutConfig });

  return {
    loading: isLoadingNodes,
    nodesRef,
    edgesRef,
    nodes,
    edges,
    setNodes,
    setEdges,
    onLayoutConfig,
    onNodesChange,
    onEdgesChange,
    refetchNodes,
  };
};
