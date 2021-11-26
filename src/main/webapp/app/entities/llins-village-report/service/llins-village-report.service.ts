import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILlinsVillageReport, getLlinsVillageReportIdentifier } from '../llins-village-report.model';

export type EntityResponseType = HttpResponse<ILlinsVillageReport>;
export type EntityArrayResponseType = HttpResponse<ILlinsVillageReport[]>;

@Injectable({ providedIn: 'root' })
export class LlinsVillageReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/llins-village-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(llinsVillageReport: ILlinsVillageReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageReport);
    return this.http
      .post<ILlinsVillageReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(llinsVillageReport: ILlinsVillageReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageReport);
    return this.http
      .put<ILlinsVillageReport>(`${this.resourceUrl}/${getLlinsVillageReportIdentifier(llinsVillageReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(llinsVillageReport: ILlinsVillageReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(llinsVillageReport);
    return this.http
      .patch<ILlinsVillageReport>(`${this.resourceUrl}/${getLlinsVillageReportIdentifier(llinsVillageReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILlinsVillageReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILlinsVillageReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLlinsVillageReportToCollectionIfMissing(
    llinsVillageReportCollection: ILlinsVillageReport[],
    ...llinsVillageReportsToCheck: (ILlinsVillageReport | null | undefined)[]
  ): ILlinsVillageReport[] {
    const llinsVillageReports: ILlinsVillageReport[] = llinsVillageReportsToCheck.filter(isPresent);
    if (llinsVillageReports.length > 0) {
      const llinsVillageReportCollectionIdentifiers = llinsVillageReportCollection.map(
        llinsVillageReportItem => getLlinsVillageReportIdentifier(llinsVillageReportItem)!
      );
      const llinsVillageReportsToAdd = llinsVillageReports.filter(llinsVillageReportItem => {
        const llinsVillageReportIdentifier = getLlinsVillageReportIdentifier(llinsVillageReportItem);
        if (llinsVillageReportIdentifier == null || llinsVillageReportCollectionIdentifiers.includes(llinsVillageReportIdentifier)) {
          return false;
        }
        llinsVillageReportCollectionIdentifiers.push(llinsVillageReportIdentifier);
        return true;
      });
      return [...llinsVillageReportsToAdd, ...llinsVillageReportCollection];
    }
    return llinsVillageReportCollection;
  }

  protected convertDateFromClient(llinsVillageReport: ILlinsVillageReport): ILlinsVillageReport {
    return Object.assign({}, llinsVillageReport, {
      created: llinsVillageReport.created?.isValid() ? llinsVillageReport.created.toJSON() : undefined,
      lastUpdated: llinsVillageReport.lastUpdated?.isValid() ? llinsVillageReport.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((llinsVillageReport: ILlinsVillageReport) => {
        llinsVillageReport.created = llinsVillageReport.created ? dayjs(llinsVillageReport.created) : undefined;
        llinsVillageReport.lastUpdated = llinsVillageReport.lastUpdated ? dayjs(llinsVillageReport.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
