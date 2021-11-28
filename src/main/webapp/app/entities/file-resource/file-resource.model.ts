import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IImageAlbum } from 'app/entities/image-album/image-album.model';
import { FileResourceDomain } from 'app/entities/enumerations/file-resource-domain.model';

export interface IFileResource {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  contentType?: string | null;
  contentLength?: string | null;
  contentMd5?: string | null;
  storageKey?: string | null;
  assigned?: boolean | null;
  domain?: FileResourceDomain | null;
  hasMultipleStorageFiles?: boolean | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  imageAlbums?: IImageAlbum[] | null;
}

export class FileResource implements IFileResource {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public contentType?: string | null,
    public contentLength?: string | null,
    public contentMd5?: string | null,
    public storageKey?: string | null,
    public assigned?: boolean | null,
    public domain?: FileResourceDomain | null,
    public hasMultipleStorageFiles?: boolean | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public imageAlbums?: IImageAlbum[] | null
  ) {
    this.assigned = this.assigned ?? false;
    this.hasMultipleStorageFiles = this.hasMultipleStorageFiles ?? false;
  }
}

export function getFileResourceIdentifier(fileResource: IFileResource): number | undefined {
  return fileResource.id;
}
