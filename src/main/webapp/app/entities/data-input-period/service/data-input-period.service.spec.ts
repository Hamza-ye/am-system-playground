import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDataInputPeriod, DataInputPeriod } from '../data-input-period.model';

import { DataInputPeriodService } from './data-input-period.service';

describe('Service Tests', () => {
  describe('DataInputPeriod Service', () => {
    let service: DataInputPeriodService;
    let httpMock: HttpTestingController;
    let elemDefault: IDataInputPeriod;
    let expectedResult: IDataInputPeriod | IDataInputPeriod[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DataInputPeriodService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        openingDate: currentDate,
        closingDate: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            openingDate: currentDate.format(DATE_FORMAT),
            closingDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DataInputPeriod', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            openingDate: currentDate.format(DATE_FORMAT),
            closingDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            openingDate: currentDate,
            closingDate: currentDate,
          },
          returnedFromService
        );

        service.create(new DataInputPeriod()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DataInputPeriod', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            openingDate: currentDate.format(DATE_FORMAT),
            closingDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            openingDate: currentDate,
            closingDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DataInputPeriod', () => {
        const patchObject = Object.assign(
          {
            openingDate: currentDate.format(DATE_FORMAT),
          },
          new DataInputPeriod()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            openingDate: currentDate,
            closingDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DataInputPeriod', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            openingDate: currentDate.format(DATE_FORMAT),
            closingDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            openingDate: currentDate,
            closingDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DataInputPeriod', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDataInputPeriodToCollectionIfMissing', () => {
        it('should add a DataInputPeriod to an empty array', () => {
          const dataInputPeriod: IDataInputPeriod = { id: 123 };
          expectedResult = service.addDataInputPeriodToCollectionIfMissing([], dataInputPeriod);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dataInputPeriod);
        });

        it('should not add a DataInputPeriod to an array that contains it', () => {
          const dataInputPeriod: IDataInputPeriod = { id: 123 };
          const dataInputPeriodCollection: IDataInputPeriod[] = [
            {
              ...dataInputPeriod,
            },
            { id: 456 },
          ];
          expectedResult = service.addDataInputPeriodToCollectionIfMissing(dataInputPeriodCollection, dataInputPeriod);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DataInputPeriod to an array that doesn't contain it", () => {
          const dataInputPeriod: IDataInputPeriod = { id: 123 };
          const dataInputPeriodCollection: IDataInputPeriod[] = [{ id: 456 }];
          expectedResult = service.addDataInputPeriodToCollectionIfMissing(dataInputPeriodCollection, dataInputPeriod);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dataInputPeriod);
        });

        it('should add only unique DataInputPeriod to an array', () => {
          const dataInputPeriodArray: IDataInputPeriod[] = [{ id: 123 }, { id: 456 }, { id: 43124 }];
          const dataInputPeriodCollection: IDataInputPeriod[] = [{ id: 123 }];
          expectedResult = service.addDataInputPeriodToCollectionIfMissing(dataInputPeriodCollection, ...dataInputPeriodArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dataInputPeriod: IDataInputPeriod = { id: 123 };
          const dataInputPeriod2: IDataInputPeriod = { id: 456 };
          expectedResult = service.addDataInputPeriodToCollectionIfMissing([], dataInputPeriod, dataInputPeriod2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dataInputPeriod);
          expect(expectedResult).toContain(dataInputPeriod2);
        });

        it('should accept null and undefined values', () => {
          const dataInputPeriod: IDataInputPeriod = { id: 123 };
          expectedResult = service.addDataInputPeriodToCollectionIfMissing([], null, dataInputPeriod, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dataInputPeriod);
        });

        it('should return initial array if no DataInputPeriod is added', () => {
          const dataInputPeriodCollection: IDataInputPeriod[] = [{ id: 123 }];
          expectedResult = service.addDataInputPeriodToCollectionIfMissing(dataInputPeriodCollection, undefined, null);
          expect(expectedResult).toEqual(dataInputPeriodCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
