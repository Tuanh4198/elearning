import { useQuery } from '@tanstack/react-query';
import { INode } from 'app/shared/model/node.model';
import axios from 'axios';

export const useFetchNodeDetail = (id?: number | string) => {
  const fetchDetail = async () => {
    if (!id) return null;
    return await axios.get<INode>(`/api/nodes/${id}`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: ['useFetchNodeDetail', id],
    queryFn: fetchDetail,
    gcTime: 0,
  });

  return {
    isLoading: isLoading || isFetching,
    data: data?.data,
    refetch,
  };
};
