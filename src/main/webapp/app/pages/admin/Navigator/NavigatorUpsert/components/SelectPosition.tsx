import { Combobox, Flex, InputBaseProps, Pill, ScrollArea } from '@mantine/core';
import { useHandleSelectWithLoadmore } from 'app/shared/hooks/useHandleSelectWithLoadmore';
import { IPosition } from 'app/shared/model/position.model';
import { ASC, ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import React, { forwardRef, Fragment, useImperativeHandle, useMemo } from 'react';

export const apiPositionUrl = 'api/positions';

const formatOption = (value: IPosition) => {
  return {
    label: value?.name || '',
    value: value?.code || '',
  };
};

export const SelectPosition = forwardRef(
  (
    {
      placeholder,
      defaultSelected,
      defaultSelecteds,
      searchPlaceholder,
      onSelectedItem,
      ...rest
    }: InputBaseProps & {
      placeholder?: string;
      searchPlaceholder?: string;
      defaultSelected?: IPosition;
      defaultSelecteds?: IPosition[];
      onSelectedItem?: (value?: string, data?: IPosition) => void;
    },
    ref
  ) => {
    const getAllPositions = async (isFirst?: boolean, name?: string, codes?: string[]) => {
      const requestUrl = `${apiPositionUrl}${`?page=${isFirst ? 0 : pageRef.current}&size=${ITEMS_PER_PAGE}&sort=id,${ASC}`}${
        name != null && name.trim() !== '' ? `&name.contains=${name.trim()}` : ''
      }${codes && codes.length > 0 ? `&code.in=${codes}` : ''}`;
      const res = await axios.get<IPosition[]>(requestUrl);
      return res;
    };

    const {
      pageRef,
      viewportRef,
      onScrollPositionChange,
      options,
      empty,
      combobox,
      onOptionSubmit,
      comboboxTarget,
      comboboxSearch,
      setSelectedValues,
      selectedValues,
    } = useHandleSelectWithLoadmore<IPosition>({
      ...rest,
      showValue: false,
      multiple: true,
      defaultSelected,
      defaultSelecteds,
      onSelectedItem,
      formatOption,
      fetchFunc: getAllPositions,
      placeholder: placeholder ? placeholder : 'Chọn vị trí',
      searchPlaceholder: searchPlaceholder ? searchPlaceholder : 'Tìm kiếm theo tên',
      processData(idMap, acc, currentItem) {
        if (currentItem?.code && !idMap[currentItem?.code]) {
          idMap[currentItem.code] = true;
          acc.push(formatOption(currentItem));
        }
        return acc;
      },
      findData(item, value) {
        return item.code === value;
      },
    });

    useImperativeHandle(ref, () => ({
      onClear(item) {
        setSelectedValues(oldValues => oldValues?.filter(i => i.value !== item));
      },
    }));

    const showSelected = useMemo(() => {
      return (
        selectedValues &&
        selectedValues?.length > 0 && (
          <ScrollArea.Autosize mah={200} scrollbars="y">
            <Flex gap={8} wrap="wrap">
              {selectedValues?.map((item, index) => (
                <Fragment key={index}>
                  <Pill
                    onRemove={() => {
                      onSelectedItem && onSelectedItem(item.value);
                      setSelectedValues(oldValues => oldValues?.filter(i => i.value !== item.value));
                    }}
                    withRemoveButton
                    c="#1E1E73"
                    fw={500}
                    fz="14px"
                    radius="24px"
                    bg="#D4D4F0"
                  >
                    {item.label}
                  </Pill>
                </Fragment>
              ))}
            </Flex>
          </ScrollArea.Autosize>
        )
      );
    }, [selectedValues]);

    return (
      <Fragment>
        <Combobox store={combobox} withinPortal={false} onOptionSubmit={onOptionSubmit}>
          {comboboxTarget}
          <Combobox.Dropdown>
            {comboboxSearch}
            <Combobox.Options>
              <ScrollArea.Autosize type="scroll" mah={200} viewportRef={viewportRef} onScrollPositionChange={onScrollPositionChange}>
                {options}
                {empty}
              </ScrollArea.Autosize>
            </Combobox.Options>
          </Combobox.Dropdown>
        </Combobox>
        {showSelected}
      </Fragment>
    );
  }
);
