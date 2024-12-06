export enum AssignStrategyEnum {
  ANY_USERS = 'ANY_USERS',
  SPEC_POSITIONS = 'SPEC_POSITIONS',
  SPEC_USERS = 'SPEC_USERS',
}

export const AssignStrategyTitle = {
  [AssignStrategyEnum.ANY_USERS]: 'Toàn công ty',
  [AssignStrategyEnum.SPEC_POSITIONS]: 'Theo vị trí',
  [AssignStrategyEnum.SPEC_USERS]: 'Tải file lên',
};
