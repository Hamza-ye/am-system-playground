import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILlinsVillageReport, LlinsVillageReport } from '../llins-village-report.model';

import { LlinsVillageReportService } from './llins-village-report.service';

describe('Service Tests', () => {
  describe('LlinsVillageReport Service', () => {
    let service: LlinsVillageReportService;
    let httpMock: HttpTestingController;
    let elemDefault: ILlinsVillageReport;
    let expectedResult: ILlinsVillageReport | ILlinsVillageReport[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LlinsVillageReportService);
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

      it('should create a LlinsVillageReport', () => {
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

        service.create(new LlinsVillageReport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LlinsVillageReport', () => {
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

      it('should partial update a LlinsVillageReport', () => {
        const patchObject = Object.assign(
          {
            residentHousehold: 1,
            idpsHousehold: 1,
            femaleIndividuals: 1,
            lessThan5Males: 1,
            comment: 'BBBBBB',
          },
          new LlinsVillageReport()
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

      it('should return a list of LlinsVillageReport', () => {
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

      it('should delete a LlinsVillageReport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLlinsVillageReportToCollectionIfMissing', () => {
        it('should add a LlinsVillageReport to an empty array', () => {
          const llinsVillageReport: ILlinsVillageReport = { id: 123 };
          expectedResult = service.addLlinsVillageReportToCollectionIfMissing([], llinsVillageReport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsVillageReport);
        });

        it('should not add a LlinsVillageReport to an array that contains it', () => {
          const llinsVillageReport: ILlinsVillageReport = { id: 123 };
          const llinsVillageReportCollection: ILlinsVillageReport[] = [
            {
              ...llinsVillageReport,
            },
            { id: 456 },
          ];
          expectedResult = service.addLlinsVillageReportToCollectionIfMissing(llinsVillageReportCollection, llinsVillageReport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsVillageReport to an array that doesn't contain it", () => {
          const llinsVillageReport: ILlinsVillageReport = { id: 123 };
          const llinsVillageReportCollection: ILlinsVillageReport[] = [{ id: 456 }];
          expectedResult = service.addLlinsVillageReportToCollectionIfMissing(llinsVillageReportCollection, llinsVillageReport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsVillageReport);
        });

        it('should add only unique LlinsVillageReport to an array', () => {
          const llinsVillageReportArray: ILlinsVillageReport[] = [{ id: 123 }, { id: 456 }, { id: 38225 }];
          const llinsVillageReportCollection: ILlinsVillageReport[] = [{ id: 123 }];
          expectedResult = service.addLlinsVillageReportToCollectionIfMissing(llinsVillageReportCollection, ...llinsVillageReportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const llinsVillageReport: ILlinsVillageReport = { id: 123 };
          const llinsVillageReport2: ILlinsVillageReport = { id: 456 };
          expectedResult = service.addLlinsVillageReportToCollectionIfMissing([], llinsVillageReport, llinsVillageReport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsVillageReport);
          expect(expectedResult).toContain(llinsVillageReport2);
        });

        it('should accept null and undefined values', () => {
          const llinsVillageReport: ILlinsVillageReport = { id: 123 };
          expectedResult = service.addLlinsVillageReportToCollectionIfMissing([], null, llinsVillageReport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsVillageReport);
        });

        it('should return initial array if no LlinsVillageReport is added', () => {
          const llinsVillageReportCollection: ILlinsVillageReport[] = [{ id: 123 }];
          expectedResult = service.addLlinsVillageReportToCollectionIfMissing(llinsVillageReportCollection, undefined, null);
          expect(expectedResult).toEqual(llinsVillageReportCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
