import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICHV, CHV } from '../chv.model';

import { CHVService } from './chv.service';

describe('Service Tests', () => {
  describe('CHV Service', () => {
    let service: CHVService;
    let httpMock: HttpTestingController;
    let elemDefault: ICHV;
    let expectedResult: ICHV | ICHV[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CHVService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
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

      it('should create a CHV', () => {
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

        service.create(new CHV()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CHV', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
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

      it('should partial update a CHV', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          new CHV()
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

      it('should return a list of CHV', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
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

      it('should delete a CHV', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCHVToCollectionIfMissing', () => {
        it('should add a CHV to an empty array', () => {
          const cHV: ICHV = { id: 123 };
          expectedResult = service.addCHVToCollectionIfMissing([], cHV);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHV);
        });

        it('should not add a CHV to an array that contains it', () => {
          const cHV: ICHV = { id: 123 };
          const cHVCollection: ICHV[] = [
            {
              ...cHV,
            },
            { id: 456 },
          ];
          expectedResult = service.addCHVToCollectionIfMissing(cHVCollection, cHV);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CHV to an array that doesn't contain it", () => {
          const cHV: ICHV = { id: 123 };
          const cHVCollection: ICHV[] = [{ id: 456 }];
          expectedResult = service.addCHVToCollectionIfMissing(cHVCollection, cHV);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHV);
        });

        it('should add only unique CHV to an array', () => {
          const cHVArray: ICHV[] = [{ id: 123 }, { id: 456 }, { id: 50901 }];
          const cHVCollection: ICHV[] = [{ id: 123 }];
          expectedResult = service.addCHVToCollectionIfMissing(cHVCollection, ...cHVArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cHV: ICHV = { id: 123 };
          const cHV2: ICHV = { id: 456 };
          expectedResult = service.addCHVToCollectionIfMissing([], cHV, cHV2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHV);
          expect(expectedResult).toContain(cHV2);
        });

        it('should accept null and undefined values', () => {
          const cHV: ICHV = { id: 123 };
          expectedResult = service.addCHVToCollectionIfMissing([], null, cHV, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHV);
        });

        it('should return initial array if no CHV is added', () => {
          const cHVCollection: ICHV[] = [{ id: 123 }];
          expectedResult = service.addCHVToCollectionIfMissing(cHVCollection, undefined, null);
          expect(expectedResult).toEqual(cHVCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
