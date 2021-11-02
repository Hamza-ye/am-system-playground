import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IPerson } from 'app/entities/person/person.model';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVTeamType } from 'app/entities/enumerations/chv-team-type.model';

export interface ICHVTeam {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  teamNo?: string;
  teamType?: CHVTeamType;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  person?: IPerson | null;
  responsibleForChvs?: ICHV[] | null;
}

export class CHVTeam implements ICHVTeam {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public teamNo?: string,
    public teamType?: CHVTeamType,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public person?: IPerson | null,
    public responsibleForChvs?: ICHV[] | null
  ) {}
}

export function getCHVTeamIdentifier(cHVTeam: ICHVTeam): number | undefined {
  return cHVTeam.id;
}
