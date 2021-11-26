import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChv, getChvIdentifier } from '../chv.model';

export type EntityResponseType = HttpResponse<IChv>;
export type EntityArrayResponseType = HttpResponse<IChv[]>;

@Injectable({ providedIn: 'root' })
export class ChvService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chvs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chv: IChv): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chv);
    return this.http
      .post<IChv>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chv: IChv): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chv);
    return this.http
      .put<IChv>(`${this.resourceUrl}/${getChvIdentifier(chv) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(chv: IChv): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chv);
    return this.http
      .patch<IChv>(`${this.resourceUrl}/${getChvIdentifier(chv) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChv>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChv[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChvToCollectionIfMissing(chvCollection: IChv[], ...chvsToCheck: (IChv | null | undefined)[]): IChv[] {
    const chvs: IChv[] = chvsToCheck.filter(isPresent);
    if (chvs.length > 0) {
      const chvCollectionIdentifiers = chvCollection.map(chvItem => getChvIdentifier(chvItem)!);
      const chvsToAdd = chvs.filter(chvItem => {
        const chvIdentifier = getChvIdentifier(chvItem);
        if (chvIdentifier == null || chvCollectionIdentifiers.includes(chvIdentifier)) {
          return false;
        }
        chvCollectionIdentifiers.push(chvIdentifier);
        return true;
      });
      return [...chvsToAdd, ...chvCollection];
    }
    return chvCollection;
  }

  protected convertDateFromClient(chv: IChv): IChv {
    return Object.assign({}, chv, {
      created: chv.created?.isValid() ? chv.created.toJSON() : undefined,
      lastUpdated: chv.lastUpdated?.isValid() ? chv.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((chv: IChv) => {
        chv.created = chv.created ? dayjs(chv.created) : undefined;
        chv.lastUpdated = chv.lastUpdated ? dayjs(chv.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
