import * as dayjs from 'dayjs';
import { IImageAlbum } from 'app/entities/image-album/image-album.model';
import { IUser } from 'app/entities/user/user.model';

export interface IContentPage {
  id?: number;
  uid?: string;
  code?: string | null;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  title?: string | null;
  subtitle?: string | null;
  content?: string | null;
  active?: boolean | null;
  visitedCount?: number | null;
  imageAlbum?: IImageAlbum | null;
  createdBy?: IUser | null;
  lastUpdatedBy?: IUser | null;
}

export class ContentPage implements IContentPage {
  constructor(
    public id?: number,
    public uid?: string,
    public code?: string | null,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public title?: string | null,
    public subtitle?: string | null,
    public content?: string | null,
    public active?: boolean | null,
    public visitedCount?: number | null,
    public imageAlbum?: IImageAlbum | null,
    public createdBy?: IUser | null,
    public lastUpdatedBy?: IUser | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getContentPageIdentifier(contentPage: IContentPage): number | undefined {
  return contentPage.id;
}
