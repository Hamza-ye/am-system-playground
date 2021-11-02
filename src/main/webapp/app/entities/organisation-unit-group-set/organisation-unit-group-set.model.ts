import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IOrganisationUnitGroup } from 'app/entities/organisation-unit-group/organisation-unit-group.model';

export interface IOrganisationUnitGroupSet {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  compulsory?: boolean | null;
  includeSubhierarchyInAnalytics?: boolean | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  organisationUnitGroups?: IOrganisationUnitGroup[] | null;
}

export class OrganisationUnitGroupSet implements IOrganisationUnitGroupSet {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public compulsory?: boolean | null,
    public includeSubhierarchyInAnalytics?: boolean | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public organisationUnitGroups?: IOrganisationUnitGroup[] | null
  ) {
    this.compulsory = this.compulsory ?? false;
    this.includeSubhierarchyInAnalytics = this.includeSubhierarchyInAnalytics ?? false;
  }
}

export function getOrganisationUnitGroupSetIdentifier(organisationUnitGroupSet: IOrganisationUnitGroupSet): number | undefined {
  return organisationUnitGroupSet.id;
}
