export interface IMetafield<T, U = string> {
  id?: number;
  ownerResource?: string;
  ownerId?: number;
  namespace?: string;
  key?: T;
  value?: string;
  type?: U;
  description?: string;
}
