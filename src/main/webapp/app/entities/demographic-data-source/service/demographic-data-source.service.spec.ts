import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDemographicDataSource, DemographicDataSource } from '../demographic-data-source.model';

import { DemographicDataSourceService } from './demographic-data-source.service';

describe('Service Tests', () => {
  describe('DemographicDataSource Service', () => {
    let service: DemographicDataSourceService;
    let httpMock: HttpTestingController;
    let elemDefault: IDemographicDataSource;
    let expectedResult: IDemographicDataSource | IDemographicDataSource[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DemographicDataSourceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
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

      it('should create a DemographicDataSource', () => {
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

        service.create(new DemographicDataSource()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DemographicDataSource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DemographicDataSource', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            name: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          new DemographicDataSource()
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

      it('should return a list of DemographicDataSource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DemographicDataSource', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDemographicDataSourceToCollectionIfMissing', () => {
        it('should add a DemographicDataSource to an empty array', () => {
          const demographicDataSource: IDemographicDataSource = { id: 123 };
          expectedResult = service.addDemographicDataSourceToCollectionIfMissing([], demographicDataSource);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demographicDataSource);
        });

        it('should not add a DemographicDataSource to an array that contains it', () => {
          const demographicDataSource: IDemographicDataSource = { id: 123 };
          const demographicDataSourceCollection: IDemographicDataSource[] = [
            {
              ...demographicDataSource,
            },
            { id: 456 },
          ];
          expectedResult = service.addDemographicDataSourceToCollectionIfMissing(demographicDataSourceCollection, demographicDataSource);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DemographicDataSource to an array that doesn't contain it", () => {
          const demographicDataSource: IDemographicDataSource = { id: 123 };
          const demographicDataSourceCollection: IDemographicDataSource[] = [{ id: 456 }];
          expectedResult = service.addDemographicDataSourceToCollectionIfMissing(demographicDataSourceCollection, demographicDataSource);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demographicDataSource);
        });

        it('should add only unique DemographicDataSource to an array', () => {
          const demographicDataSourceArray: IDemographicDataSource[] = [{ id: 123 }, { id: 456 }, { id: 62225 }];
          const demographicDataSourceCollection: IDemographicDataSource[] = [{ id: 123 }];
          expectedResult = service.addDemographicDataSourceToCollectionIfMissing(
            demographicDataSourceCollection,
            ...demographicDataSourceArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const demographicDataSource: IDemographicDataSource = { id: 123 };
          const demographicDataSource2: IDemographicDataSource = { id: 456 };
          expectedResult = service.addDemographicDataSourceToCollectionIfMissing([], demographicDataSource, demographicDataSource2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demographicDataSource);
          expect(expectedResult).toContain(demographicDataSource2);
        });

        it('should accept null and undefined values', () => {
          const demographicDataSource: IDemographicDataSource = { id: 123 };
          expectedResult = service.addDemographicDataSourceToCollectionIfMissing([], null, demographicDataSource, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demographicDataSource);
        });

        it('should return initial array if no DemographicDataSource is added', () => {
          const demographicDataSourceCollection: IDemographicDataSource[] = [{ id: 123 }];
          expectedResult = service.addDemographicDataSourceToCollectionIfMissing(demographicDataSourceCollection, undefined, null);
          expect(expectedResult).toEqual(demographicDataSourceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
