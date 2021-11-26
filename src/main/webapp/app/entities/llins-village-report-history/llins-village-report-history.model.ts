import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { ILLINSVillageReport } from 'app/entities/llins-village-report/llins-village-report.model';

export interface ILLINSVillageReportHistory {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  houses?: number;
  residentHousehold?: number;
  idpsHousehold?: number;
  maleIndividuals?: number | null;
  femaleIndividuals?: number | null;
  lessThan5Males?: number | null;
  lessThan5Females?: number | null;
  pregnantWomen?: number | null;
  quantityReceived?: number;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  dayReached?: IWorkingDay;
  llinsVillageReport?: ILLINSVillageReport;
}

export class LLINSVillageReportHistory implements ILLINSVillageReportHistory {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public houses?: number,
    public residentHousehold?: number,
    public idpsHousehold?: number,
    public maleIndividuals?: number | null,
    public femaleIndividuals?: number | null,
    public lessThan5Males?: number | null,
    public lessThan5Females?: number | null,
    public pregnantWomen?: number | null,
    public quantityReceived?: number,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public dayReached?: IWorkingDay,
    public llinsVillageReport?: ILLINSVillageReport
  ) {}
}

export function getLLINSVillageReportHistoryIdentifier(lLINSVillageReportHistory: ILLINSVillageReportHistory): number | undefined {
  return lLINSVillageReportHistory.id;
}
