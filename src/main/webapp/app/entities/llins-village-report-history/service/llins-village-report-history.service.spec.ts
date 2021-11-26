import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILLINSVillageReportHistory, LLINSVillageReportHistory } from '../llins-village-report-history.model';

import { LLINSVillageReportHistoryService } from './llins-village-report-history.service';

describe('Service Tests', () => {
  describe('LlinsVillageReportHistory Service', () => {
    let service: LLINSVillageReportHistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ILLINSVillageReportHistory;
    let expectedResult: ILLINSVillageReportHistory | ILLINSVillageReportHistory[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LLINSVillageReportHistoryService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        houses: 0,
        residentHousehold: 0,
        idpsHousehold: 0,
        maleIndividuals: 0,
        femaleIndividuals: 0,
        lessThan5Males: 0,
        lessThan5Females: 0,
        pregnantWomen: 0,
        quantityReceived: 0,
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

      it('should create a LlinsVillageReportHistory', () => {
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

        service.create(new LLINSVillageReportHistory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LlinsVillageReportHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            houses: 1,
            residentHousehold: 1,
            idpsHousehold: 1,
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
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

      it('should partial update a LlinsVillageReportHistory', () => {
        const patchObject = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            houses: 1,
            residentHousehold: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
          },
          new LLINSVillageReportHistory()
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

      it('should return a list of LlinsVillageReportHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            houses: 1,
            residentHousehold: 1,
            idpsHousehold: 1,
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
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

      it('should delete a LlinsVillageReportHistory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLLINSVillageReportHistoryToCollectionIfMissing', () => {
        it('should add a LlinsVillageReportHistory to an empty array', () => {
          const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 123 };
          expectedResult = service.addLLINSVillageReportHistoryToCollectionIfMissing([], lLINSVillageReportHistory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSVillageReportHistory);
        });

        it('should not add a LlinsVillageReportHistory to an array that contains it', () => {
          const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 123 };
          const lLINSVillageReportHistoryCollection: ILLINSVillageReportHistory[] = [
            {
              ...lLINSVillageReportHistory,
            },
            { id: 456 },
          ];
          expectedResult = service.addLLINSVillageReportHistoryToCollectionIfMissing(
            lLINSVillageReportHistoryCollection,
            lLINSVillageReportHistory
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsVillageReportHistory to an array that doesn't contain it", () => {
          const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 123 };
          const lLINSVillageReportHistoryCollection: ILLINSVillageReportHistory[] = [{ id: 456 }];
          expectedResult = service.addLLINSVillageReportHistoryToCollectionIfMissing(
            lLINSVillageReportHistoryCollection,
            lLINSVillageReportHistory
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSVillageReportHistory);
        });

        it('should add only unique LlinsVillageReportHistory to an array', () => {
          const lLINSVillageReportHistoryArray: ILLINSVillageReportHistory[] = [{ id: 123 }, { id: 456 }, { id: 45001 }];
          const lLINSVillageReportHistoryCollection: ILLINSVillageReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLLINSVillageReportHistoryToCollectionIfMissing(
            lLINSVillageReportHistoryCollection,
            ...lLINSVillageReportHistoryArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 123 };
          const lLINSVillageReportHistory2: ILLINSVillageReportHistory = { id: 456 };
          expectedResult = service.addLLINSVillageReportHistoryToCollectionIfMissing(
            [],
            lLINSVillageReportHistory,
            lLINSVillageReportHistory2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSVillageReportHistory);
          expect(expectedResult).toContain(lLINSVillageReportHistory2);
        });

        it('should accept null and undefined values', () => {
          const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 123 };
          expectedResult = service.addLLINSVillageReportHistoryToCollectionIfMissing([], null, lLINSVillageReportHistory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSVillageReportHistory);
        });

        it('should return initial array if no LlinsVillageReportHistory is added', () => {
          const lLINSVillageReportHistoryCollection: ILLINSVillageReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLLINSVillageReportHistoryToCollectionIfMissing(lLINSVillageReportHistoryCollection, undefined, null);
          expect(expectedResult).toEqual(lLINSVillageReportHistoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
