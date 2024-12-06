import React, { useContext, useEffect, useMemo, useRef } from 'react';
import { Notification, ThemeIcon, Transition } from '@mantine/core';
import { CheckCircle } from '@phosphor-icons/react';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { nodeContainerLabel } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';

export const NotiCreatedNode = () => {
  const { nodes, createdNodeId, setCreatedNodeId } = useContext(NavigatorUpsertContext);

  const timeoutRef = useRef<any>(null);

  useEffect(() => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }
    if (createdNodeId != null) {
      timeoutRef.current = setTimeout(() => {
        setCreatedNodeId(undefined);
      }, 10000);
    }
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, [createdNodeId, setCreatedNodeId]);

  const newNodeLabel = useMemo(() => {
    if (createdNodeId && nodes.length > 0) {
      for (const n of nodes) {
        if (n.data.items && n.data.items.length > 0) {
          for (const i of n.data.items) {
            if (i.id === createdNodeId) {
              return `${i.type && nodeContainerLabel[i.type]}: ${i.label}`;
            }
          }
        }
      }
    }
    return '';
  }, [createdNodeId, nodes]);

  return (
    <Transition mounted={createdNodeId != null && newNodeLabel !== ''} transition="slide-up" duration={400} timingFunction="ease">
      {styles => (
        <Notification
          color="teal"
          radius={5}
          className="noti-navigator"
          style={styles}
          onClose={() => setCreatedNodeId(undefined)}
          icon={
            <ThemeIcon color="#12B886">
              <CheckCircle size={16} weight="fill" color="#E6FCF5" />
            </ThemeIcon>
          }
        >
          Đã tạo mới <b>{newNodeLabel}</b>
        </Notification>
      )}
    </Transition>
  );
};
