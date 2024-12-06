import React, { memo } from 'react';
import { NodeHead } from 'app/pages/admin/Navigator/NavigatorUpsert/components/NodeHead';
import { Position } from 'reactflow';
import { TCustomNodeProps } from 'app/pages/admin/Navigator/NavigatorUpsert/type';

export const NodeEmpty = memo(({ id, data, isConnectable, selected }: TCustomNodeProps) => {
  return (
    <div className={`node-item grand-sea-line ${selected && 'active'} ${id}`}>
      <NodeHead
        id={id}
        data={data}
        isConnectable={isConnectable}
        flowHandleType="source"
        flowHandlePosition={Position.Right}
        withDivider={false}
      />
    </div>
  );
});
