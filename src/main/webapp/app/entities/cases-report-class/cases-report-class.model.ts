import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ICasesReportClass {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  shortName?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class CasesReportClass implements ICasesReportClass {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public shortName?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {}
}

export function getCasesReportClassIdentifier(casesReportClass: ICasesReportClass): number | undefined {
  return casesReportClass.id;
}
