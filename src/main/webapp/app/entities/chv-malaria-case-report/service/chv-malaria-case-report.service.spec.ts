import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MalariaTestResult } from 'app/entities/enumerations/malaria-test-result.model';
import { ICHVMalariaCaseReport, CHVMalariaCaseReport } from '../chv-malaria-case-report.model';

import { CHVMalariaCaseReportService } from './chv-malaria-case-report.service';

describe('Service Tests', () => {
  describe('CHVMalariaCaseReport Service', () => {
    let service: CHVMalariaCaseReportService;
    let httpMock: HttpTestingController;
    let elemDefault: ICHVMalariaCaseReport;
    let expectedResult: ICHVMalariaCaseReport | ICHVMalariaCaseReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CHVMalariaCaseReportService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        date: currentDate,
        individualName: 'AAAAAAA',
        gender: Gender.MALE,
        isPregnant: false,
        malariaTestResult: MalariaTestResult.POSITIVE,
        drugsGiven: 0,
        suppsGiven: 0,
        referral: false,
        barImageUrl: 'AAAAAAA',
        comment: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CHVMalariaCaseReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new CHVMalariaCaseReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CHVMalariaCaseReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
            individualName: 'BBBBBB',
            gender: 'BBBBBB',
            isPregnant: true,
            malariaTestResult: 'BBBBBB',
            drugsGiven: 1,
            suppsGiven: 1,
            referral: true,
            barImageUrl: 'BBBBBB',
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CHVMalariaCaseReport', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            individualName: 'BBBBBB',
            isPregnant: true,
            malariaTestResult: 'BBBBBB',
            drugsGiven: 1,
            suppsGiven: 1,
            barImageUrl: 'BBBBBB',
          },
          new CHVMalariaCaseReport()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CHVMalariaCaseReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
            individualName: 'BBBBBB',
            gender: 'BBBBBB',
            isPregnant: true,
            malariaTestResult: 'BBBBBB',
            drugsGiven: 1,
            suppsGiven: 1,
            referral: true,
            barImageUrl: 'BBBBBB',
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a CHVMalariaCaseReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCHVMalariaCaseReportToCollectionIfMissing', () => {
        it('should add a CHVMalariaCaseReport to an empty array', () => {
          const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 123 };
          expectedResult = service.addCHVMalariaCaseReportToCollectionIfMissing([], cHVMalariaCaseReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHVMalariaCaseReport);
        });

        it('should not add a CHVMalariaCaseReport to an array that contains it', () => {
          const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 123 };
          const cHVMalariaCaseReportCollection: ICHVMalariaCaseReport[] = [
            {
              ...cHVMalariaCaseReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addCHVMalariaCaseReportToCollectionIfMissing(cHVMalariaCaseReportCollection, cHVMalariaCaseReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CHVMalariaCaseReport to an array that doesn't contain it", () => {
          const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 123 };
          const cHVMalariaCaseReportCollection: ICHVMalariaCaseReport[] = [{ id: 456 }];
          expectedResult = service.addCHVMalariaCaseReportToCollectionIfMissing(cHVMalariaCaseReportCollection, cHVMalariaCaseReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHVMalariaCaseReport);
        });

        it('should add only unique CHVMalariaCaseReport to an array', () => {
          const cHVMalariaCaseReportArray: ICHVMalariaCaseReport[] = [{ id: 123 }, { id: 456 }, { id: 91831 }];
          const cHVMalariaCaseReportCollection: ICHVMalariaCaseReport[] = [{ id: 123 }];
          expectedResult = service.addCHVMalariaCaseReportToCollectionIfMissing(
            cHVMalariaCaseReportCollection,
            ...cHVMalariaCaseReportArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 123 };
          const cHVMalariaCaseReport2: ICHVMalariaCaseReport = { id: 456 };
          expectedResult = service.addCHVMalariaCaseReportToCollectionIfMissing([], cHVMalariaCaseReport, cHVMalariaCaseReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHVMalariaCaseReport);
          expect(expectedResult).toContain(cHVMalariaCaseReport2);
        });

        it('should accept null and undefined values', () => {
          const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 123 };
          expectedResult = service.addCHVMalariaCaseReportToCollectionIfMissing([], null, cHVMalariaCaseReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHVMalariaCaseReport);
        });

        it('should return initial array if no CHVMalariaCaseReport is added', () => {
          const cHVMalariaCaseReportCollection: ICHVMalariaCaseReport[] = [{ id: 123 }];
          expectedResult = service.addCHVMalariaCaseReportToCollectionIfMissing(cHVMalariaCaseReportCollection, undefined, null);
          expect(expectedResult).toEqual(cHVMalariaCaseReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
