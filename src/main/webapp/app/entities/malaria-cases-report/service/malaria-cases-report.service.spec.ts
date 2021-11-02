import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMalariaCasesReport, MalariaCasesReport } from '../malaria-cases-report.model';

import { MalariaCasesReportService } from './malaria-cases-report.service';

describe('Service Tests', () => {
  describe('MalariaCasesReport Service', () => {
    let service: MalariaCasesReportService;
    let httpMock: HttpTestingController;
    let elemDefault: IMalariaCasesReport;
    let expectedResult: IMalariaCasesReport | IMalariaCasesReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MalariaCasesReportService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        rdtTested: 0,
        rdtPositive: 0,
        rdtPf: 0,
        rdtPv: 0,
        rdtPother: 0,
        microTested: 0,
        microPositive: 0,
        microPf: 0,
        microPv: 0,
        microMix: 0,
        microPother: 0,
        probableCases: 0,
        inpatientCases: 0,
        deathCases: 0,
        treated: 0,
        suspectedCases: 0,
        totalFrequents: 0,
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

      it('should create a MalariaCasesReport', () => {
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

        service.create(new MalariaCasesReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MalariaCasesReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            rdtTested: 1,
            rdtPositive: 1,
            rdtPf: 1,
            rdtPv: 1,
            rdtPother: 1,
            microTested: 1,
            microPositive: 1,
            microPf: 1,
            microPv: 1,
            microMix: 1,
            microPother: 1,
            probableCases: 1,
            inpatientCases: 1,
            deathCases: 1,
            treated: 1,
            suspectedCases: 1,
            totalFrequents: 1,
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

      it('should partial update a MalariaCasesReport', () => {
        const patchObject = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            rdtPv: 1,
            microTested: 1,
            microPv: 1,
            microMix: 1,
            probableCases: 1,
            inpatientCases: 1,
            treated: 1,
            suspectedCases: 1,
          },
          new MalariaCasesReport()
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

      it('should return a list of MalariaCasesReport', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            rdtTested: 1,
            rdtPositive: 1,
            rdtPf: 1,
            rdtPv: 1,
            rdtPother: 1,
            microTested: 1,
            microPositive: 1,
            microPf: 1,
            microPv: 1,
            microMix: 1,
            microPother: 1,
            probableCases: 1,
            inpatientCases: 1,
            deathCases: 1,
            treated: 1,
            suspectedCases: 1,
            totalFrequents: 1,
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

      it('should delete a MalariaCasesReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMalariaCasesReportToCollectionIfMissing', () => {
        it('should add a MalariaCasesReport to an empty array', () => {
          const malariaCasesReport: IMalariaCasesReport = { id: 123 };
          expectedResult = service.addMalariaCasesReportToCollectionIfMissing([], malariaCasesReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(malariaCasesReport);
        });

        it('should not add a MalariaCasesReport to an array that contains it', () => {
          const malariaCasesReport: IMalariaCasesReport = { id: 123 };
          const malariaCasesReportCollection: IMalariaCasesReport[] = [
            {
              ...malariaCasesReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addMalariaCasesReportToCollectionIfMissing(malariaCasesReportCollection, malariaCasesReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MalariaCasesReport to an array that doesn't contain it", () => {
          const malariaCasesReport: IMalariaCasesReport = { id: 123 };
          const malariaCasesReportCollection: IMalariaCasesReport[] = [{ id: 456 }];
          expectedResult = service.addMalariaCasesReportToCollectionIfMissing(malariaCasesReportCollection, malariaCasesReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(malariaCasesReport);
        });

        it('should add only unique MalariaCasesReport to an array', () => {
          const malariaCasesReportArray: IMalariaCasesReport[] = [{ id: 123 }, { id: 456 }, { id: 90534 }];
          const malariaCasesReportCollection: IMalariaCasesReport[] = [{ id: 123 }];
          expectedResult = service.addMalariaCasesReportToCollectionIfMissing(malariaCasesReportCollection, ...malariaCasesReportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const malariaCasesReport: IMalariaCasesReport = { id: 123 };
          const malariaCasesReport2: IMalariaCasesReport = { id: 456 };
          expectedResult = service.addMalariaCasesReportToCollectionIfMissing([], malariaCasesReport, malariaCasesReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(malariaCasesReport);
          expect(expectedResult).toContain(malariaCasesReport2);
        });

        it('should accept null and undefined values', () => {
          const malariaCasesReport: IMalariaCasesReport = { id: 123 };
          expectedResult = service.addMalariaCasesReportToCollectionIfMissing([], null, malariaCasesReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(malariaCasesReport);
        });

        it('should return initial array if no MalariaCasesReport is added', () => {
          const malariaCasesReportCollection: IMalariaCasesReport[] = [{ id: 123 }];
          expectedResult = service.addMalariaCasesReportToCollectionIfMissing(malariaCasesReportCollection, undefined, null);
          expect(expectedResult).toEqual(malariaCasesReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
