import { ActionIcon } from '@mantine/core';
import { Plus } from '@phosphor-icons/react/dist/ssr';
import React, { useCallback } from 'react';
import { memo } from 'react';

interface BtnCreateNodeChildProps {
  onClick?: () => void;
}

export const BtnCreateNodeChild = memo(({ onClick }: BtnCreateNodeChildProps) => {
  const onBtnClick = useCallback(
    event => {
      event.stopPropagation();
      onClick && onClick();
    },
    [onClick]
  );

  return (
    <div onClick={onBtnClick} className="btn-create-node-child-wrapper">
      <ActionIcon size="sm" bg="#111928" radius="100%" className="btn-create-node-child">
        <Plus size={12} color="white" weight="bold" />
      </ActionIcon>
    </div>
  );
});
