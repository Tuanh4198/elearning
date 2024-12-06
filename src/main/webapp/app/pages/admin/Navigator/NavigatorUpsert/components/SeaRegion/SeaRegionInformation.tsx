import { Flex, Switch, Textarea, TextInput, Select, ComboboxData } from '@mantine/core';
import { UseFormReturnType } from '@mantine/form';
import { CaretDown } from '@phosphor-icons/react';
import { BtnSelectBanner } from 'app/pages/admin/Navigator/NavigatorUpsert/components/BtnSelectBanner';
import { NavigatorUpsertContext } from 'app/pages/admin/Navigator/NavigatorUpsert/context/NavigatorUpsertContext';
import { TCustomNodeData } from 'app/pages/admin/Navigator/NavigatorUpsert/type';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import React, { useContext } from 'react';

interface SeaRegionInformationProps {
  initialValues?: TCustomNodeData;
  form: UseFormReturnType<TCustomNodeData, (values: TCustomNodeData) => TCustomNodeData>;
  banner: File | null;
  parentRootId: string | null;
  setParentRootId: React.Dispatch<React.SetStateAction<string | null>>;
  setbanner: React.Dispatch<React.SetStateAction<File | null>>;
}

export const SeaRegionInformation = ({
  initialValues,
  form,
  banner,
  parentRootId,
  setParentRootId,
  setbanner,
}: SeaRegionInformationProps) => {
  const { nodesRef } = useContext(NavigatorUpsertContext);

  const parentRootOption = (): ComboboxData =>
    nodesRef.current
      .filter(n => n.type === NodeType.SEA_MAP)
      .map(n => ({
        label: n.label || '',
        value: n.id.toString(),
      }));

  return (
    <Flex direction="column" gap={20}>
      <TextInput
        maxLength={50}
        withAsterisk
        label="Tên Vùng biển"
        placeholder="Nhập tên"
        className="input-node-title"
        key={form.key('label')}
        {...form.getInputProps('label')}
      />
      <Textarea
        maxLength={256}
        withAsterisk
        label="Mô tả"
        placeholder="Nhập mô tả"
        key={form.key('description')}
        {...form.getInputProps('description')}
        rows={4}
        resize="none"
      />
      <Switch label="Khoá" defaultChecked={initialValues?.status} key={form.key('status')} {...form.getInputProps('status')} />
      {/* <BtnSelectBanner type={NodeType.SEA_REGION} /> */}
      <Select
        w="100%"
        withAsterisk
        allowDeselect={false}
        disabled={!!initialValues?.id}
        searchable
        onChange={setParentRootId}
        value={parentRootId}
        data={parentRootOption()}
        label="Hải trình"
        placeholder="Chọn hải trình"
        rightSection={<CaretDown size={16} />}
      />
    </Flex>
  );
};
