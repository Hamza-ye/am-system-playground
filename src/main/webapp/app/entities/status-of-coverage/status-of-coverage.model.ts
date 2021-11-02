import { IUser } from 'app/entities/user/user.model';

export interface IStatusOfCoverage {
  id?: number;
  code?: string;
  status?: string;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class StatusOfCoverage implements IStatusOfCoverage {
  constructor(
    public id?: number,
    public code?: string,
    public status?: string,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {}
}

export function getStatusOfCoverageIdentifier(statusOfCoverage: IStatusOfCoverage): number | undefined {
  return statusOfCoverage.id;
}
