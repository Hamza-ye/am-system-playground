import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILlinsFamilyReport, getLlinsFamilyReportIdentifier } from '../llins-family-report.model';

export type EntityResponseType = HttpResponse<ILlinsFamilyReport>;
export type EntityArrayResponseType = HttpResponse<ILlinsFamilyReport[]>;

@Injectable({ providedIn: 'root' })
export class LlinsFamilyReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-family-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(llinsFamilyReport: ILlinsFamilyReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyReport);
    return this.http
      .post<ILlinsFamilyReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(llinsFamilyReport: ILlinsFamilyReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyReport);
    return this.http
      .put<ILlinsFamilyReport>(`${this.resourceUrl}/${getLlinsFamilyReportIdentifier(llinsFamilyReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(llinsFamilyReport: ILlinsFamilyReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsFamilyReport);
    return this.http
      .patch<ILlinsFamilyReport>(`${this.resourceUrl}/${getLlinsFamilyReportIdentifier(llinsFamilyReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILlinsFamilyReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILlinsFamilyReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLlinsFamilyReportToCollectionIfMissing(
    llinsFamilyReportCollection: ILlinsFamilyReport[],
    ...llinsFamilyReportsToCheck: (ILlinsFamilyReport | null | undefined)[]
  ): ILlinsFamilyReport[] {
    const llinsFamilyReports: ILlinsFamilyReport[] = llinsFamilyReportsToCheck.filter(isPresent);
    if (llinsFamilyReports.length > 0) {
      const llinsFamilyReportCollectionIdentifiers = llinsFamilyReportCollection.map(
        llinsFamilyReportItem => getLlinsFamilyReportIdentifier(llinsFamilyReportItem)!
      );
      const llinsFamilyReportsToAdd = llinsFamilyReports.filter(llinsFamilyReportItem => {
        const llinsFamilyReportIdentifier = getLlinsFamilyReportIdentifier(llinsFamilyReportItem);
        if (llinsFamilyReportIdentifier == null || llinsFamilyReportCollectionIdentifiers.includes(llinsFamilyReportIdentifier)) {
          return false;
        }
        llinsFamilyReportCollectionIdentifiers.push(llinsFamilyReportIdentifier);
        return true;
      });
      return [...llinsFamilyReportsToAdd, ...llinsFamilyReportCollection];
    }
    return llinsFamilyReportCollection;
  }

  protected convertDateFromClient(llinsFamilyReport: ILlinsFamilyReport): ILlinsFamilyReport {
    return Object.assign({}, llinsFamilyReport, {
      created: llinsFamilyReport.created?.isValid() ? llinsFamilyReport.created.toJSON() : undefined,
      lastUpdated: llinsFamilyReport.lastUpdated?.isValid() ? llinsFamilyReport.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((llinsFamilyReport: ILlinsFamilyReport) => {
        llinsFamilyReport.created = llinsFamilyReport.created ? dayjs(llinsFamilyReport.created) : undefined;
        llinsFamilyReport.lastUpdated = llinsFamilyReport.lastUpdated ? dayjs(llinsFamilyReport.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
