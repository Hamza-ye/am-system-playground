import * as dayjs from "dayjs";
import {IUser} from "../../entities/user/user.model";

export interface IdentifiableObject {
  id?: string;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;

  shortName?: string;
  description?: string;
  displayShortName?: string;
  displayDescription?: string;
}
