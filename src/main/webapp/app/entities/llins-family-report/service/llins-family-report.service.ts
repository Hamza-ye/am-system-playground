import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILLINSFamilyReport, getLLINSFamilyReportIdentifier } from '../llins-family-report.model';

export type EntityResponseType = HttpResponse<ILLINSFamilyReport>;
export type EntityArrayResponseType = HttpResponse<ILLINSFamilyReport[]>;

@Injectable({ providedIn: 'root' })
export class LLINSFamilyReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-family-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lLINSFamilyReport: ILLINSFamilyReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyReport);
    return this.http
      .post<ILLINSFamilyReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lLINSFamilyReport: ILLINSFamilyReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyReport);
    return this.http
      .put<ILLINSFamilyReport>(`${this.resourceUrl}/${getLLINSFamilyReportIdentifier(lLINSFamilyReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lLINSFamilyReport: ILLINSFamilyReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSFamilyReport);
    return this.http
      .patch<ILLINSFamilyReport>(`${this.resourceUrl}/${getLLINSFamilyReportIdentifier(lLINSFamilyReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILLINSFamilyReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILLINSFamilyReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLLINSFamilyReportToCollectionIfMissing(
    lLINSFamilyReportCollection: ILLINSFamilyReport[],
    ...lLINSFamilyReportsToCheck: (ILLINSFamilyReport | null | undefined)[]
  ): ILLINSFamilyReport[] {
    const lLINSFamilyReports: ILLINSFamilyReport[] = lLINSFamilyReportsToCheck.filter(isPresent);
    if (lLINSFamilyReports.length > 0) {
      const lLINSFamilyReportCollectionIdentifiers = lLINSFamilyReportCollection.map(
        lLINSFamilyReportItem => getLLINSFamilyReportIdentifier(lLINSFamilyReportItem)!
      );
      const lLINSFamilyReportsToAdd = lLINSFamilyReports.filter(lLINSFamilyReportItem => {
        const lLINSFamilyReportIdentifier = getLLINSFamilyReportIdentifier(lLINSFamilyReportItem);
        if (lLINSFamilyReportIdentifier == null || lLINSFamilyReportCollectionIdentifiers.includes(lLINSFamilyReportIdentifier)) {
          return false;
        }
        lLINSFamilyReportCollectionIdentifiers.push(lLINSFamilyReportIdentifier);
        return true;
      });
      return [...lLINSFamilyReportsToAdd, ...lLINSFamilyReportCollection];
    }
    return lLINSFamilyReportCollection;
  }

  protected convertDateFromClient(lLINSFamilyReport: ILLINSFamilyReport): ILLINSFamilyReport {
    return Object.assign({}, lLINSFamilyReport, {
      created: lLINSFamilyReport.created?.isValid() ? lLINSFamilyReport.created.toJSON() : undefined,
      lastUpdated: lLINSFamilyReport.lastUpdated?.isValid() ? lLINSFamilyReport.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((lLINSFamilyReport: ILLINSFamilyReport) => {
        lLINSFamilyReport.created = lLINSFamilyReport.created ? dayjs(lLINSFamilyReport.created) : undefined;
        lLINSFamilyReport.lastUpdated = lLINSFamilyReport.lastUpdated ? dayjs(lLINSFamilyReport.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
