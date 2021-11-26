import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { ILlinsFamilyReport } from 'app/entities/llins-family-report/llins-family-report.model';
import { FamilyType } from 'app/entities/enumerations/family-type.model';

export interface ILlinsFamilyReportHistory {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  documentNo?: number | null;
  maleIndividuals?: number | null;
  femaleIndividuals?: number | null;
  lessThan5Males?: number | null;
  lessThan5Females?: number | null;
  pregnantWomen?: number | null;
  quantityReceived?: number | null;
  familyType?: FamilyType;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  dayReached?: IWorkingDay;
  llinsFamilyReport?: ILlinsFamilyReport;
}

export class LlinsFamilyReportHistory implements ILlinsFamilyReportHistory {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public documentNo?: number | null,
    public maleIndividuals?: number | null,
    public femaleIndividuals?: number | null,
    public lessThan5Males?: number | null,
    public lessThan5Females?: number | null,
    public pregnantWomen?: number | null,
    public quantityReceived?: number | null,
    public familyType?: FamilyType,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public dayReached?: IWorkingDay,
    public llinsFamilyReport?: ILlinsFamilyReport
  ) {}
}

export function getLlinsFamilyReportHistoryIdentifier(llinsFamilyReportHistory: ILlinsFamilyReportHistory): number | undefined {
  return llinsFamilyReportHistory.id;
}
