import * as dayjs from 'dayjs';
import { ILLINSFamilyReport } from 'app/entities/llins-family-report/llins-family-report.model';
import { IUser } from 'app/entities/user/user.model';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { IFamily } from 'app/entities/family/family.model';
import { ITeam } from 'app/entities/team/team.model';
import { FamilyType } from 'app/entities/enumerations/family-type.model';
import { StatusOfFamilyTarget } from 'app/entities/enumerations/status-of-family-target.model';

export interface ILLINSFamilyTarget {
  id?: number;
  uid?: string;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  residentsIndividualsPlanned?: number;
  idpsIndividualsPlanned?: number;
  quantityPlanned?: number;
  familyType?: FamilyType;
  statusOfFamilyTarget?: StatusOfFamilyTarget;
  llinsFamilyReports?: ILLINSFamilyReport[] | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  dayPlanned?: IWorkingDay;
  family?: IFamily;
  teamAssigned?: ITeam;
}

export class LLINSFamilyTarget implements ILLINSFamilyTarget {
  constructor(
    public id?: number,
    public uid?: string,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public residentsIndividualsPlanned?: number,
    public idpsIndividualsPlanned?: number,
    public quantityPlanned?: number,
    public familyType?: FamilyType,
    public statusOfFamilyTarget?: StatusOfFamilyTarget,
    public llinsFamilyReports?: ILLINSFamilyReport[] | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public dayPlanned?: IWorkingDay,
    public family?: IFamily,
    public teamAssigned?: ITeam
  ) {}
}

export function getLLINSFamilyTargetIdentifier(lLINSFamilyTarget: ILLINSFamilyTarget): number | undefined {
  return lLINSFamilyTarget.id;
}
