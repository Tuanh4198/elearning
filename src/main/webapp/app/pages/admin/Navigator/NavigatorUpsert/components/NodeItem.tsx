import { Flex, Switch, Text, Tooltip } from '@mantine/core';
import { DotsSix } from '@phosphor-icons/react';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { convertNodeItemToTCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { UpdateNodeParams } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpdateNode';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { INode } from 'app/shared/model/node.model';
import axios from 'axios';
import React, { Fragment, memo, useContext } from 'react';
import { Handle, Position } from 'reactflow';

interface NodeItemProps {
  item: TCustomNodeData;
  isConnectable: boolean;
  withDragAndDrop?: boolean;
  withHandleSource?: boolean;
  active: boolean;
  isDragging?: boolean;
  updateNode: (props: UpdateNodeParams) => Promise<void>;
}

export const NodeItem = memo(
  ({ item, active, isDragging, isConnectable, updateNode, withDragAndDrop = true, withHandleSource = true }: NodeItemProps) => {
    const { nodes, setNodes } = useContext(NavigatorUpsertContext);

    const onChangeStatus = event => {
      const checked = event.currentTarget.checked;
      (async () => {
        if (!item.id) return null;
        const res = await axios.get<INode>(`/api/nodes/${item.id}`);
        const nodeDetail = {
          ...item,
          ...convertNodeItemToTCustomNodeData(res.data),
        };
        const newValue = { ...nodeDetail, status: checked };
        updateNode({
          nodes: [newValue],
          isSimpleUpdate: true,
          onSucceed() {
            const newNodes = [...nodes].map(n => {
              if (n.id.toString() === newValue.rootId?.toString()) {
                return {
                  ...n,
                  data: {
                    ...n.data,
                    items: n.data.items?.map(i => {
                      if (i.id?.toString() === newValue.id?.toString()) {
                        return newValue;
                      }
                      return i;
                    }),
                  },
                };
              }
              return n;
            });
            setNodes(newNodes);
          },
          onFailed() {},
        });
      })();
    };

    return (
      <Fragment>
        <Flex align="center" gap={10}>
          {withDragAndDrop && (
            <Flex direction="column" align="center">
              <Text c="#1F2A37" fw={400} fz={12}>
                {item.position}
              </Text>
              <DotsSix size={16} color="#111928" />
            </Flex>
          )}
          <Flex direction="column">
            <Tooltip position="top-start" maw={300} multiline label={item.label}>
              <Text c="#000000" fw={600} fz={14} lineClamp={1}>
                {item.label}
              </Text>
            </Tooltip>
            <Tooltip maw={300} multiline label={item.description} mt={5}>
              <Text c="#6B7280" mih="50px" fw={400} fz={12} lineClamp={3}>
                {item.description}
              </Text>
            </Tooltip>
            {item.type !== NodeType.ISLAND && item.type !== NodeType.CREEP && (
              <Flex
                className="node-status"
                onClick={event => event.stopPropagation()}
                justify="space-between"
                style={{ cursor: 'pointer' }}
              >
                <Switch
                  onChange={onChangeStatus}
                  disabled={active}
                  mt={12}
                  checked={item.status}
                  label="Khoá"
                  style={{ cursor: 'pointer' }}
                />
              </Flex>
            )}
          </Flex>
        </Flex>
        {withHandleSource && !isDragging && (
          <Handle
            id={item.id?.toString()}
            type="source"
            position={Position.Right}
            isConnectableStart={!isDragging}
            isConnectable={isConnectable}
            style={{ opacity: 0 }}
          />
        )}
      </Fragment>
    );
  }
);
