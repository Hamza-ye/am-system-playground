import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMalariaCasesReport, getMalariaCasesReportIdentifier } from '../malaria-cases-report.model';

export type EntityResponseType = HttpResponse<IMalariaCasesReport>;
export type EntityArrayResponseType = HttpResponse<IMalariaCasesReport[]>;

@Injectable({ providedIn: 'root' })
export class MalariaCasesReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/malaria-cases-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(malariaCasesReport: IMalariaCasesReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaCasesReport);
    return this.http
      .post<IMalariaCasesReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(malariaCasesReport: IMalariaCasesReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaCasesReport);
    return this.http
      .put<IMalariaCasesReport>(`${this.resourceUrl}/${getMalariaCasesReportIdentifier(malariaCasesReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(malariaCasesReport: IMalariaCasesReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(malariaCasesReport);
    return this.http
      .patch<IMalariaCasesReport>(`${this.resourceUrl}/${getMalariaCasesReportIdentifier(malariaCasesReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMalariaCasesReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMalariaCasesReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMalariaCasesReportToCollectionIfMissing(
    malariaCasesReportCollection: IMalariaCasesReport[],
    ...malariaCasesReportsToCheck: (IMalariaCasesReport | null | undefined)[]
  ): IMalariaCasesReport[] {
    const malariaCasesReports: IMalariaCasesReport[] = malariaCasesReportsToCheck.filter(isPresent);
    if (malariaCasesReports.length > 0) {
      const malariaCasesReportCollectionIdentifiers = malariaCasesReportCollection.map(
        malariaCasesReportItem => getMalariaCasesReportIdentifier(malariaCasesReportItem)!
      );
      const malariaCasesReportsToAdd = malariaCasesReports.filter(malariaCasesReportItem => {
        const malariaCasesReportIdentifier = getMalariaCasesReportIdentifier(malariaCasesReportItem);
        if (malariaCasesReportIdentifier == null || malariaCasesReportCollectionIdentifiers.includes(malariaCasesReportIdentifier)) {
          return false;
        }
        malariaCasesReportCollectionIdentifiers.push(malariaCasesReportIdentifier);
        return true;
      });
      return [...malariaCasesReportsToAdd, ...malariaCasesReportCollection];
    }
    return malariaCasesReportCollection;
  }

  protected convertDateFromClient(malariaCasesReport: IMalariaCasesReport): IMalariaCasesReport {
    return Object.assign({}, malariaCasesReport, {
      created: malariaCasesReport.created?.isValid() ? malariaCasesReport.created.toJSON() : undefined,
      lastUpdated: malariaCasesReport.lastUpdated?.isValid() ? malariaCasesReport.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((malariaCasesReport: IMalariaCasesReport) => {
        malariaCasesReport.created = malariaCasesReport.created ? dayjs(malariaCasesReport.created) : undefined;
        malariaCasesReport.lastUpdated = malariaCasesReport.lastUpdated ? dayjs(malariaCasesReport.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
