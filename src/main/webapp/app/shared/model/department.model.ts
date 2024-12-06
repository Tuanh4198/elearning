export interface IDepartment {
  id: number;
  code: string;
  parentId?: number;
  children?: IDepartment[];
  name: string;
  managerCode?: string | null;
  representCode?: string | null;
}
