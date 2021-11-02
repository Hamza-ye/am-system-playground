import * as dayjs from 'dayjs';
import { IActivity } from 'app/entities/activity/activity.model';
import { IUser } from 'app/entities/user/user.model';

export interface IProject {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  isDisplayed?: boolean | null;
  activities?: IActivity[] | null;
  user?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public isDisplayed?: boolean | null,
    public activities?: IActivity[] | null,
    public user?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {
    this.isDisplayed = this.isDisplayed ?? false;
  }
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
