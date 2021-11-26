import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IPerson } from 'app/entities/person/person.model';
import { IMalariaUnit } from 'app/entities/malaria-unit/malaria-unit.model';
import { MalariaUnitMemberType } from 'app/entities/enumerations/malaria-unit-member-type.model';

export interface IMalariaUnitStaffMember {
  id?: number;
  uid?: string;
  code?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  memberNo?: number;
  memberType?: MalariaUnitMemberType;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  person?: IPerson | null;
  malariaUnit?: IMalariaUnit;
}

export class MalariaUnitStaffMember implements IMalariaUnitStaffMember {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public memberNo?: number,
    public memberType?: MalariaUnitMemberType,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public person?: IPerson | null,
    public malariaUnit?: IMalariaUnit
  ) {}
}

export function getMalariaUnitStaffMemberIdentifier(malariaUnitStaffMember: IMalariaUnitStaffMember): number | undefined {
  return malariaUnitStaffMember.id;
}
