import {
  Checkbox,
  CloseButton,
  Group,
  RenderTreeNodePayload,
  ScrollArea,
  TextInput,
  Tree,
  Text,
  useTree,
  Loader,
  Box,
  UseTreeReturnType,
  Flex,
  LoadingOverlay,
} from '@mantine/core';
import { debounce } from 'lodash';
import React, { forwardRef, useImperativeHandle, useMemo, useRef, useState } from 'react';
import { TreeNodeData } from '@mantine/core';
import { Empty } from 'app/shared/components/Empty';
import { CaretDown, CaretUp } from '@phosphor-icons/react';
import { normalizeString } from 'app/shared/util/normalize-string';
import { useFetchDepartment } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useFetchDepartment';
import { IDepartment } from 'app/shared/model/department.model';

const searchTree = (treeNodeData: TreeNodeData[], keyword: string): TreeNodeData[] => {
  return treeNodeData
    .map(node => {
      const normalizedItemLabel = normalizeString(node?.label?.toString() ?? '');
      const isMatch = normalizedItemLabel.includes(keyword);
      const matchedChildren = node.children ? searchTree(node.children, keyword) : [];
      if (isMatch || matchedChildren.length > 0) {
        return {
          ...node,
          children: matchedChildren.length > 0 ? matchedChildren : node.children,
        };
      }
      return null;
    })
    .filter(node => node !== null) as TreeNodeData[];
};

function buildDepartmentTree(departments?: IDepartment[]): TreeNodeData[] {
  if (!departments) return [];
  const departmentMap: { [key: number]: TreeNodeData } = {};
  const tree: TreeNodeData[] = [];
  departments.forEach(department => {
    const node: TreeNodeData = {
      label: department.name,
      value: department.code.toString(),
      children: [],
    };
    departmentMap[department.id] = node;
  });
  departments.forEach(department => {
    const parentId = department.parentId;
    if (parentId != null && parentId !== -1) {
      const parentNode = departmentMap[parentId];
      const childNode = departmentMap[department.id];
      if (parentNode) {
        parentNode.children ? parentNode.children.push(childNode) : (parentNode.children = [childNode]);
      }
    } else {
      tree.push(departmentMap[department.id]);
    }
  });
  return tree;
}

const renderTreeNode = ({
  node,
  expanded,
  hasChildren,
  elementProps,
  tree,
  isSelectedList,
  treeOption,
}: RenderTreeNodePayload & { isSelectedList?: boolean; treeOption?: UseTreeReturnType }) => {
  const checked = isSelectedList ? isSelectedList : tree.isNodeChecked(node.value);
  const indeterminate = tree.isNodeIndeterminate(node.value);

  return (
    <Group gap="xs" mb={5} align="flex-start" {...elementProps}>
      <Checkbox.Indicator
        mt={3}
        checked={checked}
        indeterminate={indeterminate}
        onClick={() => {
          if (isSelectedList) {
            return treeOption && treeOption.uncheckNode(node.value);
          } else {
            return !checked ? tree.checkNode(node.value) : tree.uncheckNode(node.value);
          }
        }}
      />
      {hasChildren && (
        <Box mt={3}>
          {expanded ? (
            <CaretUp size={14} onClick={() => tree.toggleExpanded(node.value)} />
          ) : (
            <CaretDown size={14} onClick={() => tree.toggleExpanded(node.value)} />
          )}
        </Box>
      )}
      <Text c="#111928" flex={1}>
        {node.label}
      </Text>
    </Group>
  );
};

interface TreeSelectDepartmentProps {
  defaultSelecteds?: string[];
}

export const TreeSelectDepartment = forwardRef(({ defaultSelecteds }: TreeSelectDepartmentProps, ref) => {
  const { data, isLoading } = useFetchDepartment();

  const department = useMemo(() => buildDepartmentTree(data), [data]);

  const inputRef = useRef<HTMLInputElement | null>(null);

  const [keySearch, setKeySearch] = useState('');

  const onChangeValue = debounce((value: string) => {
    setKeySearch(value);
  }, 500);

  const onClear = () => {
    if (inputRef.current) {
      inputRef.current.value = '';
      setKeySearch('');
    }
  };

  const tree = useTree({ initialCheckedState: defaultSelecteds });

  const searchResult = useMemo(() => {
    if (keySearch === '') return department;
    const normalizedKeySearch = normalizeString(keySearch);
    return searchTree(department, normalizedKeySearch);
  }, [keySearch, department]);

  useImperativeHandle(ref, () => ({
    getCheckedNodes() {
      return tree
        .getCheckedNodes()
        ?.filter(i => i.checked)
        ?.map(i => i.value);
    },
  }));

  return (
    <Flex direction="column" w="100%" gap={12}>
      <TextInput
        disabled={isLoading}
        ref={inputRef}
        label="Phòng ban"
        placeholder="Tìm kiếm phòng ban"
        onChange={e => onChangeValue(e.target.value)}
        rightSectionPointerEvents="painted"
        rightSection={
          isLoading ? (
            <Loader size={16} />
          ) : (
            <CloseButton aria-label="Clear input" onClick={onClear} style={{ display: keySearch !== '' ? undefined : 'none' }} />
          )
        }
      />
      <Box pos="relative">
        <LoadingOverlay zIndex={10} visible={isLoading} loaderProps={{ children: <Loader size={16} /> }} />
        <ScrollArea.Autosize mih={300} mah={300}>
          {searchResult.length > 0 ? (
            <Tree
              tree={tree}
              data={searchResult}
              levelOffset={23}
              expandOnClick={false}
              renderNode={payload => renderTreeNode({ ...payload })}
            />
          ) : (
            !isLoading && <Empty description="Không có kết quả!" />
          )}
        </ScrollArea.Autosize>
      </Box>
    </Flex>
  );
});
