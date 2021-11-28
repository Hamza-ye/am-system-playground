import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRelatedLink, getRelatedLinkIdentifier } from '../related-link.model';

export type EntityResponseType = HttpResponse<IRelatedLink>;
export type EntityArrayResponseType = HttpResponse<IRelatedLink[]>;

@Injectable({ providedIn: 'root' })
export class RelatedLinkService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/related-links');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(relatedLink: IRelatedLink): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(relatedLink);
    return this.http
      .post<IRelatedLink>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(relatedLink: IRelatedLink): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(relatedLink);
    return this.http
      .put<IRelatedLink>(`${this.resourceUrl}/${getRelatedLinkIdentifier(relatedLink) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(relatedLink: IRelatedLink): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(relatedLink);
    return this.http
      .patch<IRelatedLink>(`${this.resourceUrl}/${getRelatedLinkIdentifier(relatedLink) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRelatedLink>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRelatedLink[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRelatedLinkToCollectionIfMissing(
    relatedLinkCollection: IRelatedLink[],
    ...relatedLinksToCheck: (IRelatedLink | null | undefined)[]
  ): IRelatedLink[] {
    const relatedLinks: IRelatedLink[] = relatedLinksToCheck.filter(isPresent);
    if (relatedLinks.length > 0) {
      const relatedLinkCollectionIdentifiers = relatedLinkCollection.map(relatedLinkItem => getRelatedLinkIdentifier(relatedLinkItem)!);
      const relatedLinksToAdd = relatedLinks.filter(relatedLinkItem => {
        const relatedLinkIdentifier = getRelatedLinkIdentifier(relatedLinkItem);
        if (relatedLinkIdentifier == null || relatedLinkCollectionIdentifiers.includes(relatedLinkIdentifier)) {
          return false;
        }
        relatedLinkCollectionIdentifiers.push(relatedLinkIdentifier);
        return true;
      });
      return [...relatedLinksToAdd, ...relatedLinkCollection];
    }
    return relatedLinkCollection;
  }

  protected convertDateFromClient(relatedLink: IRelatedLink): IRelatedLink {
    return Object.assign({}, relatedLink, {
      created: relatedLink.created?.isValid() ? relatedLink.created.toJSON() : undefined,
      lastUpdated: relatedLink.lastUpdated?.isValid() ? relatedLink.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((relatedLink: IRelatedLink) => {
        relatedLink.created = relatedLink.created ? dayjs(relatedLink.created) : undefined;
        relatedLink.lastUpdated = relatedLink.lastUpdated ? dayjs(relatedLink.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
