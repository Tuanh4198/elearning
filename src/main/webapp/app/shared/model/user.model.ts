export interface IUser {
  id?: any;
  login?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  activated?: boolean;
  langKey?: string;
  authorities?: any[];
  createdBy?: string;
  createdDate?: Date | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date | null;
  password?: string;
}

export const defaultValue: Readonly<IUser> = {
  id: '',
  login: '',
  firstName: '',
  lastName: '',
  email: '',
  activated: true,
  langKey: '',
  authorities: [],
  createdBy: '',
  createdDate: null,
  lastModifiedBy: '',
  lastModifiedDate: null,
  password: '',
};

export const courseEmployees: Required<
  Pick<IUser, 'id' | 'firstName' | 'lastName' | 'activated'> & {
    positionName: string;
  }
>[] = [
  {
    id: 'yd23806',
    firstName: 'An',
    lastName: 'Tran',
    activated: true,
    positionName: 'Chuyên gia xảo quyệt',
  },
  {
    id: 'yd23806',
    firstName: 'An',
    lastName: 'Tran',
    activated: true,
    positionName: 'Chuyên gia xảo quyệt',
  },
  {
    id: 'yd23806',
    firstName: 'An',
    lastName: 'Tran',
    activated: true,
    positionName: 'Chuyên gia xảo quyệt',
  },
  {
    id: 'yd23806',
    firstName: 'An',
    lastName: 'Tran',
    activated: true,
    positionName: 'Chuyên gia xảo quyệt',
  },
  {
    id: 'yd23806',
    firstName: 'An',
    lastName: 'Tran',
    activated: true,
    positionName: 'Chuyên gia xảo quyệt',
  },
  {
    id: 'yd23806',
    firstName: 'An',
    lastName: 'Tran',
    activated: true,
    positionName: 'Chuyên gia xảo quyệt',
  },
];
