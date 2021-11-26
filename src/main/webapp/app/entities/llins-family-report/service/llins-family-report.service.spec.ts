import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { FamilyType } from 'app/entities/enumerations/family-type.model';
import { ILLINSFamilyReport, LLINSFamilyReport } from '../llins-family-report.model';

import { LLINSFamilyReportService } from './llins-family-report.service';

describe('Service Tests', () => {
  describe('LlinsFamilyReport Service', () => {
    let service: LLINSFamilyReportService;
    let httpMock: HttpTestingController;
    let elemDefault: ILLINSFamilyReport;
    let expectedResult: ILLINSFamilyReport | ILLINSFamilyReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LLINSFamilyReportService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        checkNo: 0,
        maleIndividuals: 0,
        femaleIndividuals: 0,
        lessThan5Males: 0,
        lessThan5Females: 0,
        pregnantWomen: 0,
        quantityReceived: 0,
        familyType: FamilyType.RESIDENT,
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

      it('should create a LlinsFamilyReport', () => {
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

        service.create(new LLINSFamilyReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LlinsFamilyReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            checkNo: 1,
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
            familyType: 'BBBBBB',
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

      it('should partial update a LlinsFamilyReport', () => {
        const patchObject = Object.assign(
          {
            checkNo: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            familyType: 'BBBBBB',
          },
          new LLINSFamilyReport()
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

      it('should return a list of LlinsFamilyReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            checkNo: 1,
            maleIndividuals: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            lessThan5Females: 1,
            pregnantWomen: 1,
            quantityReceived: 1,
            familyType: 'BBBBBB',
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

      it('should delete a LlinsFamilyReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLLINSFamilyReportToCollectionIfMissing', () => {
        it('should add a LlinsFamilyReport to an empty array', () => {
          const lLINSFamilyReport: ILLINSFamilyReport = { id: 123 };
          expectedResult = service.addLLINSFamilyReportToCollectionIfMissing([], lLINSFamilyReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSFamilyReport);
        });

        it('should not add a LlinsFamilyReport to an array that contains it', () => {
          const lLINSFamilyReport: ILLINSFamilyReport = { id: 123 };
          const lLINSFamilyReportCollection: ILLINSFamilyReport[] = [
            {
              ...lLINSFamilyReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addLLINSFamilyReportToCollectionIfMissing(lLINSFamilyReportCollection, lLINSFamilyReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsFamilyReport to an array that doesn't contain it", () => {
          const lLINSFamilyReport: ILLINSFamilyReport = { id: 123 };
          const lLINSFamilyReportCollection: ILLINSFamilyReport[] = [{ id: 456 }];
          expectedResult = service.addLLINSFamilyReportToCollectionIfMissing(lLINSFamilyReportCollection, lLINSFamilyReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSFamilyReport);
        });

        it('should add only unique LlinsFamilyReport to an array', () => {
          const lLINSFamilyReportArray: ILLINSFamilyReport[] = [{ id: 123 }, { id: 456 }, { id: 44267 }];
          const lLINSFamilyReportCollection: ILLINSFamilyReport[] = [{ id: 123 }];
          expectedResult = service.addLLINSFamilyReportToCollectionIfMissing(lLINSFamilyReportCollection, ...lLINSFamilyReportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lLINSFamilyReport: ILLINSFamilyReport = { id: 123 };
          const lLINSFamilyReport2: ILLINSFamilyReport = { id: 456 };
          expectedResult = service.addLLINSFamilyReportToCollectionIfMissing([], lLINSFamilyReport, lLINSFamilyReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSFamilyReport);
          expect(expectedResult).toContain(lLINSFamilyReport2);
        });

        it('should accept null and undefined values', () => {
          const lLINSFamilyReport: ILLINSFamilyReport = { id: 123 };
          expectedResult = service.addLLINSFamilyReportToCollectionIfMissing([], null, lLINSFamilyReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSFamilyReport);
        });

        it('should return initial array if no LlinsFamilyReport is added', () => {
          const lLINSFamilyReportCollection: ILLINSFamilyReport[] = [{ id: 123 }];
          expectedResult = service.addLLINSFamilyReportToCollectionIfMissing(lLINSFamilyReportCollection, undefined, null);
          expect(expectedResult).toEqual(lLINSFamilyReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
