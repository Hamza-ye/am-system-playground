import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChvMalariaCaseReport, getChvMalariaCaseReportIdentifier } from '../chv-malaria-case-report.model';

export type EntityResponseType = HttpResponse<IChvMalariaCaseReport>;
export type EntityArrayResponseType = HttpResponse<IChvMalariaCaseReport[]>;

@Injectable({ providedIn: 'root' })
export class ChvMalariaCaseReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chv-malaria-case-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chvMalariaCaseReport: IChvMalariaCaseReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvMalariaCaseReport);
    return this.http
      .post<IChvMalariaCaseReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chvMalariaCaseReport: IChvMalariaCaseReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvMalariaCaseReport);
    return this.http
      .put<IChvMalariaCaseReport>(`${this.resourceUrl}/${getChvMalariaCaseReportIdentifier(chvMalariaCaseReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(chvMalariaCaseReport: IChvMalariaCaseReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chvMalariaCaseReport);
    return this.http
      .patch<IChvMalariaCaseReport>(`${this.resourceUrl}/${getChvMalariaCaseReportIdentifier(chvMalariaCaseReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChvMalariaCaseReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChvMalariaCaseReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChvMalariaCaseReportToCollectionIfMissing(
    chvMalariaCaseReportCollection: IChvMalariaCaseReport[],
    ...chvMalariaCaseReportsToCheck: (IChvMalariaCaseReport | null | undefined)[]
  ): IChvMalariaCaseReport[] {
    const chvMalariaCaseReports: IChvMalariaCaseReport[] = chvMalariaCaseReportsToCheck.filter(isPresent);
    if (chvMalariaCaseReports.length > 0) {
      const chvMalariaCaseReportCollectionIdentifiers = chvMalariaCaseReportCollection.map(
        chvMalariaCaseReportItem => getChvMalariaCaseReportIdentifier(chvMalariaCaseReportItem)!
      );
      const chvMalariaCaseReportsToAdd = chvMalariaCaseReports.filter(chvMalariaCaseReportItem => {
        const chvMalariaCaseReportIdentifier = getChvMalariaCaseReportIdentifier(chvMalariaCaseReportItem);
        if (chvMalariaCaseReportIdentifier == null || chvMalariaCaseReportCollectionIdentifiers.includes(chvMalariaCaseReportIdentifier)) {
          return false;
        }
        chvMalariaCaseReportCollectionIdentifiers.push(chvMalariaCaseReportIdentifier);
        return true;
      });
      return [...chvMalariaCaseReportsToAdd, ...chvMalariaCaseReportCollection];
    }
    return chvMalariaCaseReportCollection;
  }

  protected convertDateFromClient(chvMalariaCaseReport: IChvMalariaCaseReport): IChvMalariaCaseReport {
    return Object.assign({}, chvMalariaCaseReport, {
      created: chvMalariaCaseReport.created?.isValid() ? chvMalariaCaseReport.created.toJSON() : undefined,
      lastUpdated: chvMalariaCaseReport.lastUpdated?.isValid() ? chvMalariaCaseReport.lastUpdated.toJSON() : undefined,
      date: chvMalariaCaseReport.date?.isValid() ? chvMalariaCaseReport.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
      res.body.lastUpdated = res.body.lastUpdated ? dayjs(res.body.lastUpdated) : undefined;
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((chvMalariaCaseReport: IChvMalariaCaseReport) => {
        chvMalariaCaseReport.created = chvMalariaCaseReport.created ? dayjs(chvMalariaCaseReport.created) : undefined;
        chvMalariaCaseReport.lastUpdated = chvMalariaCaseReport.lastUpdated ? dayjs(chvMalariaCaseReport.lastUpdated) : undefined;
        chvMalariaCaseReport.date = chvMalariaCaseReport.date ? dayjs(chvMalariaCaseReport.date) : undefined;
      });
    }
    return res;
  }
}
