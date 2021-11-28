import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';

export interface IImageAlbum {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  title?: string | null;
  coverImageUid?: string | null;
  subtitle?: string | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
  images?: IFileResource[] | null;
}

export class ImageAlbum implements IImageAlbum {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public title?: string | null,
    public coverImageUid?: string | null,
    public subtitle?: string | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null,
    public images?: IFileResource[] | null
  ) {}
}

export function getImageAlbumIdentifier(imageAlbum: IImageAlbum): number | undefined {
  return imageAlbum.id;
}
