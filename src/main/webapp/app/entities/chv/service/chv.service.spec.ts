import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IChv, Chv } from '../chv.model';

import { ChvService } from './chv.service';

describe('Service Tests', () => {
  describe('Chv Service', () => {
    let service: ChvService;
    let httpMock: HttpTestingController;
    let elemDefault: IChv;
    let expectedResult: IChv | IChv[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChvService);
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

      it('should create a Chv', () => {
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

        service.create(new Chv()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Chv', () => {
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

      it('should partial update a Chv', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          new Chv()
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

      it('should return a list of Chv', () => {
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

      it('should delete a Chv', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChvToCollectionIfMissing', () => {
        it('should add a Chv to an empty array', () => {
          const chv: IChv = { id: 123 };
          expectedResult = service.addChvToCollectionIfMissing([], chv);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chv);
        });

        it('should not add a Chv to an array that contains it', () => {
          const chv: IChv = { id: 123 };
          const chvCollection: IChv[] = [
            {
              ...chv,
            },
            { id: 456 },
          ];
          expectedResult = service.addChvToCollectionIfMissing(chvCollection, chv);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Chv to an array that doesn't contain it", () => {
          const chv: IChv = { id: 123 };
          const chvCollection: IChv[] = [{ id: 456 }];
          expectedResult = service.addChvToCollectionIfMissing(chvCollection, chv);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chv);
        });

        it('should add only unique Chv to an array', () => {
          const chvArray: IChv[] = [{ id: 123 }, { id: 456 }, { id: 25780 }];
          const chvCollection: IChv[] = [{ id: 123 }];
          expectedResult = service.addChvToCollectionIfMissing(chvCollection, ...chvArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chv: IChv = { id: 123 };
          const chv2: IChv = { id: 456 };
          expectedResult = service.addChvToCollectionIfMissing([], chv, chv2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chv);
          expect(expectedResult).toContain(chv2);
        });

        it('should accept null and undefined values', () => {
          const chv: IChv = { id: 123 };
          expectedResult = service.addChvToCollectionIfMissing([], null, chv, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chv);
        });

        it('should return initial array if no Chv is added', () => {
          const chvCollection: IChv[] = [{ id: 123 }];
          expectedResult = service.addChvToCollectionIfMissing(chvCollection, undefined, null);
          expect(expectedResult).toEqual(chvCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
