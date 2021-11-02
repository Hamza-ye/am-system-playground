import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICasesReportClass, CasesReportClass } from '../cases-report-class.model';

import { CasesReportClassService } from './cases-report-class.service';

describe('Service Tests', () => {
  describe('CasesReportClass Service', () => {
    let service: CasesReportClassService;
    let httpMock: HttpTestingController;
    let elemDefault: ICasesReportClass;
    let expectedResult: ICasesReportClass | ICasesReportClass[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CasesReportClassService);
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

      it('should create a CasesReportClass', () => {
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

        service.create(new CasesReportClass()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CasesReportClass', () => {
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

      it('should partial update a CasesReportClass', () => {
        const patchObject = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new CasesReportClass()
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

      it('should return a list of CasesReportClass', () => {
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

      it('should delete a CasesReportClass', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCasesReportClassToCollectionIfMissing', () => {
        it('should add a CasesReportClass to an empty array', () => {
          const casesReportClass: ICasesReportClass = { id: 123 };
          expectedResult = service.addCasesReportClassToCollectionIfMissing([], casesReportClass);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(casesReportClass);
        });

        it('should not add a CasesReportClass to an array that contains it', () => {
          const casesReportClass: ICasesReportClass = { id: 123 };
          const casesReportClassCollection: ICasesReportClass[] = [
            {
              ...casesReportClass,
            },
            { id: 456 },
          ];
          expectedResult = service.addCasesReportClassToCollectionIfMissing(casesReportClassCollection, casesReportClass);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CasesReportClass to an array that doesn't contain it", () => {
          const casesReportClass: ICasesReportClass = { id: 123 };
          const casesReportClassCollection: ICasesReportClass[] = [{ id: 456 }];
          expectedResult = service.addCasesReportClassToCollectionIfMissing(casesReportClassCollection, casesReportClass);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(casesReportClass);
        });

        it('should add only unique CasesReportClass to an array', () => {
          const casesReportClassArray: ICasesReportClass[] = [{ id: 123 }, { id: 456 }, { id: 7697 }];
          const casesReportClassCollection: ICasesReportClass[] = [{ id: 123 }];
          expectedResult = service.addCasesReportClassToCollectionIfMissing(casesReportClassCollection, ...casesReportClassArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const casesReportClass: ICasesReportClass = { id: 123 };
          const casesReportClass2: ICasesReportClass = { id: 456 };
          expectedResult = service.addCasesReportClassToCollectionIfMissing([], casesReportClass, casesReportClass2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(casesReportClass);
          expect(expectedResult).toContain(casesReportClass2);
        });

        it('should accept null and undefined values', () => {
          const casesReportClass: ICasesReportClass = { id: 123 };
          expectedResult = service.addCasesReportClassToCollectionIfMissing([], null, casesReportClass, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(casesReportClass);
        });

        it('should return initial array if no CasesReportClass is added', () => {
          const casesReportClassCollection: ICasesReportClass[] = [{ id: 123 }];
          expectedResult = service.addCasesReportClassToCollectionIfMissing(casesReportClassCollection, undefined, null);
          expect(expectedResult).toEqual(casesReportClassCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
