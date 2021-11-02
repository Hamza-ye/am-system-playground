import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILLINSVillageTarget, getLLINSVillageTargetIdentifier } from '../llins-village-target.model';

export type EntityResponseType = HttpResponse<ILLINSVillageTarget>;
export type EntityArrayResponseType = HttpResponse<ILLINSVillageTarget[]>;

@Injectable({ providedIn: 'root' })
export class LLINSVillageTargetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-village-targets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lLINSVillageTarget: ILLINSVillageTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageTarget);
    return this.http
      .post<ILLINSVillageTarget>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lLINSVillageTarget: ILLINSVillageTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageTarget);
    return this.http
      .put<ILLINSVillageTarget>(`${this.resourceUrl}/${getLLINSVillageTargetIdentifier(lLINSVillageTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lLINSVillageTarget: ILLINSVillageTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageTarget);
    return this.http
      .patch<ILLINSVillageTarget>(`${this.resourceUrl}/${getLLINSVillageTargetIdentifier(lLINSVillageTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILLINSVillageTarget>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILLINSVillageTarget[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLLINSVillageTargetToCollectionIfMissing(
    lLINSVillageTargetCollection: ILLINSVillageTarget[],
    ...lLINSVillageTargetsToCheck: (ILLINSVillageTarget | null | undefined)[]
  ): ILLINSVillageTarget[] {
    const lLINSVillageTargets: ILLINSVillageTarget[] = lLINSVillageTargetsToCheck.filter(isPresent);
    if (lLINSVillageTargets.length > 0) {
      const lLINSVillageTargetCollectionIdentifiers = lLINSVillageTargetCollection.map(
        lLINSVillageTargetItem => getLLINSVillageTargetIdentifier(lLINSVillageTargetItem)!
      );
      const lLINSVillageTargetsToAdd = lLINSVillageTargets.filter(lLINSVillageTargetItem => {
        const lLINSVillageTargetIdentifier = getLLINSVillageTargetIdentifier(lLINSVillageTargetItem);
        if (lLINSVillageTargetIdentifier == null || lLINSVillageTargetCollectionIdentifiers.includes(lLINSVillageTargetIdentifier)) {
          return false;
        }
        lLINSVillageTargetCollectionIdentifiers.push(lLINSVillageTargetIdentifier);
        return true;
      });
      return [...lLINSVillageTargetsToAdd, ...lLINSVillageTargetCollection];
    }
    return lLINSVillageTargetCollection;
  }

  protected convertDateFromClient(lLINSVillageTarget: ILLINSVillageTarget): ILLINSVillageTarget {
    return Object.assign({}, lLINSVillageTarget, {
      created: lLINSVillageTarget.created?.isValid() ? lLINSVillageTarget.created.toJSON() : undefined,
      lastUpdated: lLINSVillageTarget.lastUpdated?.isValid() ? lLINSVillageTarget.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((lLINSVillageTarget: ILLINSVillageTarget) => {
        lLINSVillageTarget.created = lLINSVillageTarget.created ? dayjs(lLINSVillageTarget.created) : undefined;
        lLINSVillageTarget.lastUpdated = lLINSVillageTarget.lastUpdated ? dayjs(lLINSVillageTarget.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
