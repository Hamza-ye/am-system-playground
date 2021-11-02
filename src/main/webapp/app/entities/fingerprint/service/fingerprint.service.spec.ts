import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFingerprint, Fingerprint } from '../fingerprint.model';

import { FingerprintService } from './fingerprint.service';

describe('Service Tests', () => {
  describe('Fingerprint Service', () => {
    let service: FingerprintService;
    let httpMock: HttpTestingController;
    let elemDefault: IFingerprint;
    let expectedResult: IFingerprint | IFingerprint[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FingerprintService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        description: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        fingerprintUrl: 'AAAAAAA',
        fingerprintOwner: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Fingerprint', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
          },
          returnedFromService
        );

        service.create(new Fingerprint()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Fingerprint', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            fingerprintUrl: 'BBBBBB',
            fingerprintOwner: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Fingerprint', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            fingerprintUrl: 'BBBBBB',
            fingerprintOwner: 'BBBBBB',
          },
          new Fingerprint()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Fingerprint', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            fingerprintUrl: 'BBBBBB',
            fingerprintOwner: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Fingerprint', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFingerprintToCollectionIfMissing', () => {
        it('should add a Fingerprint to an empty array', () => {
          const fingerprint: IFingerprint = { id: 123 };
          expectedResult = service.addFingerprintToCollectionIfMissing([], fingerprint);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fingerprint);
        });

        it('should not add a Fingerprint to an array that contains it', () => {
          const fingerprint: IFingerprint = { id: 123 };
          const fingerprintCollection: IFingerprint[] = [
            {
              ...fingerprint,
            },
            { id: 456 },
          ];
          expectedResult = service.addFingerprintToCollectionIfMissing(fingerprintCollection, fingerprint);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Fingerprint to an array that doesn't contain it", () => {
          const fingerprint: IFingerprint = { id: 123 };
          const fingerprintCollection: IFingerprint[] = [{ id: 456 }];
          expectedResult = service.addFingerprintToCollectionIfMissing(fingerprintCollection, fingerprint);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fingerprint);
        });

        it('should add only unique Fingerprint to an array', () => {
          const fingerprintArray: IFingerprint[] = [{ id: 123 }, { id: 456 }, { id: 26711 }];
          const fingerprintCollection: IFingerprint[] = [{ id: 123 }];
          expectedResult = service.addFingerprintToCollectionIfMissing(fingerprintCollection, ...fingerprintArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fingerprint: IFingerprint = { id: 123 };
          const fingerprint2: IFingerprint = { id: 456 };
          expectedResult = service.addFingerprintToCollectionIfMissing([], fingerprint, fingerprint2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fingerprint);
          expect(expectedResult).toContain(fingerprint2);
        });

        it('should accept null and undefined values', () => {
          const fingerprint: IFingerprint = { id: 123 };
          expectedResult = service.addFingerprintToCollectionIfMissing([], null, fingerprint, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fingerprint);
        });

        it('should return initial array if no Fingerprint is added', () => {
          const fingerprintCollection: IFingerprint[] = [{ id: 123 }];
          expectedResult = service.addFingerprintToCollectionIfMissing(fingerprintCollection, undefined, null);
          expect(expectedResult).toEqual(fingerprintCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
