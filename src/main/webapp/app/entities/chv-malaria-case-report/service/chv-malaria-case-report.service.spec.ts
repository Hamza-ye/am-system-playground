import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MalariaTestResult } from 'app/entities/enumerations/malaria-test-result.model';
import { IChvMalariaCaseReport, ChvMalariaCaseReport } from '../chv-malaria-case-report.model';

import { ChvMalariaCaseReportService } from './chv-malaria-case-report.service';

describe('Service Tests', () => {
  describe('ChvMalariaCaseReport Service', () => {
    let service: ChvMalariaCaseReportService;
    let httpMock: HttpTestingController;
    let elemDefault: IChvMalariaCaseReport;
    let expectedResult: IChvMalariaCaseReport | IChvMalariaCaseReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChvMalariaCaseReportService);
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

      it('should create a ChvMalariaCaseReport', () => {
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

        service.create(new ChvMalariaCaseReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChvMalariaCaseReport', () => {
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

      it('should partial update a ChvMalariaCaseReport', () => {
        const patchObject = Object.assign(
          {
            individualName: 'BBBBBB',
            drugsGiven: 1,
            suppsGiven: 1,
            barImageUrl: 'BBBBBB',
            comment: 'BBBBBB',
          },
          new ChvMalariaCaseReport()
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

      it('should return a list of ChvMalariaCaseReport', () => {
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

      it('should delete a ChvMalariaCaseReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChvMalariaCaseReportToCollectionIfMissing', () => {
        it('should add a ChvMalariaCaseReport to an empty array', () => {
          const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 123 };
          expectedResult = service.addChvMalariaCaseReportToCollectionIfMissing([], chvMalariaCaseReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chvMalariaCaseReport);
        });

        it('should not add a ChvMalariaCaseReport to an array that contains it', () => {
          const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 123 };
          const chvMalariaCaseReportCollection: IChvMalariaCaseReport[] = [
            {
              ...chvMalariaCaseReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addChvMalariaCaseReportToCollectionIfMissing(chvMalariaCaseReportCollection, chvMalariaCaseReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChvMalariaCaseReport to an array that doesn't contain it", () => {
          const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 123 };
          const chvMalariaCaseReportCollection: IChvMalariaCaseReport[] = [{ id: 456 }];
          expectedResult = service.addChvMalariaCaseReportToCollectionIfMissing(chvMalariaCaseReportCollection, chvMalariaCaseReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chvMalariaCaseReport);
        });

        it('should add only unique ChvMalariaCaseReport to an array', () => {
          const chvMalariaCaseReportArray: IChvMalariaCaseReport[] = [{ id: 123 }, { id: 456 }, { id: 69356 }];
          const chvMalariaCaseReportCollection: IChvMalariaCaseReport[] = [{ id: 123 }];
          expectedResult = service.addChvMalariaCaseReportToCollectionIfMissing(
            chvMalariaCaseReportCollection,
            ...chvMalariaCaseReportArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 123 };
          const chvMalariaCaseReport2: IChvMalariaCaseReport = { id: 456 };
          expectedResult = service.addChvMalariaCaseReportToCollectionIfMissing([], chvMalariaCaseReport, chvMalariaCaseReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chvMalariaCaseReport);
          expect(expectedResult).toContain(chvMalariaCaseReport2);
        });

        it('should accept null and undefined values', () => {
          const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 123 };
          expectedResult = service.addChvMalariaCaseReportToCollectionIfMissing([], null, chvMalariaCaseReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chvMalariaCaseReport);
        });

        it('should return initial array if no ChvMalariaCaseReport is added', () => {
          const chvMalariaCaseReportCollection: IChvMalariaCaseReport[] = [{ id: 123 }];
          expectedResult = service.addChvMalariaCaseReportToCollectionIfMissing(chvMalariaCaseReportCollection, undefined, null);
          expect(expectedResult).toEqual(chvMalariaCaseReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
