import * as dayjs from 'dayjs';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { IUser } from 'app/entities/user/user.model';

export interface IExternalFileResource {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  accessToken?: string | null;
  expires?: dayjs.Dayjs | null;
  fileResource?: IFileResource | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class ExternalFileResource implements IExternalFileResource {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public accessToken?: string | null,
    public expires?: dayjs.Dayjs | null,
    public fileResource?: IFileResource | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {}
}

export function getExternalFileResourceIdentifier(externalFileResource: IExternalFileResource): number | undefined {
  return externalFileResource.id;
}
