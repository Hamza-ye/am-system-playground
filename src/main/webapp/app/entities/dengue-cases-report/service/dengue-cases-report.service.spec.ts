import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDengueCasesReport, DengueCasesReport } from '../dengue-cases-report.model';

import { DengueCasesReportService } from './dengue-cases-report.service';

describe('Service Tests', () => {
  describe('DengueCasesReport Service', () => {
    let service: DengueCasesReportService;
    let httpMock: HttpTestingController;
    let elemDefault: IDengueCasesReport;
    let expectedResult: IDengueCasesReport | IDengueCasesReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DengueCasesReportService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        rdtTested: 0,
        rdtPositive: 0,
        probableCases: 0,
        inpatientCases: 0,
        deathCases: 0,
        treated: 0,
        suspectedCases: 0,
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

      it('should create a DengueCasesReport', () => {
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

        service.create(new DengueCasesReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DengueCasesReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            rdtTested: 1,
            rdtPositive: 1,
            probableCases: 1,
            inpatientCases: 1,
            deathCases: 1,
            treated: 1,
            suspectedCases: 1,
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

      it('should partial update a DengueCasesReport', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            probableCases: 1,
            inpatientCases: 1,
            suspectedCases: 1,
          },
          new DengueCasesReport()
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

      it('should return a list of DengueCasesReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            rdtTested: 1,
            rdtPositive: 1,
            probableCases: 1,
            inpatientCases: 1,
            deathCases: 1,
            treated: 1,
            suspectedCases: 1,
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

      it('should delete a DengueCasesReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDengueCasesReportToCollectionIfMissing', () => {
        it('should add a DengueCasesReport to an empty array', () => {
          const dengueCasesReport: IDengueCasesReport = { id: 123 };
          expectedResult = service.addDengueCasesReportToCollectionIfMissing([], dengueCasesReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dengueCasesReport);
        });

        it('should not add a DengueCasesReport to an array that contains it', () => {
          const dengueCasesReport: IDengueCasesReport = { id: 123 };
          const dengueCasesReportCollection: IDengueCasesReport[] = [
            {
              ...dengueCasesReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addDengueCasesReportToCollectionIfMissing(dengueCasesReportCollection, dengueCasesReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DengueCasesReport to an array that doesn't contain it", () => {
          const dengueCasesReport: IDengueCasesReport = { id: 123 };
          const dengueCasesReportCollection: IDengueCasesReport[] = [{ id: 456 }];
          expectedResult = service.addDengueCasesReportToCollectionIfMissing(dengueCasesReportCollection, dengueCasesReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dengueCasesReport);
        });

        it('should add only unique DengueCasesReport to an array', () => {
          const dengueCasesReportArray: IDengueCasesReport[] = [{ id: 123 }, { id: 456 }, { id: 25053 }];
          const dengueCasesReportCollection: IDengueCasesReport[] = [{ id: 123 }];
          expectedResult = service.addDengueCasesReportToCollectionIfMissing(dengueCasesReportCollection, ...dengueCasesReportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dengueCasesReport: IDengueCasesReport = { id: 123 };
          const dengueCasesReport2: IDengueCasesReport = { id: 456 };
          expectedResult = service.addDengueCasesReportToCollectionIfMissing([], dengueCasesReport, dengueCasesReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dengueCasesReport);
          expect(expectedResult).toContain(dengueCasesReport2);
        });

        it('should accept null and undefined values', () => {
          const dengueCasesReport: IDengueCasesReport = { id: 123 };
          expectedResult = service.addDengueCasesReportToCollectionIfMissing([], null, dengueCasesReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dengueCasesReport);
        });

        it('should return initial array if no DengueCasesReport is added', () => {
          const dengueCasesReportCollection: IDengueCasesReport[] = [{ id: 123 }];
          expectedResult = service.addDengueCasesReportToCollectionIfMissing(dengueCasesReportCollection, undefined, null);
          expect(expectedResult).toEqual(dengueCasesReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
