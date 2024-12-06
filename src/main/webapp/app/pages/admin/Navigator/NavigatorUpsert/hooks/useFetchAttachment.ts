import { keepPreviousData, useQuery } from '@tanstack/react-query';
import { IAttachment } from 'app/shared/model/attachment.model';
import { NodeType } from 'app/shared/model/enumerations/node-type-enum.model';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import axios from 'axios';
import { useCallback, useState } from 'react';

export const AdminFetchAttachmentName = 'AdminFetchAttachmentName';
export const useFetchAttachment = (type: NodeType) => {
  const [params, setParams] = useState<{
    page: number;
    size: number;
  }>({
    page: 1,
    size: ITEMS_PER_PAGE,
  });

  const handleNextPage = useCallback((value: number) => {
    setParams(old => ({
      ...old,
      page: value,
    }));
  }, []);

  const fetchAttachment = async () => {
    const requestUrl = `/api/attachments?source.equals=${type}`;
    return await axios.get<IAttachment[]>(requestUrl, {
      params: {
        page: params.page - 1,
        size: params.size,
      },
    });
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [AdminFetchAttachmentName, params, type],
    queryFn: fetchAttachment,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    size: params.size,
    page: params.page,
    total: data?.headers?.xTotalCount,
    handleNextPage,
    refetch,
  };
};
