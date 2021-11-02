import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMalariaUnit, MalariaUnit } from '../malaria-unit.model';

import { MalariaUnitService } from './malaria-unit.service';

describe('Service Tests', () => {
  describe('MalariaUnit Service', () => {
    let service: MalariaUnitService;
    let httpMock: HttpTestingController;
    let elemDefault: IMalariaUnit;
    let expectedResult: IMalariaUnit | IMalariaUnit[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MalariaUnitService);
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

      it('should create a MalariaUnit', () => {
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

        service.create(new MalariaUnit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MalariaUnit', () => {
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

      it('should partial update a MalariaUnit', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new MalariaUnit()
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

      it('should return a list of MalariaUnit', () => {
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

      it('should delete a MalariaUnit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMalariaUnitToCollectionIfMissing', () => {
        it('should add a MalariaUnit to an empty array', () => {
          const malariaUnit: IMalariaUnit = { id: 123 };
          expectedResult = service.addMalariaUnitToCollectionIfMissing([], malariaUnit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(malariaUnit);
        });

        it('should not add a MalariaUnit to an array that contains it', () => {
          const malariaUnit: IMalariaUnit = { id: 123 };
          const malariaUnitCollection: IMalariaUnit[] = [
            {
              ...malariaUnit,
            },
            { id: 456 },
          ];
          expectedResult = service.addMalariaUnitToCollectionIfMissing(malariaUnitCollection, malariaUnit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MalariaUnit to an array that doesn't contain it", () => {
          const malariaUnit: IMalariaUnit = { id: 123 };
          const malariaUnitCollection: IMalariaUnit[] = [{ id: 456 }];
          expectedResult = service.addMalariaUnitToCollectionIfMissing(malariaUnitCollection, malariaUnit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(malariaUnit);
        });

        it('should add only unique MalariaUnit to an array', () => {
          const malariaUnitArray: IMalariaUnit[] = [{ id: 123 }, { id: 456 }, { id: 97691 }];
          const malariaUnitCollection: IMalariaUnit[] = [{ id: 123 }];
          expectedResult = service.addMalariaUnitToCollectionIfMissing(malariaUnitCollection, ...malariaUnitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const malariaUnit: IMalariaUnit = { id: 123 };
          const malariaUnit2: IMalariaUnit = { id: 456 };
          expectedResult = service.addMalariaUnitToCollectionIfMissing([], malariaUnit, malariaUnit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(malariaUnit);
          expect(expectedResult).toContain(malariaUnit2);
        });

        it('should accept null and undefined values', () => {
          const malariaUnit: IMalariaUnit = { id: 123 };
          expectedResult = service.addMalariaUnitToCollectionIfMissing([], null, malariaUnit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(malariaUnit);
        });

        it('should return initial array if no MalariaUnit is added', () => {
          const malariaUnitCollection: IMalariaUnit[] = [{ id: 123 }];
          expectedResult = service.addMalariaUnitToCollectionIfMissing(malariaUnitCollection, undefined, null);
          expect(expectedResult).toEqual(malariaUnitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
