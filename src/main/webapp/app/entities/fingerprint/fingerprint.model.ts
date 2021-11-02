import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IFamily } from 'app/entities/family/family.model';

export interface IFingerprint {
  id?: number;
  uid?: string;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  fingerprintUrl?: string | null;
  fingerprintOwner?: string | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
  family?: IFamily | null;
}

export class Fingerprint implements IFingerprint {
  constructor(
    public id?: number,
    public uid?: string,
    public description?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public fingerprintUrl?: string | null,
    public fingerprintOwner?: string | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public family?: IFamily | null
  ) {}
}

export function getFingerprintIdentifier(fingerprint: IFingerprint): number | undefined {
  return fingerprint.id;
}
