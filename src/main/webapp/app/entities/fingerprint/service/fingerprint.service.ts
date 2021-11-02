import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFingerprint, getFingerprintIdentifier } from '../fingerprint.model';

export type EntityResponseType = HttpResponse<IFingerprint>;
export type EntityArrayResponseType = HttpResponse<IFingerprint[]>;

@Injectable({ providedIn: 'root' })
export class FingerprintService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fingerprints');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fingerprint: IFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fingerprint);
    return this.http
      .post<IFingerprint>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fingerprint: IFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fingerprint);
    return this.http
      .put<IFingerprint>(`${this.resourceUrl}/${getFingerprintIdentifier(fingerprint) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fingerprint: IFingerprint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fingerprint);
    return this.http
      .patch<IFingerprint>(`${this.resourceUrl}/${getFingerprintIdentifier(fingerprint) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFingerprint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFingerprint[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFingerprintToCollectionIfMissing(
    fingerprintCollection: IFingerprint[],
    ...fingerprintsToCheck: (IFingerprint | null | undefined)[]
  ): IFingerprint[] {
    const fingerprints: IFingerprint[] = fingerprintsToCheck.filter(isPresent);
    if (fingerprints.length > 0) {
      const fingerprintCollectionIdentifiers = fingerprintCollection.map(fingerprintItem => getFingerprintIdentifier(fingerprintItem)!);
      const fingerprintsToAdd = fingerprints.filter(fingerprintItem => {
        const fingerprintIdentifier = getFingerprintIdentifier(fingerprintItem);
        if (fingerprintIdentifier == null || fingerprintCollectionIdentifiers.includes(fingerprintIdentifier)) {
          return false;
        }
        fingerprintCollectionIdentifiers.push(fingerprintIdentifier);
        return true;
      });
      return [...fingerprintsToAdd, ...fingerprintCollection];
    }
    return fingerprintCollection;
  }

  protected convertDateFromClient(fingerprint: IFingerprint): IFingerprint {
    return Object.assign({}, fingerprint, {
      created: fingerprint.created?.isValid() ? fingerprint.created.toJSON() : undefined,
      lastUpdated: fingerprint.lastUpdated?.isValid() ? fingerprint.lastUpdated.toJSON() : undefined,
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
      res.body.forEach((fingerprint: IFingerprint) => {
        fingerprint.created = fingerprint.created ? dayjs(fingerprint.created) : undefined;
        fingerprint.lastUpdated = fingerprint.lastUpdated ? dayjs(fingerprint.lastUpdated) : undefined;
      });
    }
    return res;
  }
}
