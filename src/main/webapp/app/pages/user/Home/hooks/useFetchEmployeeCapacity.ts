import { keepPreviousData, useQuery } from '@tanstack/react-query';
import axios from 'axios';

export enum CapacityType {
  SPECIALTY = 'SPECIALTY',
  LEADERSHIP = 'LEADERSHIP',
  CORE_COMPETENCY = 'CORE_COMPETENCY',
}

export interface ICapacites {
  code: string;
  criteria: string;
  id: number;
  realCapacity: number;
  targetCapacity: number;
  type: CapacityType | string;
}
export const FetchEmployeeCapacityName = 'FetchEmployeeCapacityName';
export const useFetchEmployeeCapacity = () => {
  const fetchEmployeeCapacity = async () => {
    return await axios.get<ICapacites[]>(`/api/employee-capacities`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [FetchEmployeeCapacityName],
    queryFn: fetchEmployeeCapacity,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isFetching || isLoading,
    data: data?.data,
    refetch,
  };
};

export interface IBehaviorCapacities {
  id: number;
  code: string;
  level: string;
  criteria: string;
  behavior: string;
  realCapacity: number;
  targetCapacity: number;
  type: CapacityType | string;
}
export const EmployeeBehaviorCapacityName = 'EmployeeBehaviorCapacityName';
export const useFetchEmployeeBehaviorCapacity = () => {
  const fetchEmployeeBehaviorCapacity = async () => {
    return await axios.get<IBehaviorCapacities[]>(`/api/employee-behavior-capacities`);
  };

  const { isLoading, isFetching, data, refetch } = useQuery({
    queryKey: [EmployeeBehaviorCapacityName],
    queryFn: fetchEmployeeBehaviorCapacity,
    placeholderData: keepPreviousData,
  });

  return {
    isLoading: isFetching || isLoading,
    data: data?.data,
    refetch,
  };
};
