import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICHVMalariaReportVersion1, CHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';

import { CHVMalariaReportVersion1Service } from './chv-malaria-report-version-1.service';

describe('Service Tests', () => {
  describe('CHVMalariaReportVersion1 Service', () => {
    let service: CHVMalariaReportVersion1Service;
    let httpMock: HttpTestingController;
    let elemDefault: ICHVMalariaReportVersion1;
    let expectedResult: ICHVMalariaReportVersion1 | ICHVMalariaReportVersion1[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CHVMalariaReportVersion1Service);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        tested: 0,
        positive: 0,
        drugsGiven: 0,
        suppsGiven: 0,
        rdtBalance: 0,
        rdtReceived: 0,
        rdtUsed: 0,
        rdtDamagedLost: 0,
        drugsBalance: 0,
        drugsReceived: 0,
        drugsUsed: 0,
        drugsDamagedLost: 0,
        suppsBalance: 0,
        suppsReceived: 0,
        suppsUsed: 0,
        suppsDamagedLost: 0,
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

      it('should create a CHVMalariaReportVersion1', () => {
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

        service.create(new CHVMalariaReportVersion1()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CHVMalariaReportVersion1', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            tested: 1,
            positive: 1,
            drugsGiven: 1,
            suppsGiven: 1,
            rdtBalance: 1,
            rdtReceived: 1,
            rdtUsed: 1,
            rdtDamagedLost: 1,
            drugsBalance: 1,
            drugsReceived: 1,
            drugsUsed: 1,
            drugsDamagedLost: 1,
            suppsBalance: 1,
            suppsReceived: 1,
            suppsUsed: 1,
            suppsDamagedLost: 1,
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

      it('should partial update a CHVMalariaReportVersion1', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            positive: 1,
            suppsGiven: 1,
            rdtBalance: 1,
            rdtReceived: 1,
            rdtUsed: 1,
            drugsReceived: 1,
            drugsDamagedLost: 1,
            suppsReceived: 1,
            suppsDamagedLost: 1,
          },
          new CHVMalariaReportVersion1()
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

      it('should return a list of CHVMalariaReportVersion1', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            tested: 1,
            positive: 1,
            drugsGiven: 1,
            suppsGiven: 1,
            rdtBalance: 1,
            rdtReceived: 1,
            rdtUsed: 1,
            rdtDamagedLost: 1,
            drugsBalance: 1,
            drugsReceived: 1,
            drugsUsed: 1,
            drugsDamagedLost: 1,
            suppsBalance: 1,
            suppsReceived: 1,
            suppsUsed: 1,
            suppsDamagedLost: 1,
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

      it('should delete a CHVMalariaReportVersion1', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCHVMalariaReportVersion1ToCollectionIfMissing', () => {
        it('should add a CHVMalariaReportVersion1 to an empty array', () => {
          const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 123 };
          expectedResult = service.addCHVMalariaReportVersion1ToCollectionIfMissing([], cHVMalariaReportVersion1);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHVMalariaReportVersion1);
        });

        it('should not add a CHVMalariaReportVersion1 to an array that contains it', () => {
          const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 123 };
          const cHVMalariaReportVersion1Collection: ICHVMalariaReportVersion1[] = [
            {
              ...cHVMalariaReportVersion1,
            },
            { id: 456 },
          ];
          expectedResult = service.addCHVMalariaReportVersion1ToCollectionIfMissing(
            cHVMalariaReportVersion1Collection,
            cHVMalariaReportVersion1
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CHVMalariaReportVersion1 to an array that doesn't contain it", () => {
          const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 123 };
          const cHVMalariaReportVersion1Collection: ICHVMalariaReportVersion1[] = [{ id: 456 }];
          expectedResult = service.addCHVMalariaReportVersion1ToCollectionIfMissing(
            cHVMalariaReportVersion1Collection,
            cHVMalariaReportVersion1
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHVMalariaReportVersion1);
        });

        it('should add only unique CHVMalariaReportVersion1 to an array', () => {
          const cHVMalariaReportVersion1Array: ICHVMalariaReportVersion1[] = [{ id: 123 }, { id: 456 }, { id: 14392 }];
          const cHVMalariaReportVersion1Collection: ICHVMalariaReportVersion1[] = [{ id: 123 }];
          expectedResult = service.addCHVMalariaReportVersion1ToCollectionIfMissing(
            cHVMalariaReportVersion1Collection,
            ...cHVMalariaReportVersion1Array
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 123 };
          const cHVMalariaReportVersion12: ICHVMalariaReportVersion1 = { id: 456 };
          expectedResult = service.addCHVMalariaReportVersion1ToCollectionIfMissing(
            [],
            cHVMalariaReportVersion1,
            cHVMalariaReportVersion12
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHVMalariaReportVersion1);
          expect(expectedResult).toContain(cHVMalariaReportVersion12);
        });

        it('should accept null and undefined values', () => {
          const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 123 };
          expectedResult = service.addCHVMalariaReportVersion1ToCollectionIfMissing([], null, cHVMalariaReportVersion1, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHVMalariaReportVersion1);
        });

        it('should return initial array if no CHVMalariaReportVersion1 is added', () => {
          const cHVMalariaReportVersion1Collection: ICHVMalariaReportVersion1[] = [{ id: 123 }];
          expectedResult = service.addCHVMalariaReportVersion1ToCollectionIfMissing(cHVMalariaReportVersion1Collection, undefined, null);
          expect(expectedResult).toEqual(cHVMalariaReportVersion1Collection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
