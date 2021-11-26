import * as dayjs from 'dayjs';
import { ILlinsFamilyReportHistory } from 'app/entities/llins-family-report-history/llins-family-report-history.model';
import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { ILlinsFamilyTarget } from 'app/entities/llins-family-target/llins-family-target.model';
import { ITeam } from 'app/entities/team/team.model';
import { FamilyType } from 'app/entities/enumerations/family-type.model';

export interface ILlinsFamilyReport {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  checkNo?: number | null;
  maleIndividuals?: number;
  femaleIndividuals?: number;
  lessThan5Males?: number;
  lessThan5Females?: number;
  pregnantWomen?: number;
  quantityReceived?: number;
  familyType?: FamilyType;
  comment?: string | null;
  llinsFamilyReportHistories?: ILlinsFamilyReportHistory[] | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  dayReached?: IWorkingDay;
  targetDetails?: ILlinsFamilyTarget | null;
  executingTeam?: ITeam;
}

export class LlinsFamilyReport implements ILlinsFamilyReport {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public checkNo?: number | null,
    public maleIndividuals?: number,
    public femaleIndividuals?: number,
    public lessThan5Males?: number,
    public lessThan5Females?: number,
    public pregnantWomen?: number,
    public quantityReceived?: number,
    public familyType?: FamilyType,
    public comment?: string | null,
    public llinsFamilyReportHistories?: ILlinsFamilyReportHistory[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public dayReached?: IWorkingDay,
    public targetDetails?: ILlinsFamilyTarget | null,
    public executingTeam?: ITeam
  ) {}
}

export function getLlinsFamilyReportIdentifier(llinsFamilyReport: ILlinsFamilyReport): number | undefined {
  return llinsFamilyReport.id;
}
