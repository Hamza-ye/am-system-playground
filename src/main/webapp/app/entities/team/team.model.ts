import * as dayjs from 'dayjs';
import { IWhMovement } from 'app/entities/wh-movement/wh-movement.model';
import { ILlinsVillageTarget } from 'app/entities/llins-village-target/llins-village-target.model';
import { ILlinsVillageReport } from 'app/entities/llins-village-report/llins-village-report.model';
import { ILlinsFamilyTarget } from 'app/entities/llins-family-target/llins-family-target.model';
import { ILlinsFamilyReport } from 'app/entities/llins-family-report/llins-family-report.model';
import { IUser } from 'app/entities/user/user.model';
import { IPerson } from 'app/entities/person/person.model';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { TeamType } from 'app/entities/enumerations/team-type.model';

export interface ITeam {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  teamNo?: number;
  teamType?: TeamType;
  whMovements?: IWhMovement[] | null;
  llinsVillageTargets?: ILlinsVillageTarget[] | null;
  llinsVillageReports?: ILlinsVillageReport[] | null;
  llinsFamilyTargets?: ILlinsFamilyTarget[] | null;
  llinsFamilyReports?: ILlinsFamilyReport[] | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  person?: IPerson | null;
  assignedToWarehouses?: IWarehouse[] | null;
}

export class Team implements ITeam {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public teamNo?: number,
    public teamType?: TeamType,
    public whMovements?: IWhMovement[] | null,
    public llinsVillageTargets?: ILlinsVillageTarget[] | null,
    public llinsVillageReports?: ILlinsVillageReport[] | null,
    public llinsFamilyTargets?: ILlinsFamilyTarget[] | null,
    public llinsFamilyReports?: ILlinsFamilyReport[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public person?: IPerson | null,
    public assignedToWarehouses?: IWarehouse[] | null
  ) {}
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}
