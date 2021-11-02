import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDataProvider, DataProvider } from '../data-provider.model';

import { DataProviderService } from './data-provider.service';

describe('Service Tests', () => {
  describe('DataProvider Service', () => {
    let service: DataProviderService;
    let httpMock: HttpTestingController;
    let elemDefault: IDataProvider;
    let expectedResult: IDataProvider | IDataProvider[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DataProviderService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        mobile: 'AAAAAAA',
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

      it('should create a DataProvider', () => {
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

        service.create(new DataProvider()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DataProvider', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            mobile: 'BBBBBB',
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

      it('should partial update a DataProvider', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          new DataProvider()
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

      it('should return a list of DataProvider', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            mobile: 'BBBBBB',
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

      it('should delete a DataProvider', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDataProviderToCollectionIfMissing', () => {
        it('should add a DataProvider to an empty array', () => {
          const dataProvider: IDataProvider = { id: 123 };
          expectedResult = service.addDataProviderToCollectionIfMissing([], dataProvider);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dataProvider);
        });

        it('should not add a DataProvider to an array that contains it', () => {
          const dataProvider: IDataProvider = { id: 123 };
          const dataProviderCollection: IDataProvider[] = [
            {
              ...dataProvider,
            },
            { id: 456 },
          ];
          expectedResult = service.addDataProviderToCollectionIfMissing(dataProviderCollection, dataProvider);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DataProvider to an array that doesn't contain it", () => {
          const dataProvider: IDataProvider = { id: 123 };
          const dataProviderCollection: IDataProvider[] = [{ id: 456 }];
          expectedResult = service.addDataProviderToCollectionIfMissing(dataProviderCollection, dataProvider);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dataProvider);
        });

        it('should add only unique DataProvider to an array', () => {
          const dataProviderArray: IDataProvider[] = [{ id: 123 }, { id: 456 }, { id: 61089 }];
          const dataProviderCollection: IDataProvider[] = [{ id: 123 }];
          expectedResult = service.addDataProviderToCollectionIfMissing(dataProviderCollection, ...dataProviderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dataProvider: IDataProvider = { id: 123 };
          const dataProvider2: IDataProvider = { id: 456 };
          expectedResult = service.addDataProviderToCollectionIfMissing([], dataProvider, dataProvider2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dataProvider);
          expect(expectedResult).toContain(dataProvider2);
        });

        it('should accept null and undefined values', () => {
          const dataProvider: IDataProvider = { id: 123 };
          expectedResult = service.addDataProviderToCollectionIfMissing([], null, dataProvider, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dataProvider);
        });

        it('should return initial array if no DataProvider is added', () => {
          const dataProviderCollection: IDataProvider[] = [{ id: 123 }];
          expectedResult = service.addDataProviderToCollectionIfMissing(dataProviderCollection, undefined, null);
          expect(expectedResult).toEqual(dataProviderCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
