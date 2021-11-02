import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILLINSVillageReport, LLINSVillageReport } from '../llins-village-report.model';

import { LLINSVillageReportService } from './llins-village-report.service';

describe('Service Tests', () => {
  describe('LLINSVillageReport Service', () => {
    let service: LLINSVillageReportService;
    let httpMock: HttpTestingController;
    let elemDefault: ILLINSVillageReport;
    let expectedResult: ILLINSVillageReport | ILLINSVillageReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LLINSVillageReportService);
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
        comment: 'AAAAAAA',
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

      it('should create a LLINSVillageReport', () => {
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

        service.create(new LLINSVillageReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LLINSVillageReport', () => {
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
            comment: 'BBBBBB',
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

      it('should partial update a LLINSVillageReport', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            houses: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            comment: 'BBBBBB',
          },
          new LLINSVillageReport()
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

      it('should return a list of LLINSVillageReport', () => {
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
            comment: 'BBBBBB',
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

      it('should delete a LLINSVillageReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLLINSVillageReportToCollectionIfMissing', () => {
        it('should add a LLINSVillageReport to an empty array', () => {
          const lLINSVillageReport: ILLINSVillageReport = { id: 123 };
          expectedResult = service.addLLINSVillageReportToCollectionIfMissing([], lLINSVillageReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSVillageReport);
        });

        it('should not add a LLINSVillageReport to an array that contains it', () => {
          const lLINSVillageReport: ILLINSVillageReport = { id: 123 };
          const lLINSVillageReportCollection: ILLINSVillageReport[] = [
            {
              ...lLINSVillageReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addLLINSVillageReportToCollectionIfMissing(lLINSVillageReportCollection, lLINSVillageReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LLINSVillageReport to an array that doesn't contain it", () => {
          const lLINSVillageReport: ILLINSVillageReport = { id: 123 };
          const lLINSVillageReportCollection: ILLINSVillageReport[] = [{ id: 456 }];
          expectedResult = service.addLLINSVillageReportToCollectionIfMissing(lLINSVillageReportCollection, lLINSVillageReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSVillageReport);
        });

        it('should add only unique LLINSVillageReport to an array', () => {
          const lLINSVillageReportArray: ILLINSVillageReport[] = [{ id: 123 }, { id: 456 }, { id: 80783 }];
          const lLINSVillageReportCollection: ILLINSVillageReport[] = [{ id: 123 }];
          expectedResult = service.addLLINSVillageReportToCollectionIfMissing(lLINSVillageReportCollection, ...lLINSVillageReportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lLINSVillageReport: ILLINSVillageReport = { id: 123 };
          const lLINSVillageReport2: ILLINSVillageReport = { id: 456 };
          expectedResult = service.addLLINSVillageReportToCollectionIfMissing([], lLINSVillageReport, lLINSVillageReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSVillageReport);
          expect(expectedResult).toContain(lLINSVillageReport2);
        });

        it('should accept null and undefined values', () => {
          const lLINSVillageReport: ILLINSVillageReport = { id: 123 };
          expectedResult = service.addLLINSVillageReportToCollectionIfMissing([], null, lLINSVillageReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSVillageReport);
        });

        it('should return initial array if no LLINSVillageReport is added', () => {
          const lLINSVillageReportCollection: ILLINSVillageReport[] = [{ id: 123 }];
          expectedResult = service.addLLINSVillageReportToCollectionIfMissing(lLINSVillageReportCollection, undefined, null);
          expect(expectedResult).toEqual(lLINSVillageReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
