import { enrichEdgeItem, gatherNodeItem } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { TCustomNode } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { IEdge } from 'app/shared/model/edge.model';
import { INode } from 'app/shared/model/node.model';
import { notiError } from 'app/shared/notifications';
import axios from 'axios';
import React, { useEffect, useRef } from 'react';
import { useState } from 'react';
import { Edge, Node } from 'reactflow';

export const useFetchNodes = ({
  setNodes,
  setEdges,
  onLayoutConfig,
}: {
  setNodes: React.Dispatch<React.SetStateAction<Node<any, string | undefined>[]>>;
  setEdges: React.Dispatch<React.SetStateAction<Edge<any>[]>>;
  onLayoutConfig: ({ _nodes, _edges }: { _nodes: TCustomNode[]; _edges: Edge[] }) => void;
}) => {
  const nodesRef = useRef<INode[]>([]);
  const edgesRef = useRef<IEdge[]>([]);

  const [isLoadingNodes, setIsLoadingNodes] = useState(false);

  const fetchNodes = async () => {
    setIsLoadingNodes(true);
    try {
      const resNodes = await axios.get<INode[]>(`/api/nodes`);
      const resEdges = await axios.get<IEdge[]>(`/api/edges`);
      nodesRef.current = resNodes.data;
      edgesRef.current = resEdges.data;
      if (resNodes.data) {
        const nodes = gatherNodeItem(resNodes.data);
        setNodes(nodes);
        const edges = enrichEdgeItem({ nodes, edges: [], newEdges: resEdges.data });
        setEdges(edges);
        // set layout horizontal
        // onLayoutConfig({ _nodes: nodes, _edges: edges });
      }
    } catch (error: any) {
      console.error('Create error: ', error);
      const errMsg = error?.response?.data?.errors?.message?.[0];
      notiError({
        message: (
          <>
            Tải nodes thất bại, vui lòng thử lại sau! <br />
            {errMsg}
          </>
        ),
      });
    }
    setIsLoadingNodes(false);
  };

  useEffect(() => {
    fetchNodes();
  }, []);

  return {
    isLoadingNodes,
    refetchNodes: fetchNodes,
    nodesRef,
    edgesRef,
  };
};
