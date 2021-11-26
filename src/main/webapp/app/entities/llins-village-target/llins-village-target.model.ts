import * as dayjs from 'dayjs';
import { ILLINSVillageReport } from 'app/entities/llins-village-report/llins-village-report.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { IStatusOfCoverage } from 'app/entities/status-of-coverage/status-of-coverage.model';
import { ITeam } from 'app/entities/team/team.model';

export interface ILLINSVillageTarget {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  residentsIndividuals?: number;
  idpsIndividuals?: number;
  residentsFamilies?: number;
  idpsFamilies?: number;
  noOfDaysNeeded?: number | null;
  quantity?: number;
  llinsVillageReports?: ILLINSVillageReport[] | null;
  organisationUnit?: IOrganisationUnit;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  dayPlanned?: IWorkingDay;
  statusOfCoverage?: IStatusOfCoverage | null;
  teamAssigned?: ITeam;
}

export class LLINSVillageTarget implements ILLINSVillageTarget {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public residentsIndividuals?: number,
    public idpsIndividuals?: number,
    public residentsFamilies?: number,
    public idpsFamilies?: number,
    public noOfDaysNeeded?: number | null,
    public quantity?: number,
    public llinsVillageReports?: ILLINSVillageReport[] | null,
    public organisationUnit?: IOrganisationUnit,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public dayPlanned?: IWorkingDay,
    public statusOfCoverage?: IStatusOfCoverage | null,
    public teamAssigned?: ITeam
  ) {}
}

export function getLLINSVillageTargetIdentifier(lLINSVillageTarget: ILLINSVillageTarget): number | undefined {
  return lLINSVillageTarget.id;
}
