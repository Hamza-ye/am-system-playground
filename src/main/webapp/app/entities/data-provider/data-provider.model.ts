import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IFamily } from 'app/entities/family/family.model';

export interface IDataProvider {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  mobile?: string | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  family?: IFamily | null;
}

export class DataProvider implements IDataProvider {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public mobile?: string | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public family?: IFamily | null
  ) {}
}

export function getDataProviderIdentifier(dataProvider: IDataProvider): number | undefined {
  return dataProvider.id;
}
