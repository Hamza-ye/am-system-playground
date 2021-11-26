import * as dayjs from 'dayjs';
import { IPerson } from 'app/entities/person/person.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IUser } from 'app/entities/user/user.model';
import { ICHVTeam } from 'app/entities/chv-team/chv-team.model';

export interface ICHV {
  id?: number;
  uid?: string;
  code?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  mobile?: string | null;
  person?: IPerson | null;
  coveredSubVillages?: IOrganisationUnit[] | null;
  district?: IOrganisationUnit;
  homeSubvillage?: IOrganisationUnit | null;
  managedByHf?: IOrganisationUnit | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  supervisionTeams?: ICHVTeam[] | null;
}

export class CHV implements ICHV {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public mobile?: string | null,
    public person?: IPerson | null,
    public coveredSubVillages?: IOrganisationUnit[] | null,
    public district?: IOrganisationUnit,
    public homeSubvillage?: IOrganisationUnit | null,
    public managedByHf?: IOrganisationUnit | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public supervisionTeams?: ICHVTeam[] | null
  ) {}
}

export function getCHVIdentifier(cHV: ICHV): number | undefined {
  return cHV.id;
}
