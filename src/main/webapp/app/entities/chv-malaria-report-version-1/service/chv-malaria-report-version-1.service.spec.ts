import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChvMalariaReportVersion1, ChvMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';

import { ChvMalariaReportVersion1Service } from './chv-malaria-report-version-1.service';

describe('Service Tests', () => {
  describe('ChvMalariaReportVersion1 Service', () => {
    let service: ChvMalariaReportVersion1Service;
    let httpMock: HttpTestingController;
    let elemDefault: IChvMalariaReportVersion1;
    let expectedResult: IChvMalariaReportVersion1 | IChvMalariaReportVersion1[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChvMalariaReportVersion1Service);
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

      it('should create a ChvMalariaReportVersion1', () => {
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

        service.create(new ChvMalariaReportVersion1()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChvMalariaReportVersion1', () => {
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

      it('should partial update a ChvMalariaReportVersion1', () => {
        const patchObject = Object.assign(
          {
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            tested: 1,
            positive: 1,
            suppsGiven: 1,
            rdtUsed: 1,
            drugsReceived: 1,
            suppsBalance: 1,
            suppsUsed: 1,
            suppsDamagedLost: 1,
          },
          new ChvMalariaReportVersion1()
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

      it('should return a list of ChvMalariaReportVersion1', () => {
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

      it('should delete a ChvMalariaReportVersion1', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChvMalariaReportVersion1ToCollectionIfMissing', () => {
        it('should add a ChvMalariaReportVersion1 to an empty array', () => {
          const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 123 };
          expectedResult = service.addChvMalariaReportVersion1ToCollectionIfMissing([], chvMalariaReportVersion1);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chvMalariaReportVersion1);
        });

        it('should not add a ChvMalariaReportVersion1 to an array that contains it', () => {
          const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 123 };
          const chvMalariaReportVersion1Collection: IChvMalariaReportVersion1[] = [
            {
              ...chvMalariaReportVersion1,
            },
            { id: 456 },
          ];
          expectedResult = service.addChvMalariaReportVersion1ToCollectionIfMissing(
            chvMalariaReportVersion1Collection,
            chvMalariaReportVersion1
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChvMalariaReportVersion1 to an array that doesn't contain it", () => {
          const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 123 };
          const chvMalariaReportVersion1Collection: IChvMalariaReportVersion1[] = [{ id: 456 }];
          expectedResult = service.addChvMalariaReportVersion1ToCollectionIfMissing(
            chvMalariaReportVersion1Collection,
            chvMalariaReportVersion1
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chvMalariaReportVersion1);
        });

        it('should add only unique ChvMalariaReportVersion1 to an array', () => {
          const chvMalariaReportVersion1Array: IChvMalariaReportVersion1[] = [{ id: 123 }, { id: 456 }, { id: 75218 }];
          const chvMalariaReportVersion1Collection: IChvMalariaReportVersion1[] = [{ id: 123 }];
          expectedResult = service.addChvMalariaReportVersion1ToCollectionIfMissing(
            chvMalariaReportVersion1Collection,
            ...chvMalariaReportVersion1Array
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 123 };
          const chvMalariaReportVersion12: IChvMalariaReportVersion1 = { id: 456 };
          expectedResult = service.addChvMalariaReportVersion1ToCollectionIfMissing(
            [],
            chvMalariaReportVersion1,
            chvMalariaReportVersion12
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chvMalariaReportVersion1);
          expect(expectedResult).toContain(chvMalariaReportVersion12);
        });

        it('should accept null and undefined values', () => {
          const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 123 };
          expectedResult = service.addChvMalariaReportVersion1ToCollectionIfMissing([], null, chvMalariaReportVersion1, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chvMalariaReportVersion1);
        });

        it('should return initial array if no ChvMalariaReportVersion1 is added', () => {
          const chvMalariaReportVersion1Collection: IChvMalariaReportVersion1[] = [{ id: 123 }];
          expectedResult = service.addChvMalariaReportVersion1ToCollectionIfMissing(chvMalariaReportVersion1Collection, undefined, null);
          expect(expectedResult).toEqual(chvMalariaReportVersion1Collection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
