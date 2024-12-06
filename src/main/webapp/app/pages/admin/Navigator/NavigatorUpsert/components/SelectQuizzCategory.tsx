import { Combobox, InputBaseProps, ScrollArea } from '@mantine/core';
import { useHandleSelectWithLoadmore } from 'app/shared/hooks/useHandleSelectWithLoadmore';
import { IQuizzCategory } from 'app/shared/model/quizz-category.model';
import { ITEMS_PER_PAGE, ASC } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import React, { forwardRef } from 'react';

export const apiQuizzCategory = 'api/quizz-categories';

const formatOption = (value: IQuizzCategory) => {
  return {
    label: value?.title || '',
    value: value?.id?.toString() || '',
  };
};

export const SelectQuizzCategory = forwardRef(
  (
    {
      placeholder,
      defaultSelected,
      searchPlaceholder,
      onSelectedItem,
      allowClear,
      ...rest
    }: InputBaseProps & {
      placeholder?: string;
      searchPlaceholder?: string;
      defaultSelected?: IQuizzCategory;
      allowClear?: boolean;
      onSelectedItem?: (value?: string, data?: IQuizzCategory) => void;
    },
    ref
  ) => {
    const getAllPositions = async (isFirst?: boolean, search?: string) => {
      const requestUrl = `${apiQuizzCategory}${`?page=${isFirst ? 0 : pageRef.current}&size=${ITEMS_PER_PAGE}&sort=id,${ASC}`}${
        search != null && search.trim() !== '' ? `&search=${search.trim()}` : ''
      }`;
      const res = await axios.get<IQuizzCategory[]>(requestUrl);
      return res;
    };

    const { pageRef, viewportRef, onScrollPositionChange, options, empty, combobox, onOptionSubmit, comboboxTarget, comboboxSearch } =
      useHandleSelectWithLoadmore<IQuizzCategory>({
        ...rest,
        allowClear,
        defaultSelected,
        onSelectedItem,
        formatOption,
        fetchFunc: getAllPositions,
        placeholder: placeholder ? placeholder : 'Chọn danh mục',
        searchPlaceholder: searchPlaceholder ? searchPlaceholder : 'Tìm kiếm theo tên',
        processData(idMap, acc, currentItem) {
          if (currentItem?.id && !idMap[currentItem?.id]) {
            idMap[currentItem.id] = true;
            acc.push(formatOption(currentItem));
          }
          return acc;
        },
        findData(item, value) {
          return item.id?.toString() === value;
        },
      });

    return (
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
    );
  }
);
