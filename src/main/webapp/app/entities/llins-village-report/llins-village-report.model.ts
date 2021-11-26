import * as dayjs from 'dayjs';
import { ILlinsVillageReportHistory } from 'app/entities/llins-village-report-history/llins-village-report-history.model';
import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { ILlinsVillageTarget } from 'app/entities/llins-village-target/llins-village-target.model';
import { ITeam } from 'app/entities/team/team.model';

export interface ILlinsVillageReport {
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
  comment?: string | null;
  llinsVillageReportHistories?: ILlinsVillageReportHistory[] | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  dayReached?: IWorkingDay;
  targetDetails?: ILlinsVillageTarget | null;
  executingTeam?: ITeam;
}

export class LlinsVillageReport implements ILlinsVillageReport {
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
    public comment?: string | null,
    public llinsVillageReportHistories?: ILlinsVillageReportHistory[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public dayReached?: IWorkingDay,
    public targetDetails?: ILlinsVillageTarget | null,
    public executingTeam?: ITeam
  ) {}
}

export function getLlinsVillageReportIdentifier(llinsVillageReport: ILlinsVillageReport): number | undefined {
  return llinsVillageReport.id;
}
