import React, { useContext, useRef, useState } from 'react';
import { Box, Button, Flex, LoadingOverlay, Loader, Text, Switch, Divider } from '@mantine/core';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { useUpsertSeaRegion } from 'app/pages/admin/Navigator/NavigatorUpsert/hooks/useUpsertSeaRegion';
import { SeaRegionInformation } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SeaRegion/SeaRegionInformation';
import { SelectPermission } from 'app/pages/admin/Navigator/NavigatorUpsert/components/SelectPermission';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { errorPermission } from 'app/pages/admin/Navigator/NavigatorUpsert/helper';

interface DrawerCreateSeaRegionProps {
  onClose: () => void;
  parentNodeId: string;
  parentItemId: string;
  parentNodeType: NodeType;
}

export const DrawerCreateSeaRegion = ({ onClose, parentNodeId, parentItemId, parentNodeType }: DrawerCreateSeaRegionProps) => {
  const selectDepartmentRef = useRef<any>();
  const selectedPositionsRef = useRef<string[]>();

  const { creatingNode } = useContext(NavigatorUpsertContext);

  const [banner, setbanner] = useState<File | null>(null);
  const [filePermission, setFilePermission] = useState<File | null>(null);
  const [employeeCodes, setEmployeeCodes] = useState<string[]>();

  const { form, onSubmit, parentRootId, setParentRootId, toggleIsAssignAllEmployee, isAssignAllEmployee } = useUpsertSeaRegion({
    initialValues: undefined,
    parentNodeId,
    parentItemId,
    parentNodeType,
    closeDrawer: onClose,
  });

  return (
    <Box pos="relative">
      <LoadingOverlay zIndex={10} visible={creatingNode} loaderProps={{ children: <Loader /> }} />
      <form
        onSubmit={form.onSubmit((value: TCustomNodeData) =>
          onSubmit({
            value,
            employeeCodes,
            selectedPositions: selectedPositionsRef.current,
            selectedDepartments: selectDepartmentRef.current?.getCheckedNodes(),
          })
        )}
      >
        <Flex direction="column" gap={20}>
          <SeaRegionInformation
            parentRootId={parentRootId}
            setParentRootId={setParentRootId}
            form={form}
            banner={banner}
            setbanner={setbanner}
          />
          <Divider />
          <Text c="black" fz={14}>
            Phân quyền tham gia
          </Text>
          <Switch defaultChecked={isAssignAllEmployee} onChange={toggleIsAssignAllEmployee} label="Giao bài cho tất cả nhân sự" />
          {!isAssignAllEmployee && (
            <SelectPermission
              form={form}
              employeeCodes={employeeCodes}
              setEmployeeCodes={setEmployeeCodes}
              filePermission={filePermission}
              setFilePermission={setFilePermission}
              selectDepartmentRef={selectDepartmentRef}
              selectedPositionsRef={selectedPositionsRef}
            />
          )}
          {form.errors?.[errorPermission] && (
            <Text c="#FA5252" mt="sm">
              {form.errors?.[errorPermission]}
            </Text>
          )}
          <Flex gap={12} justify="flex-end">
            <Button onClick={onClose} variant="outline" color="#111928">
              Hủy
            </Button>
            <Button type="submit">Tạo</Button>
          </Flex>
        </Flex>
      </form>
    </Box>
  );
};
