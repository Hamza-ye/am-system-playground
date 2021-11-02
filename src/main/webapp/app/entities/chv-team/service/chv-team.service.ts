import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICHVTeam, getCHVTeamIdentifier } from '../chv-team.model';

export type EntityResponseType = HttpResponse<ICHVTeam>;
export type EntityArrayResponseType = HttpResponse<ICHVTeam[]>;

@Injectable({ providedIn: 'root' })
export class CHVTeamService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chv-teams');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cHVTeam: ICHVTeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVTeam);
    return this.http
      .post<ICHVTeam>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cHVTeam: ICHVTeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVTeam);
    return this.http
      .put<ICHVTeam>(`${this.resourceUrl}/${getCHVTeamIdentifier(cHVTeam) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cHVTeam: ICHVTeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVTeam);
    return this.http
      .patch<ICHVTeam>(`${this.resourceUrl}/${getCHVTeamIdentifier(cHVTeam) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICHVTeam>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICHVTeam[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCHVTeamToCollectionIfMissing(cHVTeamCollection: ICHVTeam[], ...cHVTeamsToCheck: (ICHVTeam | null | undefined)[]): ICHVTeam[] {
    const cHVTeams: ICHVTeam[] = cHVTeamsToCheck.filter(isPresent);
    if (cHVTeams.length > 0) {
      const cHVTeamCollectionIdentifiers = cHVTeamCollection.map(cHVTeamItem => getCHVTeamIdentifier(cHVTeamItem)!);
      const cHVTeamsToAdd = cHVTeams.filter(cHVTeamItem => {
        const cHVTeamIdentifier = getCHVTeamIdentifier(cHVTeamItem);
        if (cHVTeamIdentifier == null || cHVTeamCollectionIdentifiers.includes(cHVTeamIdentifier)) {
          return false;
        }
        cHVTeamCollectionIdentifiers.push(cHVTeamIdentifier);
        return true;
      });
      return [...cHVTeamsToAdd, ...cHVTeamCollection];
    }
    return cHVTeamCollection;
  }

  protected convertDateFromClient(cHVTeam: ICHVTeam): ICHVTeam {
    return Object.assign({}, cHVTeam, {
      created: cHVTeam.created?.isValid() ? cHVTeam.created.toJSON() : undefined,
      lastUpdated: cHVTeam.lastUpdated?.isValid() ? cHVTeam.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((cHVTeam: ICHVTeam) => {
        cHVTeam.created = cHVTeam.created ? dayjs(cHVTeam.created) : undefined;
        cHVTeam.lastUpdated = cHVTeam.lastUpdated ? dayjs(cHVTeam.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
