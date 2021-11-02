import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILLINSFamilyTarget, getLLINSFamilyTargetIdentifier } from '../llins-family-target.model';

export type EntityResponseType = HttpResponse<ILLINSFamilyTarget>;
export type EntityArrayResponseType = HttpResponse<ILLINSFamilyTarget[]>;

@Injectable({ providedIn: 'root' })
export class LLINSFamilyTargetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-family-targets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lLINSFamilyTarget: ILLINSFamilyTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyTarget);
    return this.http
      .post<ILLINSFamilyTarget>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lLINSFamilyTarget: ILLINSFamilyTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyTarget);
    return this.http
      .put<ILLINSFamilyTarget>(`${this.resourceUrl}/${getLLINSFamilyTargetIdentifier(lLINSFamilyTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lLINSFamilyTarget: ILLINSFamilyTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyTarget);
    return this.http
      .patch<ILLINSFamilyTarget>(`${this.resourceUrl}/${getLLINSFamilyTargetIdentifier(lLINSFamilyTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILLINSFamilyTarget>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILLINSFamilyTarget[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLLINSFamilyTargetToCollectionIfMissing(
    lLINSFamilyTargetCollection: ILLINSFamilyTarget[],
    ...lLINSFamilyTargetsToCheck: (ILLINSFamilyTarget | null | undefined)[]
  ): ILLINSFamilyTarget[] {
    const lLINSFamilyTargets: ILLINSFamilyTarget[] = lLINSFamilyTargetsToCheck.filter(isPresent);
    if (lLINSFamilyTargets.length > 0) {
      const lLINSFamilyTargetCollectionIdentifiers = lLINSFamilyTargetCollection.map(
        lLINSFamilyTargetItem => getLLINSFamilyTargetIdentifier(lLINSFamilyTargetItem)!
      );
      const lLINSFamilyTargetsToAdd = lLINSFamilyTargets.filter(lLINSFamilyTargetItem => {
        const lLINSFamilyTargetIdentifier = getLLINSFamilyTargetIdentifier(lLINSFamilyTargetItem);
        if (lLINSFamilyTargetIdentifier == null || lLINSFamilyTargetCollectionIdentifiers.includes(lLINSFamilyTargetIdentifier)) {
          return false;
        }
        lLINSFamilyTargetCollectionIdentifiers.push(lLINSFamilyTargetIdentifier);
        return true;
      });
      return [...lLINSFamilyTargetsToAdd, ...lLINSFamilyTargetCollection];
    }
    return lLINSFamilyTargetCollection;
  }

  protected convertDateFromClient(lLINSFamilyTarget: ILLINSFamilyTarget): ILLINSFamilyTarget {
    return Object.assign({}, lLINSFamilyTarget, {
      created: lLINSFamilyTarget.created?.isValid() ? lLINSFamilyTarget.created.toJSON() : undefined,
      lastUpdated: lLINSFamilyTarget.lastUpdated?.isValid() ? lLINSFamilyTarget.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((lLINSFamilyTarget: ILLINSFamilyTarget) => {
        lLINSFamilyTarget.created = lLINSFamilyTarget.created ? dayjs(lLINSFamilyTarget.created) : undefined;
        lLINSFamilyTarget.lastUpdated = lLINSFamilyTarget.lastUpdated ? dayjs(lLINSFamilyTarget.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
