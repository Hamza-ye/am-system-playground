import * as dayjs from 'dayjs';
import { IContentPage } from 'app/entities/content-page/content-page.model';
import { IActivity } from 'app/entities/activity/activity.model';
import { IUser } from 'app/entities/user/user.model';

export interface IProject {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  displayed?: boolean | null;
  contentPage?: IContentPage | null;
  activities?: IActivity[] | null;
  createdBy?: IUser | null;
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
    public displayed?: boolean | null,
    public contentPage?: IContentPage | null,
    public activities?: IActivity[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {
    this.displayed = this.displayed ?? false;
  }
}

export function getProjectIdentifier(project: IProject): number | undefined {
  return project.id;
}
