import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { ICHV } from 'app/entities/chv/chv.model';
import { IPeriod } from 'app/entities/period/period.model';

export interface ICHVMalariaReportVersion1 {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  tested?: number | null;
  positive?: number | null;
  drugsGiven?: number | null;
  suppsGiven?: number | null;
  rdtBalance?: number | null;
  rdtReceived?: number | null;
  rdtUsed?: number | null;
  rdtDamagedLost?: number | null;
  drugsBalance?: number | null;
  drugsReceived?: number | null;
  drugsUsed?: number | null;
  drugsDamagedLost?: number | null;
  suppsBalance?: number | null;
  suppsReceived?: number | null;
  suppsUsed?: number | null;
  suppsDamagedLost?: number | null;
  comment?: string | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  chv?: ICHV;
  period?: IPeriod;
}

export class CHVMalariaReportVersion1 implements ICHVMalariaReportVersion1 {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public tested?: number | null,
    public positive?: number | null,
    public drugsGiven?: number | null,
    public suppsGiven?: number | null,
    public rdtBalance?: number | null,
    public rdtReceived?: number | null,
    public rdtUsed?: number | null,
    public rdtDamagedLost?: number | null,
    public drugsBalance?: number | null,
    public drugsReceived?: number | null,
    public drugsUsed?: number | null,
    public drugsDamagedLost?: number | null,
    public suppsBalance?: number | null,
    public suppsReceived?: number | null,
    public suppsUsed?: number | null,
    public suppsDamagedLost?: number | null,
    public comment?: string | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public chv?: ICHV,
    public period?: IPeriod
  ) {}
}

export function getCHVMalariaReportVersion1Identifier(cHVMalariaReportVersion1: ICHVMalariaReportVersion1): number | undefined {
  return cHVMalariaReportVersion1.id;
}
