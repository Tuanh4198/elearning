import React, { Fragment, memo, useContext, useMemo } from 'react';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { NodeItem } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeItem';
import { NodeHead } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeHead';
import { BtnCreateNodeChild } from 'app/pages/admin/Navigator/NavigatorUpsert/components/BtnCreateNodeChild';
import { useDrawer } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDrawer';
import { DrawerUpsertSeaMap } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaMap/DrawerSeaMap';
import { TCustomNodeData, TCustomNodeProps } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { DrawerCreateSeaRegion } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaRegion/DrawerCreateSeaRegion';
import { useUpdateNode } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { Loader, LoadingOverlay } from '@mantine/core';

export const NodeContainerSeaMap = memo(({ id, type, data, isConnectable, selected }: TCustomNodeProps) => {
  const { createdNodeId } = useContext(NavigatorUpsertContext);

  const items = useMemo(() => data.items || [], [data.items]);

  const itemIds = useMemo(() => items.map(i => i.id?.toString()), [items]);

  return (
    <div className={`node-item ${selected && 'active'} ${itemIds.includes(createdNodeId?.toString()) && 'created'}`}>
      <NodeHead id={id} data={data} isConnectable={isConnectable} />
      <div className="droppable-wrapper">
        {items.map(item => (
          <NodeSeaMap key={item.id} parentId={id} parentNodeType={type as NodeType} item={item} isConnectable={isConnectable} />
        ))}
      </div>
    </div>
  );
});

interface NodeSeaMapProps {
  parentId: string | number;
  parentNodeType: NodeType;
  item: TCustomNodeData;
  isConnectable: boolean;
}

const NodeSeaMap = ({ parentId, parentNodeType, item, isConnectable }: NodeSeaMapProps) => {
  const { createdNodeId, setOpenedDrawer } = useContext(NavigatorUpsertContext);

  const detailDrawerID = useMemo(() => item.id?.toString() || '', [item.id]);

  const { opened: openedDetail, drawer: drawerDetail } = useDrawer(detailDrawerID);

  const createDrawerID = useMemo(() => `${parentId}_${item.id}`, [parentId, item.id]);

  const { opened: openedCreate, drawer: drawerCreate } = useDrawer(createDrawerID);

  const { updatetingNode, updateNode } = useUpdateNode();

  return (
    <Fragment>
      <div
        onClick={() => setOpenedDrawer(detailDrawerID)}
        className={`draggable-wrapper ${(openedDetail || openedCreate) && 'active'} ${
          createdNodeId?.toString() === item.id?.toString() && 'created'
        }`}
        style={{ cursor: 'pointer' }}
      >
        <LoadingOverlay w="100%" h="100%" zIndex={10000} visible={updatetingNode} loaderProps={{ children: <Loader /> }} />
        <NodeItem item={item} active={openedDetail} isConnectable={isConnectable} withDragAndDrop={false} updateNode={updateNode} />
        <BtnCreateNodeChild onClick={() => setOpenedDrawer(createDrawerID)} />
      </div>
      {drawerDetail({
        title: 'Chi tiết',
        content: (
          <DrawerUpsertSeaMap
            initialValues={item}
            updatetingNode={updatetingNode}
            updateNode={updateNode}
            onClose={() => setOpenedDrawer(undefined)}
          />
        ),
      })}
      {drawerCreate({
        title: 'Tạo mới',
        content: (
          <DrawerCreateSeaRegion
            parentNodeId={parentId?.toString()}
            parentItemId={item.id?.toString() || ''}
            parentNodeType={parentNodeType}
            onClose={() => setOpenedDrawer(undefined)}
          />
        ),
      })}
    </Fragment>
  );
};
