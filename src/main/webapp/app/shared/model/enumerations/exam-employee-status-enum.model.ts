export enum ExamEmployeeStatusEnum {
  NOT_ATTENDED = 'NOT_ATTENDED',
  PASS = 'PASS',
  NOT_PASS = 'NOT_PASS',
  EXPIRED = 'EXPIRED',
  NOT_COMPLETED = 'NOT_COMPLETED',
}

export const ExamEmployeeStatusEnumTitle = {
  [ExamEmployeeStatusEnum.NOT_ATTENDED]: {
    color: '#000000',
    bg: '#FAB005',
    label: 'Chưa làm',
  },
  [ExamEmployeeStatusEnum.PASS]: {
    color: '#E6FCF5',
    bg: '#12B886',
    label: 'Đã hoàn thành',
  },
  [ExamEmployeeStatusEnum.NOT_PASS]: {
    color: '#FFF5F5',
    bg: '#FA5252',
    label: 'Chưa hoàn thành',
  },
  [ExamEmployeeStatusEnum.EXPIRED]: {
    color: '#FFF5F5',
    bg: '#FA5252',
    label: 'Hết hạn',
  },
};
