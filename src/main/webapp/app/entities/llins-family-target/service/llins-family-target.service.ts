import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILlinsFamilyTarget, getLlinsFamilyTargetIdentifier } from '../llins-family-target.model';

export type EntityResponseType = HttpResponse<ILlinsFamilyTarget>;
export type EntityArrayResponseType = HttpResponse<ILlinsFamilyTarget[]>;

@Injectable({ providedIn: 'root' })
export class LlinsFamilyTargetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-family-targets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(llinsFamilyTarget: ILlinsFamilyTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyTarget);
    return this.http
      .post<ILlinsFamilyTarget>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(llinsFamilyTarget: ILlinsFamilyTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyTarget);
    return this.http
      .put<ILlinsFamilyTarget>(`${this.resourceUrl}/${getLlinsFamilyTargetIdentifier(llinsFamilyTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(llinsFamilyTarget: ILlinsFamilyTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyTarget);
    return this.http
      .patch<ILlinsFamilyTarget>(`${this.resourceUrl}/${getLlinsFamilyTargetIdentifier(llinsFamilyTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILlinsFamilyTarget>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILlinsFamilyTarget[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLlinsFamilyTargetToCollectionIfMissing(
    llinsFamilyTargetCollection: ILlinsFamilyTarget[],
    ...llinsFamilyTargetsToCheck: (ILlinsFamilyTarget | null | undefined)[]
  ): ILlinsFamilyTarget[] {
    const llinsFamilyTargets: ILlinsFamilyTarget[] = llinsFamilyTargetsToCheck.filter(isPresent);
    if (llinsFamilyTargets.length > 0) {
      const llinsFamilyTargetCollectionIdentifiers = llinsFamilyTargetCollection.map(
        llinsFamilyTargetItem => getLlinsFamilyTargetIdentifier(llinsFamilyTargetItem)!
      );
      const llinsFamilyTargetsToAdd = llinsFamilyTargets.filter(llinsFamilyTargetItem => {
        const llinsFamilyTargetIdentifier = getLlinsFamilyTargetIdentifier(llinsFamilyTargetItem);
        if (llinsFamilyTargetIdentifier == null || llinsFamilyTargetCollectionIdentifiers.includes(llinsFamilyTargetIdentifier)) {
          return false;
        }
        llinsFamilyTargetCollectionIdentifiers.push(llinsFamilyTargetIdentifier);
        return true;
      });
      return [...llinsFamilyTargetsToAdd, ...llinsFamilyTargetCollection];
    }
    return llinsFamilyTargetCollection;
  }

  protected convertDateFromClient(llinsFamilyTarget: ILlinsFamilyTarget): ILlinsFamilyTarget {
    return Object.assign({}, llinsFamilyTarget, {
      created: llinsFamilyTarget.created?.isValid() ? llinsFamilyTarget.created.toJSON() : undefined,
      lastUpdated: llinsFamilyTarget.lastUpdated?.isValid() ? llinsFamilyTarget.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((llinsFamilyTarget: ILlinsFamilyTarget) => {
        llinsFamilyTarget.created = llinsFamilyTarget.created ? dayjs(llinsFamilyTarget.created) : undefined;
        llinsFamilyTarget.lastUpdated = llinsFamilyTarget.lastUpdated ? dayjs(llinsFamilyTarget.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
