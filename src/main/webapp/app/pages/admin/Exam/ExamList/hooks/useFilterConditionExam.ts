import { useState } from 'react';

export interface IFilterConditionExam {
  title?: string;
  categoryId?: string;
  status?: string;
  applyTime?: string;
  search?: string;
  rootId?: string;
}

export const useFilterConditionExam = () => {
  const [conditions, setConditions] = useState<IFilterConditionExam>();

  return {
    conditions,
  };
};
