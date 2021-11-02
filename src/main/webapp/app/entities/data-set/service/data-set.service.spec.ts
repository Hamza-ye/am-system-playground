import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDataSet, DataSet } from '../data-set.model';

import { DataSetService } from './data-set.service';

describe('Service Tests', () => {
  describe('DataSet Service', () => {
    let service: DataSetService;
    let httpMock: HttpTestingController;
    let elemDefault: IDataSet;
    let expectedResult: IDataSet | IDataSet[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DataSetService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        shortName: 'AAAAAAA',
        description: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        expiryDays: 0,
        timelyDays: 0,
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

      it('should create a DataSet', () => {
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

        service.create(new DataSet()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DataSet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            expiryDays: 1,
            timelyDays: 1,
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

      it('should partial update a DataSet', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            timelyDays: 1,
          },
          new DataSet()
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

      it('should return a list of DataSet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            expiryDays: 1,
            timelyDays: 1,
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

      it('should delete a DataSet', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDataSetToCollectionIfMissing', () => {
        it('should add a DataSet to an empty array', () => {
          const dataSet: IDataSet = { id: 123 };
          expectedResult = service.addDataSetToCollectionIfMissing([], dataSet);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dataSet);
        });

        it('should not add a DataSet to an array that contains it', () => {
          const dataSet: IDataSet = { id: 123 };
          const dataSetCollection: IDataSet[] = [
            {
              ...dataSet,
            },
            { id: 456 },
          ];
          expectedResult = service.addDataSetToCollectionIfMissing(dataSetCollection, dataSet);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DataSet to an array that doesn't contain it", () => {
          const dataSet: IDataSet = { id: 123 };
          const dataSetCollection: IDataSet[] = [{ id: 456 }];
          expectedResult = service.addDataSetToCollectionIfMissing(dataSetCollection, dataSet);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dataSet);
        });

        it('should add only unique DataSet to an array', () => {
          const dataSetArray: IDataSet[] = [{ id: 123 }, { id: 456 }, { id: 70017 }];
          const dataSetCollection: IDataSet[] = [{ id: 123 }];
          expectedResult = service.addDataSetToCollectionIfMissing(dataSetCollection, ...dataSetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dataSet: IDataSet = { id: 123 };
          const dataSet2: IDataSet = { id: 456 };
          expectedResult = service.addDataSetToCollectionIfMissing([], dataSet, dataSet2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dataSet);
          expect(expectedResult).toContain(dataSet2);
        });

        it('should accept null and undefined values', () => {
          const dataSet: IDataSet = { id: 123 };
          expectedResult = service.addDataSetToCollectionIfMissing([], null, dataSet, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dataSet);
        });

        it('should return initial array if no DataSet is added', () => {
          const dataSetCollection: IDataSet[] = [{ id: 123 }];
          expectedResult = service.addDataSetToCollectionIfMissing(dataSetCollection, undefined, null);
          expect(expectedResult).toEqual(dataSetCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
