import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { FamilyType } from 'app/entities/enumerations/family-type.model';
import { ILlinsFamilyReportHistory, LlinsFamilyReportHistory } from '../llins-family-report-history.model';

import { LlinsFamilyReportHistoryService } from './llins-family-report-history.service';

describe('Service Tests', () => {
  describe('LlinsFamilyReportHistory Service', () => {
    let service: LlinsFamilyReportHistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ILlinsFamilyReportHistory;
    let expectedResult: ILlinsFamilyReportHistory | ILlinsFamilyReportHistory[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LlinsFamilyReportHistoryService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        documentNo: 0,
        maleIndividuals: 0,
        femaleIndividuals: 0,
        lessThan5Males: 0,
        lessThan5Females: 0,
        pregnantWomen: 0,
        quantityReceived: 0,
        familyType: FamilyType.RESIDENT,
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

      it('should create a LlinsFamilyReportHistory', () => {
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

        service.create(new LlinsFamilyReportHistory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LlinsFamilyReportHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            documentNo: 1,
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
            familyType: 'BBBBBB',
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

      it('should partial update a LlinsFamilyReportHistory', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            documentNo: 1,
            femaleIndividuals: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
          },
          new LlinsFamilyReportHistory()
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

      it('should return a list of LlinsFamilyReportHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            documentNo: 1,
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
            familyType: 'BBBBBB',
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

      it('should delete a LlinsFamilyReportHistory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLlinsFamilyReportHistoryToCollectionIfMissing', () => {
        it('should add a LlinsFamilyReportHistory to an empty array', () => {
          const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 123 };
          expectedResult = service.addLlinsFamilyReportHistoryToCollectionIfMissing([], llinsFamilyReportHistory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsFamilyReportHistory);
        });

        it('should not add a LlinsFamilyReportHistory to an array that contains it', () => {
          const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 123 };
          const llinsFamilyReportHistoryCollection: ILlinsFamilyReportHistory[] = [
            {
              ...llinsFamilyReportHistory,
            },
            { id: 456 },
          ];
          expectedResult = service.addLlinsFamilyReportHistoryToCollectionIfMissing(
            llinsFamilyReportHistoryCollection,
            llinsFamilyReportHistory
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsFamilyReportHistory to an array that doesn't contain it", () => {
          const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 123 };
          const llinsFamilyReportHistoryCollection: ILlinsFamilyReportHistory[] = [{ id: 456 }];
          expectedResult = service.addLlinsFamilyReportHistoryToCollectionIfMissing(
            llinsFamilyReportHistoryCollection,
            llinsFamilyReportHistory
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsFamilyReportHistory);
        });

        it('should add only unique LlinsFamilyReportHistory to an array', () => {
          const llinsFamilyReportHistoryArray: ILlinsFamilyReportHistory[] = [{ id: 123 }, { id: 456 }, { id: 90434 }];
          const llinsFamilyReportHistoryCollection: ILlinsFamilyReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLlinsFamilyReportHistoryToCollectionIfMissing(
            llinsFamilyReportHistoryCollection,
            ...llinsFamilyReportHistoryArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 123 };
          const llinsFamilyReportHistory2: ILlinsFamilyReportHistory = { id: 456 };
          expectedResult = service.addLlinsFamilyReportHistoryToCollectionIfMissing(
            [],
            llinsFamilyReportHistory,
            llinsFamilyReportHistory2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsFamilyReportHistory);
          expect(expectedResult).toContain(llinsFamilyReportHistory2);
        });

        it('should accept null and undefined values', () => {
          const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 123 };
          expectedResult = service.addLlinsFamilyReportHistoryToCollectionIfMissing([], null, llinsFamilyReportHistory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsFamilyReportHistory);
        });

        it('should return initial array if no LlinsFamilyReportHistory is added', () => {
          const llinsFamilyReportHistoryCollection: ILlinsFamilyReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLlinsFamilyReportHistoryToCollectionIfMissing(llinsFamilyReportHistoryCollection, undefined, null);
          expect(expectedResult).toEqual(llinsFamilyReportHistoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
