import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IPerson } from 'app/entities/person/person.model';
import { IChv } from 'app/entities/chv/chv.model';
import { ChvTeamType } from 'app/entities/enumerations/chv-team-type.model';

export interface IChvTeam {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  teamNo?: string;
  teamType?: ChvTeamType;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  person?: IPerson | null;
  responsibleForChvs?: IChv[] | null;
}

export class ChvTeam implements IChvTeam {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public teamNo?: string,
    public teamType?: ChvTeamType,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public person?: IPerson | null,
    public responsibleForChvs?: IChv[] | null
  ) {}
}

export function getChvTeamIdentifier(chvTeam: IChvTeam): number | undefined {
  return chvTeam.id;
}
