import * as dayjs from 'dayjs';
import { ILLINSVillageReportHistory } from 'app/entities/llins-village-report-history/llins-village-report-history.model';
import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { ILLINSVillageTarget } from 'app/entities/llins-village-target/llins-village-target.model';
import { ITeam } from 'app/entities/team/team.model';

export interface ILLINSVillageReport {
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
  llinsVillageReportHistories?: ILLINSVillageReportHistory[] | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  dayReached?: IWorkingDay;
  targetDetails?: ILLINSVillageTarget | null;
  executingTeam?: ITeam;
}

export class LLINSVillageReport implements ILLINSVillageReport {
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
    public llinsVillageReportHistories?: ILLINSVillageReportHistory[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public dayReached?: IWorkingDay,
    public targetDetails?: ILLINSVillageTarget | null,
    public executingTeam?: ITeam
  ) {}
}

export function getLLINSVillageReportIdentifier(lLINSVillageReport: ILLINSVillageReport): number | undefined {
  return lLINSVillageReport.id;
}
