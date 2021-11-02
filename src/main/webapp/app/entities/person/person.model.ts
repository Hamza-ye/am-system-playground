import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IPersonAuthorityGroup } from 'app/entities/person-authority-group/person-authority-group.model';
import { IPeopleGroup } from 'app/entities/people-group/people-group.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IPerson {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  uuid?: string | null;
  gender?: Gender | null;
  mobile?: string | null;
  lastLogin?: dayjs.Dayjs | null;
  login?: string | null;
  selfRegistered?: boolean | null;
  disabled?: boolean | null;
  userInfo?: IUser | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  organisationUnits?: IOrganisationUnit[] | null;
  dataViewOrganisationUnits?: IOrganisationUnit[] | null;
  personAuthorityGroups?: IPersonAuthorityGroup[] | null;
  groups?: IPeopleGroup[] | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public uuid?: string | null,
    public gender?: Gender | null,
    public mobile?: string | null,
    public lastLogin?: dayjs.Dayjs | null,
    public login?: string | null,
    public selfRegistered?: boolean | null,
    public disabled?: boolean | null,
    public userInfo?: IUser | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public organisationUnits?: IOrganisationUnit[] | null,
    public dataViewOrganisationUnits?: IOrganisationUnit[] | null,
    public personAuthorityGroups?: IPersonAuthorityGroup[] | null,
    public groups?: IPeopleGroup[] | null
  ) {
    this.selfRegistered = this.selfRegistered ?? false;
    this.disabled = this.disabled ?? false;
  }
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
