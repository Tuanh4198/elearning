import { Divider, Flex, Pill, Text } from '@mantine/core';
import { maxItemNodeSeaMap } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import React, { Fragment, memo } from 'react';
import { Handle, HandleType, Position } from 'reactflow';

interface NodeHeadProps {
  id: string | number;
  data: TCustomNodeData;
  isConnectable: boolean;
  flowHandleType?: HandleType;
  flowHandlePosition?: Position;
  withDivider?: boolean;
}

export const NodeHead = memo(
  ({ id, data, isConnectable, flowHandleType = 'target', flowHandlePosition = Position.Left, withDivider = true }: NodeHeadProps) => {
    return (
      <Fragment>
        <Flex align="center" gap={5} className={`_${id.toString()}`} style={{ cursor: 'move' }}>
          <Text c="#000000" fw={600} style={{ textTransform: 'uppercase' }}>
            {data.label}
          </Text>
          {data?.type !== NodeType.ROOT && (
            <Pill c="#1E1E73" bg="#D4D4F0" fz={12} fw={500}>
              {data?.items?.length}
              {data?.items?.[0]?.type === NodeType.SEA_MAP && `/${maxItemNodeSeaMap}`}
            </Pill>
          )}
        </Flex>
        <Handle
          id={flowHandleType === 'source' ? data.rootId?.toString() : id.toString()}
          type={flowHandleType}
          position={flowHandlePosition}
          style={{ opacity: 0 }}
          isConnectable={isConnectable}
        />
        {withDivider && <Divider my="md" />}
      </Fragment>
    );
  }
);
