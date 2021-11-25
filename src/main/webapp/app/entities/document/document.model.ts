import * as dayjs from 'dayjs';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { IUser } from 'app/entities/user/user.model';

export interface IDocument {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  url?: string | null;
  external?: boolean | null;
  contentType?: string | null;
  attachment?: boolean | null;
  fileResource?: IFileResource | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public url?: string | null,
    public external?: boolean | null,
    public contentType?: string | null,
    public attachment?: boolean | null,
    public fileResource?: IFileResource | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {
    this.external = this.external ?? false;
    this.attachment = this.attachment ?? false;
  }
}

export function getDocumentIdentifier(document: IDocument): number | undefined {
  return document.id;
}
