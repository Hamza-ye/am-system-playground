import * as dayjs from 'dayjs';
import { IContentPage } from 'app/entities/content-page/content-page.model';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { IUser } from 'app/entities/user/user.model';
import { IProject } from 'app/entities/project/project.model';

export interface IActivity {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  active?: boolean | null;
  displayed?: boolean | null;
  contentPage?: IContentPage | null;
  warehouses?: IWarehouse[] | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  project?: IProject;
}

export class Activity implements IActivity {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public active?: boolean | null,
    public displayed?: boolean | null,
    public contentPage?: IContentPage | null,
    public warehouses?: IWarehouse[] | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public project?: IProject
  ) {
    this.active = this.active ?? false;
    this.displayed = this.displayed ?? false;
  }
}

export function getActivityIdentifier(activity: IActivity): number | undefined {
  return activity.id;
}
