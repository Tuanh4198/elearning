import React, { Fragment } from 'react';
import { BlockCapacities } from 'app/pages/user/Home/components/BlockCapacities';
import { BlockCapacityChart } from 'app/pages/user/Home/components/BlockCapacityChart';
import { useFetchEmployeeBehaviorCapacity, useFetchEmployeeCapacity } from 'app/pages/user/Home/hooks/useFetchEmployeeCapacity';

export const BlockEmployeeCapacity = () => {
  const { isLoading: isLoadingEmployeeCapacity, data: employeeCapacity } = useFetchEmployeeCapacity();

  const { isLoading: isLoadingEmployeeBehaviorCapacity, data: employeeBehaviorCapacity } = useFetchEmployeeBehaviorCapacity();

  return (
    <Fragment>
      <BlockCapacityChart isLoading={isLoadingEmployeeCapacity || isLoadingEmployeeBehaviorCapacity} employeeCapacity={employeeCapacity} />
      <BlockCapacities
        isLoading={isLoadingEmployeeCapacity || isLoadingEmployeeBehaviorCapacity}
        employeeCapacity={employeeCapacity}
        employeeBehaviorCapacity={employeeBehaviorCapacity}
      />
    </Fragment>
  );
};
