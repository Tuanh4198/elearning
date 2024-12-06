import { useState } from 'react';
import axios from 'axios';
import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { CategoryTypeEnum, ICategory } from 'app/shared/model/category.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

interface IFilterParams {
  search: string;
  page: number;
}
export const useFetchCategories = (filter: IFilterParams) => {
  const [size, setSize] = useState<number>(ITEMS_PER_PAGE);

  const fetchCategories = async () => {
    const params = {
      size,
      page: filter.page - 1,
      search: filter.search,
      type: CategoryTypeEnum.COURSE,
    };
    return await axios.get<ICategory[]>(`/api/course-employees/categories`, { params });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: ['useFetchCourseCategoriesAdmin', filter.page, size, filter.search],
    queryFn: fetchCategories,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size,
    total: data?.headers?.xTotalCount,
    setSize,
    refetch,
  };
};
