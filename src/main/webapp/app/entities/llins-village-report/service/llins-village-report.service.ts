import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILLINSVillageReport, getLLINSVillageReportIdentifier } from '../llins-village-report.model';

export type EntityResponseType = HttpResponse<ILLINSVillageReport>;
export type EntityArrayResponseType = HttpResponse<ILLINSVillageReport[]>;

@Injectable({ providedIn: 'root' })
export class LLINSVillageReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-village-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lLINSVillageReport: ILLINSVillageReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageReport);
    return this.http
      .post<ILLINSVillageReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(lLINSVillageReport: ILLINSVillageReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageReport);
    return this.http
      .put<ILLINSVillageReport>(`${this.resourceUrl}/${getLLINSVillageReportIdentifier(lLINSVillageReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(lLINSVillageReport: ILLINSVillageReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(lLINSVillageReport);
    return this.http
      .patch<ILLINSVillageReport>(`${this.resourceUrl}/${getLLINSVillageReportIdentifier(lLINSVillageReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILLINSVillageReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILLINSVillageReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLLINSVillageReportToCollectionIfMissing(
    lLINSVillageReportCollection: ILLINSVillageReport[],
    ...lLINSVillageReportsToCheck: (ILLINSVillageReport | null | undefined)[]
  ): ILLINSVillageReport[] {
    const lLINSVillageReports: ILLINSVillageReport[] = lLINSVillageReportsToCheck.filter(isPresent);
    if (lLINSVillageReports.length > 0) {
      const lLINSVillageReportCollectionIdentifiers = lLINSVillageReportCollection.map(
        lLINSVillageReportItem => getLLINSVillageReportIdentifier(lLINSVillageReportItem)!
      );
      const lLINSVillageReportsToAdd = lLINSVillageReports.filter(lLINSVillageReportItem => {
        const lLINSVillageReportIdentifier = getLLINSVillageReportIdentifier(lLINSVillageReportItem);
        if (lLINSVillageReportIdentifier == null || lLINSVillageReportCollectionIdentifiers.includes(lLINSVillageReportIdentifier)) {
          return false;
        }
        lLINSVillageReportCollectionIdentifiers.push(lLINSVillageReportIdentifier);
        return true;
      });
      return [...lLINSVillageReportsToAdd, ...lLINSVillageReportCollection];
    }
    return lLINSVillageReportCollection;
  }

  protected convertDateFromClient(lLINSVillageReport: ILLINSVillageReport): ILLINSVillageReport {
    return Object.assign({}, lLINSVillageReport, {
      created: lLINSVillageReport.created?.isValid() ? lLINSVillageReport.created.toJSON() : undefined,
      lastUpdated: lLINSVillageReport.lastUpdated?.isValid() ? lLINSVillageReport.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((lLINSVillageReport: ILLINSVillageReport) => {
        lLINSVillageReport.created = lLINSVillageReport.created ? dayjs(lLINSVillageReport.created) : undefined;
        lLINSVillageReport.lastUpdated = lLINSVillageReport.lastUpdated ? dayjs(lLINSVillageReport.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
