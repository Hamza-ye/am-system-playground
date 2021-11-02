import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDengueCasesReport, getDengueCasesReportIdentifier } from '../dengue-cases-report.model';

export type EntityResponseType = HttpResponse<IDengueCasesReport>;
export type EntityArrayResponseType = HttpResponse<IDengueCasesReport[]>;

@Injectable({ providedIn: 'root' })
export class DengueCasesReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dengue-cases-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dengueCasesReport: IDengueCasesReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dengueCasesReport);
    return this.http
      .post<IDengueCasesReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dengueCasesReport: IDengueCasesReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dengueCasesReport);
    return this.http
      .put<IDengueCasesReport>(`${this.resourceUrl}/${getDengueCasesReportIdentifier(dengueCasesReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dengueCasesReport: IDengueCasesReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dengueCasesReport);
    return this.http
      .patch<IDengueCasesReport>(`${this.resourceUrl}/${getDengueCasesReportIdentifier(dengueCasesReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDengueCasesReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDengueCasesReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDengueCasesReportToCollectionIfMissing(
    dengueCasesReportCollection: IDengueCasesReport[],
    ...dengueCasesReportsToCheck: (IDengueCasesReport | null | undefined)[]
  ): IDengueCasesReport[] {
    const dengueCasesReports: IDengueCasesReport[] = dengueCasesReportsToCheck.filter(isPresent);
    if (dengueCasesReports.length > 0) {
      const dengueCasesReportCollectionIdentifiers = dengueCasesReportCollection.map(
        dengueCasesReportItem => getDengueCasesReportIdentifier(dengueCasesReportItem)!
      );
      const dengueCasesReportsToAdd = dengueCasesReports.filter(dengueCasesReportItem => {
        const dengueCasesReportIdentifier = getDengueCasesReportIdentifier(dengueCasesReportItem);
        if (dengueCasesReportIdentifier == null || dengueCasesReportCollectionIdentifiers.includes(dengueCasesReportIdentifier)) {
          return false;
        }
        dengueCasesReportCollectionIdentifiers.push(dengueCasesReportIdentifier);
        return true;
      });
      return [...dengueCasesReportsToAdd, ...dengueCasesReportCollection];
    }
    return dengueCasesReportCollection;
  }

  protected convertDateFromClient(dengueCasesReport: IDengueCasesReport): IDengueCasesReport {
    return Object.assign({}, dengueCasesReport, {
      created: dengueCasesReport.created?.isValid() ? dengueCasesReport.created.toJSON() : undefined,
      lastUpdated: dengueCasesReport.lastUpdated?.isValid() ? dengueCasesReport.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((dengueCasesReport: IDengueCasesReport) => {
        dengueCasesReport.created = dengueCasesReport.created ? dayjs(dengueCasesReport.created) : undefined;
        dengueCasesReport.lastUpdated = dengueCasesReport.lastUpdated ? dayjs(dengueCasesReport.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
