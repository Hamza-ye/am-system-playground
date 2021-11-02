import * as dayjs from 'dayjs';
import { IFamilyHead } from 'app/entities/family-head/family-head.model';
import { IDataProvider } from 'app/entities/data-provider/data-provider.model';
import { IFingerprint } from 'app/entities/fingerprint/fingerprint.model';
import { ILLINSFamilyTarget } from 'app/entities/llins-family-target/llins-family-target.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { IUser } from 'app/entities/user/user.model';

export interface IFamily {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  familyNo?: number;
  address?: string | null;
  familyHeads?: IFamilyHead[] | null;
  dataProviders?: IDataProvider[] | null;
  fingerprints?: IFingerprint[] | null;
  llinsFamilyTargets?: ILLINSFamilyTarget[] | null;
  organisationUnit?: IOrganisationUnit;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class Family implements IFamily {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public familyNo?: number,
    public address?: string | null,
    public familyHeads?: IFamilyHead[] | null,
    public dataProviders?: IDataProvider[] | null,
    public fingerprints?: IFingerprint[] | null,
    public llinsFamilyTargets?: ILLINSFamilyTarget[] | null,
    public organisationUnit?: IOrganisationUnit,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {}
}

export function getFamilyIdentifier(family: IFamily): number | undefined {
  return family.id;
}
