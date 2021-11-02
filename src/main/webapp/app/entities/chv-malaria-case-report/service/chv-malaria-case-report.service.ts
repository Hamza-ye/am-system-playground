import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICHVMalariaCaseReport, getCHVMalariaCaseReportIdentifier } from '../chv-malaria-case-report.model';

export type EntityResponseType = HttpResponse<ICHVMalariaCaseReport>;
export type EntityArrayResponseType = HttpResponse<ICHVMalariaCaseReport[]>;

@Injectable({ providedIn: 'root' })
export class CHVMalariaCaseReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chv-malaria-case-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cHVMalariaCaseReport: ICHVMalariaCaseReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVMalariaCaseReport);
    return this.http
      .post<ICHVMalariaCaseReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(cHVMalariaCaseReport: ICHVMalariaCaseReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVMalariaCaseReport);
    return this.http
      .put<ICHVMalariaCaseReport>(`${this.resourceUrl}/${getCHVMalariaCaseReportIdentifier(cHVMalariaCaseReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(cHVMalariaCaseReport: ICHVMalariaCaseReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cHVMalariaCaseReport);
    return this.http
      .patch<ICHVMalariaCaseReport>(`${this.resourceUrl}/${getCHVMalariaCaseReportIdentifier(cHVMalariaCaseReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICHVMalariaCaseReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICHVMalariaCaseReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCHVMalariaCaseReportToCollectionIfMissing(
    cHVMalariaCaseReportCollection: ICHVMalariaCaseReport[],
    ...cHVMalariaCaseReportsToCheck: (ICHVMalariaCaseReport | null | undefined)[]
  ): ICHVMalariaCaseReport[] {
    const cHVMalariaCaseReports: ICHVMalariaCaseReport[] = cHVMalariaCaseReportsToCheck.filter(isPresent);
    if (cHVMalariaCaseReports.length > 0) {
      const cHVMalariaCaseReportCollectionIdentifiers = cHVMalariaCaseReportCollection.map(
        cHVMalariaCaseReportItem => getCHVMalariaCaseReportIdentifier(cHVMalariaCaseReportItem)!
      );
      const cHVMalariaCaseReportsToAdd = cHVMalariaCaseReports.filter(cHVMalariaCaseReportItem => {
        const cHVMalariaCaseReportIdentifier = getCHVMalariaCaseReportIdentifier(cHVMalariaCaseReportItem);
        if (cHVMalariaCaseReportIdentifier == null || cHVMalariaCaseReportCollectionIdentifiers.includes(cHVMalariaCaseReportIdentifier)) {
          return false;
        }
        cHVMalariaCaseReportCollectionIdentifiers.push(cHVMalariaCaseReportIdentifier);
        return true;
      });
      return [...cHVMalariaCaseReportsToAdd, ...cHVMalariaCaseReportCollection];
    }
    return cHVMalariaCaseReportCollection;
  }

  protected convertDateFromClient(cHVMalariaCaseReport: ICHVMalariaCaseReport): ICHVMalariaCaseReport {
    return Object.assign({}, cHVMalariaCaseReport, {
      created: cHVMalariaCaseReport.created?.isValid() ? cHVMalariaCaseReport.created.toJSON() : undefined,
      lastUpdated: cHVMalariaCaseReport.lastUpdated?.isValid() ? cHVMalariaCaseReport.lastUpdated.toJSON() : undefined,
      date: cHVMalariaCaseReport.date?.isValid() ? cHVMalariaCaseReport.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((cHVMalariaCaseReport: ICHVMalariaCaseReport) => {
        cHVMalariaCaseReport.created = cHVMalariaCaseReport.created ? dayjs(cHVMalariaCaseReport.created) : undefined;
        cHVMalariaCaseReport.lastUpdated = cHVMalariaCaseReport.lastUpdated ? dayjs(cHVMalariaCaseReport.lastUpdated) : undefined;
        cHVMalariaCaseReport.date = cHVMalariaCaseReport.date ? dayjs(cHVMalariaCaseReport.date) : undefined;
      });
    }
    return res;
  }
}
