import * as dayjs from 'dayjs';
import { IContentPage } from 'app/entities/content-page/content-page.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IMalariaUnitStaffMember } from 'app/entities/malaria-unit-staff-member/malaria-unit-staff-member.model';
import { IUser } from 'app/entities/user/user.model';

export interface IMalariaUnit {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  shortName?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  contentPage?: IContentPage | null;
  organisationUnits?: IOrganisationUnit[] | null;
  malariaUnitStaffMembers?: IMalariaUnitStaffMember[] | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class MalariaUnit implements IMalariaUnit {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public shortName?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public contentPage?: IContentPage | null,
    public organisationUnits?: IOrganisationUnit[] | null,
    public malariaUnitStaffMembers?: IMalariaUnitStaffMember[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {}
}

export function getMalariaUnitIdentifier(malariaUnit: IMalariaUnit): number | undefined {
  return malariaUnit.id;
}
