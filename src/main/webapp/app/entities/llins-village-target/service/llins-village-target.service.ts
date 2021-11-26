import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILlinsVillageTarget, getLlinsVillageTargetIdentifier } from '../llins-village-target.model';

export type EntityResponseType = HttpResponse<ILlinsVillageTarget>;
export type EntityArrayResponseType = HttpResponse<ILlinsVillageTarget[]>;

@Injectable({ providedIn: 'root' })
export class LlinsVillageTargetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-village-targets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(llinsVillageTarget: ILlinsVillageTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageTarget);
    return this.http
      .post<ILlinsVillageTarget>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(llinsVillageTarget: ILlinsVillageTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageTarget);
    return this.http
      .put<ILlinsVillageTarget>(`${this.resourceUrl}/${getLlinsVillageTargetIdentifier(llinsVillageTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(llinsVillageTarget: ILlinsVillageTarget): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageTarget);
    return this.http
      .patch<ILlinsVillageTarget>(`${this.resourceUrl}/${getLlinsVillageTargetIdentifier(llinsVillageTarget) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILlinsVillageTarget>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILlinsVillageTarget[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLlinsVillageTargetToCollectionIfMissing(
    llinsVillageTargetCollection: ILlinsVillageTarget[],
    ...llinsVillageTargetsToCheck: (ILlinsVillageTarget | null | undefined)[]
  ): ILlinsVillageTarget[] {
    const llinsVillageTargets: ILlinsVillageTarget[] = llinsVillageTargetsToCheck.filter(isPresent);
    if (llinsVillageTargets.length > 0) {
      const llinsVillageTargetCollectionIdentifiers = llinsVillageTargetCollection.map(
        llinsVillageTargetItem => getLlinsVillageTargetIdentifier(llinsVillageTargetItem)!
      );
      const llinsVillageTargetsToAdd = llinsVillageTargets.filter(llinsVillageTargetItem => {
        const llinsVillageTargetIdentifier = getLlinsVillageTargetIdentifier(llinsVillageTargetItem);
        if (llinsVillageTargetIdentifier == null || llinsVillageTargetCollectionIdentifiers.includes(llinsVillageTargetIdentifier)) {
          return false;
        }
        llinsVillageTargetCollectionIdentifiers.push(llinsVillageTargetIdentifier);
        return true;
      });
      return [...llinsVillageTargetsToAdd, ...llinsVillageTargetCollection];
    }
    return llinsVillageTargetCollection;
  }

  protected convertDateFromClient(llinsVillageTarget: ILlinsVillageTarget): ILlinsVillageTarget {
    return Object.assign({}, llinsVillageTarget, {
      created: llinsVillageTarget.created?.isValid() ? llinsVillageTarget.created.toJSON() : undefined,
      lastUpdated: llinsVillageTarget.lastUpdated?.isValid() ? llinsVillageTarget.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((llinsVillageTarget: ILlinsVillageTarget) => {
        llinsVillageTarget.created = llinsVillageTarget.created ? dayjs(llinsVillageTarget.created) : undefined;
        llinsVillageTarget.lastUpdated = llinsVillageTarget.lastUpdated ? dayjs(llinsVillageTarget.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
