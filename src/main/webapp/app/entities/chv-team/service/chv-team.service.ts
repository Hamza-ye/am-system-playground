import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChvTeam, getChvTeamIdentifier } from '../chv-team.model';

export type EntityResponseType = HttpResponse<IChvTeam>;
export type EntityArrayResponseType = HttpResponse<IChvTeam[]>;

@Injectable({ providedIn: 'root' })
export class ChvTeamService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chv-teams');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chvTeam: IChvTeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvTeam);
    return this.http
      .post<IChvTeam>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chvTeam: IChvTeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvTeam);
    return this.http
      .put<IChvTeam>(`${this.resourceUrl}/${getChvTeamIdentifier(chvTeam) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(chvTeam: IChvTeam): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvTeam);
    return this.http
      .patch<IChvTeam>(`${this.resourceUrl}/${getChvTeamIdentifier(chvTeam) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChvTeam>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChvTeam[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChvTeamToCollectionIfMissing(chvTeamCollection: IChvTeam[], ...chvTeamsToCheck: (IChvTeam | null | undefined)[]): IChvTeam[] {
    const chvTeams: IChvTeam[] = chvTeamsToCheck.filter(isPresent);
    if (chvTeams.length > 0) {
      const chvTeamCollectionIdentifiers = chvTeamCollection.map(chvTeamItem => getChvTeamIdentifier(chvTeamItem)!);
      const chvTeamsToAdd = chvTeams.filter(chvTeamItem => {
        const chvTeamIdentifier = getChvTeamIdentifier(chvTeamItem);
        if (chvTeamIdentifier == null || chvTeamCollectionIdentifiers.includes(chvTeamIdentifier)) {
          return false;
        }
        chvTeamCollectionIdentifiers.push(chvTeamIdentifier);
        return true;
      });
      return [...chvTeamsToAdd, ...chvTeamCollection];
    }
    return chvTeamCollection;
  }

  protected convertDateFromClient(chvTeam: IChvTeam): IChvTeam {
    return Object.assign({}, chvTeam, {
      created: chvTeam.created?.isValid() ? chvTeam.created.toJSON() : undefined,
      lastUpdated: chvTeam.lastUpdated?.isValid() ? chvTeam.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((chvTeam: IChvTeam) => {
        chvTeam.created = chvTeam.created ? dayjs(chvTeam.created) : undefined;
        chvTeam.lastUpdated = chvTeam.lastUpdated ? dayjs(chvTeam.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
