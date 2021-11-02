import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFamilyHead, FamilyHead } from '../family-head.model';

import { FamilyHeadService } from './family-head.service';

describe('Service Tests', () => {
  describe('FamilyHead Service', () => {
    let service: FamilyHeadService;
    let httpMock: HttpTestingController;
    let elemDefault: IFamilyHead;
    let expectedResult: IFamilyHead | IFamilyHead[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FamilyHeadService);
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

      it('should create a FamilyHead', () => {
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

        service.create(new FamilyHead()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FamilyHead', () => {
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

      it('should partial update a FamilyHead', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new FamilyHead()
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

      it('should return a list of FamilyHead', () => {
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

      it('should delete a FamilyHead', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFamilyHeadToCollectionIfMissing', () => {
        it('should add a FamilyHead to an empty array', () => {
          const familyHead: IFamilyHead = { id: 123 };
          expectedResult = service.addFamilyHeadToCollectionIfMissing([], familyHead);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(familyHead);
        });

        it('should not add a FamilyHead to an array that contains it', () => {
          const familyHead: IFamilyHead = { id: 123 };
          const familyHeadCollection: IFamilyHead[] = [
            {
              ...familyHead,
            },
            { id: 456 },
          ];
          expectedResult = service.addFamilyHeadToCollectionIfMissing(familyHeadCollection, familyHead);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FamilyHead to an array that doesn't contain it", () => {
          const familyHead: IFamilyHead = { id: 123 };
          const familyHeadCollection: IFamilyHead[] = [{ id: 456 }];
          expectedResult = service.addFamilyHeadToCollectionIfMissing(familyHeadCollection, familyHead);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(familyHead);
        });

        it('should add only unique FamilyHead to an array', () => {
          const familyHeadArray: IFamilyHead[] = [{ id: 123 }, { id: 456 }, { id: 569 }];
          const familyHeadCollection: IFamilyHead[] = [{ id: 123 }];
          expectedResult = service.addFamilyHeadToCollectionIfMissing(familyHeadCollection, ...familyHeadArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const familyHead: IFamilyHead = { id: 123 };
          const familyHead2: IFamilyHead = { id: 456 };
          expectedResult = service.addFamilyHeadToCollectionIfMissing([], familyHead, familyHead2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(familyHead);
          expect(expectedResult).toContain(familyHead2);
        });

        it('should accept null and undefined values', () => {
          const familyHead: IFamilyHead = { id: 123 };
          expectedResult = service.addFamilyHeadToCollectionIfMissing([], null, familyHead, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(familyHead);
        });

        it('should return initial array if no FamilyHead is added', () => {
          const familyHeadCollection: IFamilyHead[] = [{ id: 123 }];
          expectedResult = service.addFamilyHeadToCollectionIfMissing(familyHeadCollection, undefined, null);
          expect(expectedResult).toEqual(familyHeadCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
