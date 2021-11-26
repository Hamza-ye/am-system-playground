import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILlinsVillageReportHistory, LlinsVillageReportHistory } from '../llins-village-report-history.model';

import { LlinsVillageReportHistoryService } from './llins-village-report-history.service';

describe('Service Tests', () => {
  describe('LlinsVillageReportHistory Service', () => {
    let service: LlinsVillageReportHistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: ILlinsVillageReportHistory;
    let expectedResult: ILlinsVillageReportHistory | ILlinsVillageReportHistory[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LlinsVillageReportHistoryService);
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

        service.create(new LlinsVillageReportHistory()).subscribe(resp => (expectedResult = resp.body));

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
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            houses: 1,
            idpsHousehold: 1,
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            quantityReceived: 1,
          },
          new LlinsVillageReportHistory()
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

      describe('addLlinsVillageReportHistoryToCollectionIfMissing', () => {
        it('should add a LlinsVillageReportHistory to an empty array', () => {
          const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 123 };
          expectedResult = service.addLlinsVillageReportHistoryToCollectionIfMissing([], llinsVillageReportHistory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsVillageReportHistory);
        });

        it('should not add a LlinsVillageReportHistory to an array that contains it', () => {
          const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 123 };
          const llinsVillageReportHistoryCollection: ILlinsVillageReportHistory[] = [
            {
              ...llinsVillageReportHistory,
            },
            { id: 456 },
          ];
          expectedResult = service.addLlinsVillageReportHistoryToCollectionIfMissing(
            llinsVillageReportHistoryCollection,
            llinsVillageReportHistory
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsVillageReportHistory to an array that doesn't contain it", () => {
          const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 123 };
          const llinsVillageReportHistoryCollection: ILlinsVillageReportHistory[] = [{ id: 456 }];
          expectedResult = service.addLlinsVillageReportHistoryToCollectionIfMissing(
            llinsVillageReportHistoryCollection,
            llinsVillageReportHistory
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsVillageReportHistory);
        });

        it('should add only unique LlinsVillageReportHistory to an array', () => {
          const llinsVillageReportHistoryArray: ILlinsVillageReportHistory[] = [{ id: 123 }, { id: 456 }, { id: 66706 }];
          const llinsVillageReportHistoryCollection: ILlinsVillageReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLlinsVillageReportHistoryToCollectionIfMissing(
            llinsVillageReportHistoryCollection,
            ...llinsVillageReportHistoryArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 123 };
          const llinsVillageReportHistory2: ILlinsVillageReportHistory = { id: 456 };
          expectedResult = service.addLlinsVillageReportHistoryToCollectionIfMissing(
            [],
            llinsVillageReportHistory,
            llinsVillageReportHistory2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsVillageReportHistory);
          expect(expectedResult).toContain(llinsVillageReportHistory2);
        });

        it('should accept null and undefined values', () => {
          const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 123 };
          expectedResult = service.addLlinsVillageReportHistoryToCollectionIfMissing([], null, llinsVillageReportHistory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsVillageReportHistory);
        });

        it('should return initial array if no LlinsVillageReportHistory is added', () => {
          const llinsVillageReportHistoryCollection: ILlinsVillageReportHistory[] = [{ id: 123 }];
          expectedResult = service.addLlinsVillageReportHistoryToCollectionIfMissing(llinsVillageReportHistoryCollection, undefined, null);
          expect(expectedResult).toEqual(llinsVillageReportHistoryCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
