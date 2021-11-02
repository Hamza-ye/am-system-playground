import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IPerson } from 'app/entities/person/person.model';

export interface IPeopleGroup {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  uuid?: string | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  members?: IPerson[] | null;
  managedByGroups?: IPeopleGroup[] | null;
  managedGroups?: IPeopleGroup[] | null;
}

export class PeopleGroup implements IPeopleGroup {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public uuid?: string | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public members?: IPerson[] | null,
    public managedByGroups?: IPeopleGroup[] | null,
    public managedGroups?: IPeopleGroup[] | null
  ) {}
}

export function getPeopleGroupIdentifier(peopleGroup: IPeopleGroup): number | undefined {
  return peopleGroup.id;
}
