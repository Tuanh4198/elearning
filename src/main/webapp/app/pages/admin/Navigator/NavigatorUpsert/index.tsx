import React, { useContext, useMemo, useRef } from 'react';
import 'reactflow/dist/style.css';
import './styles.scss';
import ReactFlow, { Background, MiniMap, Panel, ReactFlowProvider } from 'reactflow';
import {
  NavigatorUpsertContext,
  NavigatorUpsertContextView,
} from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { DragDropContext } from 'react-beautiful-dnd';
import { miniMapNodeColor, nodeTypes } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { NotiCreatedNode } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NotiCreatedNode';
import { Loader, LoadingOverlay } from '@mantine/core';
import AutocompleteSearch from 'app/pages/admin/Navigator/NavigatorUpsert/components/AutocompleteSearch';
import { useReorderNodes } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useReorderNodes';
import { useRepositionNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useRepositionNode';

const NavigatorUpsertContent = () => {
  const autocompleteSearchRef = useRef<any>(null);

  const { reactFlowRef, loading, nodes, edges, onNodesChange, onEdgesChange } = useContext(NavigatorUpsertContext);

  const { onDragNodeEnd, reorderingNode } = useReorderNodes();

  const { onDragNodeStop, repositioningNode } = useRepositionNode();

  const isLoading = useMemo(() => loading || reorderingNode || repositioningNode, [loading, reorderingNode, repositioningNode]);

  const onMouseDownCapture = () => {
    autocompleteSearchRef?.current?.onClose && autocompleteSearchRef?.current?.onClose();
  };

  return (
    <DragDropContext onDragEnd={onDragNodeEnd}>
      <ReactFlow
        ref={reactFlowRef}
        fitView
        nodesDraggable
        minZoom={0.5}
        maxZoom={1}
        nodes={nodes}
        edges={edges}
        nodeTypes={nodeTypes}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onNodeDragStop={onDragNodeStop}
        onMouseDownCapture={onMouseDownCapture}
      >
        <LoadingOverlay w="100%" h="100%" zIndex={10000} visible={isLoading} loaderProps={{ children: <Loader /> }} />
        <Panel position="top-left">{!isLoading && <AutocompleteSearch ref={autocompleteSearchRef} />}</Panel>
        <MiniMap zoomable pannable nodeColor={miniMapNodeColor} />
        <NotiCreatedNode />
        <Background />
      </ReactFlow>
    </DragDropContext>
  );
};

export const NavigatorUpsert = () => {
  return (
    <ReactFlowProvider>
      <NavigatorUpsertContextView>
        <NavigatorUpsertContent />
      </NavigatorUpsertContextView>
    </ReactFlowProvider>
  );
};
