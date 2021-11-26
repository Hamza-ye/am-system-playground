import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IPersonAuthorityGroup {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  members?: IPerson[] | null;
}

export class PersonAuthorityGroup implements IPersonAuthorityGroup {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public members?: IPerson[] | null
  ) {}
}

export function getPersonAuthorityGroupIdentifier(personAuthorityGroup: IPersonAuthorityGroup): number | undefined {
  return personAuthorityGroup.id;
}
