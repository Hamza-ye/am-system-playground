import * as dayjs from 'dayjs';
import { IDemographicData } from 'app/entities/demographic-data/demographic-data.model';
import { IUser } from 'app/entities/user/user.model';

export interface IDemographicDataSource {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  demographicData?: IDemographicData[] | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class DemographicDataSource implements IDemographicDataSource {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public demographicData?: IDemographicData[] | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {}
}

export function getDemographicDataSourceIdentifier(demographicDataSource: IDemographicDataSource): number | undefined {
  return demographicDataSource.id;
}
