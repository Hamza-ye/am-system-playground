import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFamily, Family } from '../family.model';

import { FamilyService } from './family.service';

describe('Service Tests', () => {
  describe('Family Service', () => {
    let service: FamilyService;
    let httpMock: HttpTestingController;
    let elemDefault: IFamily;
    let expectedResult: IFamily | IFamily[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FamilyService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        familyNo: 0,
        address: 'AAAAAAA',
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

      it('should create a Family', () => {
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

        service.create(new Family()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Family', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            familyNo: 1,
            address: 'BBBBBB',
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

      it('should partial update a Family', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            address: 'BBBBBB',
          },
          new Family()
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

      it('should return a list of Family', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            familyNo: 1,
            address: 'BBBBBB',
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

      it('should delete a Family', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFamilyToCollectionIfMissing', () => {
        it('should add a Family to an empty array', () => {
          const family: IFamily = { id: 123 };
          expectedResult = service.addFamilyToCollectionIfMissing([], family);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(family);
        });

        it('should not add a Family to an array that contains it', () => {
          const family: IFamily = { id: 123 };
          const familyCollection: IFamily[] = [
            {
              ...family,
            },
            { id: 456 },
          ];
          expectedResult = service.addFamilyToCollectionIfMissing(familyCollection, family);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Family to an array that doesn't contain it", () => {
          const family: IFamily = { id: 123 };
          const familyCollection: IFamily[] = [{ id: 456 }];
          expectedResult = service.addFamilyToCollectionIfMissing(familyCollection, family);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(family);
        });

        it('should add only unique Family to an array', () => {
          const familyArray: IFamily[] = [{ id: 123 }, { id: 456 }, { id: 65011 }];
          const familyCollection: IFamily[] = [{ id: 123 }];
          expectedResult = service.addFamilyToCollectionIfMissing(familyCollection, ...familyArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const family: IFamily = { id: 123 };
          const family2: IFamily = { id: 456 };
          expectedResult = service.addFamilyToCollectionIfMissing([], family, family2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(family);
          expect(expectedResult).toContain(family2);
        });

        it('should accept null and undefined values', () => {
          const family: IFamily = { id: 123 };
          expectedResult = service.addFamilyToCollectionIfMissing([], null, family, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(family);
        });

        it('should return initial array if no Family is added', () => {
          const familyCollection: IFamily[] = [{ id: 123 }];
          expectedResult = service.addFamilyToCollectionIfMissing(familyCollection, undefined, null);
          expect(expectedResult).toEqual(familyCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
