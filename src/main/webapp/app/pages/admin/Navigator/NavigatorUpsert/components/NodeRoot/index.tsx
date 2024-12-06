import React, { memo, useContext } from 'react';
import { NodeHead } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeHead';
import { BtnCreateNodeChild } from 'app/pages/admin/Navigator/NavigatorUpsert/components/BtnCreateNodeChild';
import { Position } from 'reactflow';
import { useDrawer } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useDrawer';
import { DrawerUpsertSeaMap } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaMap/DrawerSeaMap';
import { TCustomNodeProps } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';

export const NodeRoot = memo(({ id, type, data, isConnectable, selected }: TCustomNodeProps) => {
  const { setOpenedDrawer } = useContext(NavigatorUpsertContext);

  const { opened, drawer } = useDrawer(id);

  return (
    <div className={`node-item grand-sea-line ${(selected || opened) && 'active'} ${id}`}>
      <NodeHead
        id={id}
        data={data}
        isConnectable={isConnectable}
        flowHandleType="source"
        flowHandlePosition={Position.Right}
        withDivider={false}
      />
      <BtnCreateNodeChild onClick={() => setOpenedDrawer(id)} />
      {drawer({
        title: 'Tạo mới',
        content: (
          <DrawerUpsertSeaMap
            initialValues={undefined}
            onClose={() => setOpenedDrawer(undefined)}
            parentNodeId={id.toString()}
            parentItemId={id.toString()}
            parentNodeType={type as NodeType}
          />
        ),
      })}
    </div>
  );
});
