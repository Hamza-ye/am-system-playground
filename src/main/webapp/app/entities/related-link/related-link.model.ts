import * as dayjs from 'dayjs';
import { IContentPage } from 'app/entities/content-page/content-page.model';

export interface IRelatedLink {
  id?: number;
  uid?: string;
  name?: string | null;
  created?: dayjs.Dayjs | null;
  lastUpdated?: dayjs.Dayjs | null;
  url?: string | null;
  contentPages?: IContentPage[] | null;
}

export class RelatedLink implements IRelatedLink {
  constructor(
    public id?: number,
    public uid?: string,
    public name?: string | null,
    public created?: dayjs.Dayjs | null,
    public lastUpdated?: dayjs.Dayjs | null,
    public url?: string | null,
    public contentPages?: IContentPage[] | null
  ) {}
}

export function getRelatedLinkIdentifier(relatedLink: IRelatedLink): number | undefined {
  return relatedLink.id;
}
