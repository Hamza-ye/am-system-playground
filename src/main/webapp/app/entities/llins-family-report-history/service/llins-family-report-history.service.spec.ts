import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { FamilyType } from 'app/entities/enumerations/family-type.model';
import { ILLINSFamilyReportHistory, LLINSFamilyReportHistory } from '../llins-family-report-history.model';

import { LLINSFamilyReportHistoryService } from './llins-family-report-history.service';

describe('Service Tests', () => {
  describe('LlinsFamilyReportHistory Service', () => {
    let service: LLINSFamilyReportHistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ILLINSFamilyReportHistory;
    let expectedResult: ILLINSFamilyReportHistory | ILLINSFamilyReportHistory[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LLINSFamilyReportHistoryService);
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

        service.create(new LLINSFamilyReportHistory()).subscribe(resp => (expectedResult = resp.body));

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
            created: currentDate.format(DATE_TIME_FORMAT),
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
            familyType: 'BBBBBB',
          },
          new LLINSFamilyReportHistory()
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

      describe('addLLINSFamilyReportHistoryToCollectionIfMissing', () => {
        it('should add a LlinsFamilyReportHistory to an empty array', () => {
          const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 123 };
          expectedResult = service.addLLINSFamilyReportHistoryToCollectionIfMissing([], lLINSFamilyReportHistory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSFamilyReportHistory);
        });

        it('should not add a LlinsFamilyReportHistory to an array that contains it', () => {
          const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 123 };
          const lLINSFamilyReportHistoryCollection: ILLINSFamilyReportHistory[] = [
            {
              ...lLINSFamilyReportHistory,
            },
            { id: 456 },
          ];
          expectedResult = service.addLLINSFamilyReportHistoryToCollectionIfMissing(
            lLINSFamilyReportHistoryCollection,
            lLINSFamilyReportHistory
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsFamilyReportHistory to an array that doesn't contain it", () => {
          const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 123 };
          const lLINSFamilyReportHistoryCollection: ILLINSFamilyReportHistory[] = [{ id: 456 }];
          expectedResult = service.addLLINSFamilyReportHistoryToCollectionIfMissing(
            lLINSFamilyReportHistoryCollection,
            lLINSFamilyReportHistory
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSFamilyReportHistory);
        });

        it('should add only unique LlinsFamilyReportHistory to an array', () => {
          const lLINSFamilyReportHistoryArray: ILLINSFamilyReportHistory[] = [{ id: 123 }, { id: 456 }, { id: 48232 }];
          const lLINSFamilyReportHistoryCollection: ILLINSFamilyReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLLINSFamilyReportHistoryToCollectionIfMissing(
            lLINSFamilyReportHistoryCollection,
            ...lLINSFamilyReportHistoryArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 123 };
          const lLINSFamilyReportHistory2: ILLINSFamilyReportHistory = { id: 456 };
          expectedResult = service.addLLINSFamilyReportHistoryToCollectionIfMissing(
            [],
            lLINSFamilyReportHistory,
            lLINSFamilyReportHistory2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSFamilyReportHistory);
          expect(expectedResult).toContain(lLINSFamilyReportHistory2);
        });

        it('should accept null and undefined values', () => {
          const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 123 };
          expectedResult = service.addLLINSFamilyReportHistoryToCollectionIfMissing([], null, lLINSFamilyReportHistory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSFamilyReportHistory);
        });

        it('should return initial array if no LlinsFamilyReportHistory is added', () => {
          const lLINSFamilyReportHistoryCollection: ILLINSFamilyReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLLINSFamilyReportHistoryToCollectionIfMissing(lLINSFamilyReportHistoryCollection, undefined, null);
          expect(expectedResult).toEqual(lLINSFamilyReportHistoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
