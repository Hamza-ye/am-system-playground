import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPeriodType, PeriodType } from '../period-type.model';

import { PeriodTypeService } from './period-type.service';

describe('Service Tests', () => {
  describe('PeriodType Service', () => {
    let service: PeriodTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IPeriodType;
    let expectedResult: IPeriodType | IPeriodType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PeriodTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PeriodType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PeriodType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PeriodType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PeriodType', () => {
        const patchObject = Object.assign({}, new PeriodType());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PeriodType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PeriodType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPeriodTypeToCollectionIfMissing', () => {
        it('should add a PeriodType to an empty array', () => {
          const periodType: IPeriodType = { id: 123 };
          expectedResult = service.addPeriodTypeToCollectionIfMissing([], periodType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(periodType);
        });

        it('should not add a PeriodType to an array that contains it', () => {
          const periodType: IPeriodType = { id: 123 };
          const periodTypeCollection: IPeriodType[] = [
            {
              ...periodType,
            },
            { id: 456 },
          ];
          expectedResult = service.addPeriodTypeToCollectionIfMissing(periodTypeCollection, periodType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PeriodType to an array that doesn't contain it", () => {
          const periodType: IPeriodType = { id: 123 };
          const periodTypeCollection: IPeriodType[] = [{ id: 456 }];
          expectedResult = service.addPeriodTypeToCollectionIfMissing(periodTypeCollection, periodType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(periodType);
        });

        it('should add only unique PeriodType to an array', () => {
          const periodTypeArray: IPeriodType[] = [{ id: 123 }, { id: 456 }, { id: 59007 }];
          const periodTypeCollection: IPeriodType[] = [{ id: 123 }];
          expectedResult = service.addPeriodTypeToCollectionIfMissing(periodTypeCollection, ...periodTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const periodType: IPeriodType = { id: 123 };
          const periodType2: IPeriodType = { id: 456 };
          expectedResult = service.addPeriodTypeToCollectionIfMissing([], periodType, periodType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(periodType);
          expect(expectedResult).toContain(periodType2);
        });

        it('should accept null and undefined values', () => {
          const periodType: IPeriodType = { id: 123 };
          expectedResult = service.addPeriodTypeToCollectionIfMissing([], null, periodType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(periodType);
        });

        it('should return initial array if no PeriodType is added', () => {
          const periodTypeCollection: IPeriodType[] = [{ id: 123 }];
          expectedResult = service.addPeriodTypeToCollectionIfMissing(periodTypeCollection, undefined, null);
          expect(expectedResult).toEqual(periodTypeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
