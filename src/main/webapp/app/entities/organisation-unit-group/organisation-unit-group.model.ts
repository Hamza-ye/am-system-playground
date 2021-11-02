import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IOrganisationUnitGroupSet } from 'app/entities/organisation-unit-group-set/organisation-unit-group-set.model';

export interface IOrganisationUnitGroup {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  shortName?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  symbol?: string | null;
  color?: string | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  members?: IOrganisationUnit[] | null;
  groupSets?: IOrganisationUnitGroupSet[] | null;
}

export class OrganisationUnitGroup implements IOrganisationUnitGroup {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public shortName?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public symbol?: string | null,
    public color?: string | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public members?: IOrganisationUnit[] | null,
    public groupSets?: IOrganisationUnitGroupSet[] | null
  ) {}
}

export function getOrganisationUnitGroupIdentifier(organisationUnitGroup: IOrganisationUnitGroup): number | undefined {
  return organisationUnitGroup.id;
}
