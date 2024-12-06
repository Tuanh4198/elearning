import { CloseButton, Input, List, Popover } from '@mantine/core';
import { useClickOutside, useDisclosure } from '@mantine/hooks';
import { MagnifyingGlass } from '@phosphor-icons/react';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { nodeContainerLabel, nodeItemGap, nodeItemHeight, nodeWith } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';
import { Empty } from 'app/shared/components/Empty';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { INode, NodeMetafieldKey } from 'app/shared/model/node.model';
import { normalizeString } from 'app/shared/util/normalize-string';
import { debounce } from 'lodash';
import React, { memo, forwardRef, useContext, useMemo, useRef, useState, useImperativeHandle } from 'react';
import { useReactFlow } from 'reactflow';

const width = 480;

const AutocompleteSearch = forwardRef<{ onClose: () => void }>((_, ref) => {
  const { setCenter } = useReactFlow();

  const inputRef = useRef<HTMLInputElement | null>(null);

  const { nodesRef, nodes } = useContext(NavigatorUpsertContext);

  const [opened, { close, open }] = useDisclosure(false);

  const [keySearch, setKeySearch] = useState('');

  const [selected, setSelected] = useState<string | number>();

  const insideRef = useClickOutside(close);

  const onChangeValue = debounce((value: string) => {
    setKeySearch(value);
    open();
  }, 500);

  const searchResult = useMemo(() => {
    if (keySearch === '') return [];
    const normalizedKeySearch = normalizeString(keySearch);
    return nodesRef.current.filter(n => {
      const normalizedItem = normalizeString(n.label);
      return n.type !== NodeType.ROOT && n.type !== NodeType.CONTAINER && normalizedItem.includes(normalizedKeySearch);
    });
  }, [keySearch]);

  useImperativeHandle(ref, () => ({
    onClose: close,
  }));

  const onClear = () => {
    if (inputRef.current) {
      inputRef.current.value = '';
      setKeySearch('');
      setSelected(undefined);
    }
  };

  const onSelectSearchResult = (selectedValue: INode) => {
    const selectedPosition = selectedValue.metafields?.find(m => m.key === NodeMetafieldKey.position)?.value;
    const r = nodes.find(n => selectedValue.rootId?.toString() === n.id.toString());
    setSelected(selectedValue.id);
    if (r?.position.x != null && r?.position.y != null) {
      const _x = r?.position.x + nodeWith / 2;
      const _y = r?.position.y + (selectedPosition ? Number(selectedPosition) * (nodeItemHeight + nodeItemGap) : 0);
      setCenter(_x, _y, { zoom: 1, duration: 1 });
    }
    close();
  };

  return (
    <Popover opened={opened} position="bottom-start" shadow="md">
      <Popover.Target ref={insideRef}>
        <Input
          w={width}
          ref={inputRef}
          placeholder="Tìm kiếm"
          leftSection={<MagnifyingGlass size={16} />}
          onClick={open}
          onChange={e => onChangeValue(e.target.value)}
          rightSectionPointerEvents="painted"
          rightSection={
            <CloseButton aria-label="Clear input" onClick={onClear} style={{ display: keySearch !== '' ? undefined : 'none' }} />
          }
          styles={{
            wrapper: {
              borderRadius: 50,
              boxShadow:
                '0px 7px 7px -5px rgba(0, 0, 0, 0.04), 0px 10px 15px -5px rgba(0, 0, 0, 0.10), 0px 1px 3px 0px rgba(0, 0, 0, 0.05)',
            },
            input: {
              borderRadius: 50,
            },
          }}
        />
      </Popover.Target>
      <Popover.Dropdown w={width}>
        {searchResult.length > 0 ? (
          <List className="list-option-search-result" listStyleType="none" spacing={8}>
            {searchResult.map(i => (
              <List.Item onClick={() => onSelectSearchResult(i)} key={i.id} c={selected === i.id ? '#2a2a86' : undefined}>
                {nodeContainerLabel[i.type]}: {i.label}
              </List.Item>
            ))}
          </List>
        ) : (
          <Empty description="Không có kết quả!" />
        )}
      </Popover.Dropdown>
    </Popover>
  );
});

export default memo(AutocompleteSearch);
