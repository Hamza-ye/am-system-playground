import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContentPage, getContentPageIdentifier } from '../content-page.model';

export type EntityResponseType = HttpResponse<IContentPage>;
export type EntityArrayResponseType = HttpResponse<IContentPage[]>;

@Injectable({ providedIn: 'root' })
export class ContentPageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/content-pages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contentPage: IContentPage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contentPage);
    return this.http
      .post<IContentPage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contentPage: IContentPage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contentPage);
    return this.http
      .put<IContentPage>(`${this.resourceUrl}/${getContentPageIdentifier(contentPage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contentPage: IContentPage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contentPage);
    return this.http
      .patch<IContentPage>(`${this.resourceUrl}/${getContentPageIdentifier(contentPage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContentPage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContentPage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContentPageToCollectionIfMissing(
    contentPageCollection: IContentPage[],
    ...contentPagesToCheck: (IContentPage | null | undefined)[]
  ): IContentPage[] {
    const contentPages: IContentPage[] = contentPagesToCheck.filter(isPresent);
    if (contentPages.length > 0) {
      const contentPageCollectionIdentifiers = contentPageCollection.map(contentPageItem => getContentPageIdentifier(contentPageItem)!);
      const contentPagesToAdd = contentPages.filter(contentPageItem => {
        const contentPageIdentifier = getContentPageIdentifier(contentPageItem);
        if (contentPageIdentifier == null || contentPageCollectionIdentifiers.includes(contentPageIdentifier)) {
          return false;
        }
        contentPageCollectionIdentifiers.push(contentPageIdentifier);
        return true;
      });
      return [...contentPagesToAdd, ...contentPageCollection];
    }
    return contentPageCollection;
  }

  protected convertDateFromClient(contentPage: IContentPage): IContentPage {
    return Object.assign({}, contentPage, {
      created: contentPage.created?.isValid() ? contentPage.created.toJSON() : undefined,
      lastUpdated: contentPage.lastUpdated?.isValid() ? contentPage.lastUpdated.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? dayjs(res.body.lastUpdated) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contentPage: IContentPage) => {
        contentPage.created = contentPage.created ? dayjs(contentPage.created) : undefined;
        contentPage.lastUpdated = contentPage.lastUpdated ? dayjs(contentPage.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
