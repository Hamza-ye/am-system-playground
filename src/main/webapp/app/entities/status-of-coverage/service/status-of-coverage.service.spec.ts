import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStatusOfCoverage, StatusOfCoverage } from '../status-of-coverage.model';

import { StatusOfCoverageService } from './status-of-coverage.service';

describe('Service Tests', () => {
  describe('StatusOfCoverage Service', () => {
    let service: StatusOfCoverageService;
    let httpMock: HttpTestingController;
    let elemDefault: IStatusOfCoverage;
    let expectedResult: IStatusOfCoverage | IStatusOfCoverage[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StatusOfCoverageService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        code: 'AAAAAAA',
        status: 'AAAAAAA',
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

      it('should create a StatusOfCoverage', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new StatusOfCoverage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StatusOfCoverage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a StatusOfCoverage', () => {
        const patchObject = Object.assign({}, new StatusOfCoverage());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of StatusOfCoverage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            status: 'BBBBBB',
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

      it('should delete a StatusOfCoverage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStatusOfCoverageToCollectionIfMissing', () => {
        it('should add a StatusOfCoverage to an empty array', () => {
          const statusOfCoverage: IStatusOfCoverage = { id: 123 };
          expectedResult = service.addStatusOfCoverageToCollectionIfMissing([], statusOfCoverage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(statusOfCoverage);
        });

        it('should not add a StatusOfCoverage to an array that contains it', () => {
          const statusOfCoverage: IStatusOfCoverage = { id: 123 };
          const statusOfCoverageCollection: IStatusOfCoverage[] = [
            {
              ...statusOfCoverage,
            },
            { id: 456 },
          ];
          expectedResult = service.addStatusOfCoverageToCollectionIfMissing(statusOfCoverageCollection, statusOfCoverage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StatusOfCoverage to an array that doesn't contain it", () => {
          const statusOfCoverage: IStatusOfCoverage = { id: 123 };
          const statusOfCoverageCollection: IStatusOfCoverage[] = [{ id: 456 }];
          expectedResult = service.addStatusOfCoverageToCollectionIfMissing(statusOfCoverageCollection, statusOfCoverage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(statusOfCoverage);
        });

        it('should add only unique StatusOfCoverage to an array', () => {
          const statusOfCoverageArray: IStatusOfCoverage[] = [{ id: 123 }, { id: 456 }, { id: 2059 }];
          const statusOfCoverageCollection: IStatusOfCoverage[] = [{ id: 123 }];
          expectedResult = service.addStatusOfCoverageToCollectionIfMissing(statusOfCoverageCollection, ...statusOfCoverageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const statusOfCoverage: IStatusOfCoverage = { id: 123 };
          const statusOfCoverage2: IStatusOfCoverage = { id: 456 };
          expectedResult = service.addStatusOfCoverageToCollectionIfMissing([], statusOfCoverage, statusOfCoverage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(statusOfCoverage);
          expect(expectedResult).toContain(statusOfCoverage2);
        });

        it('should accept null and undefined values', () => {
          const statusOfCoverage: IStatusOfCoverage = { id: 123 };
          expectedResult = service.addStatusOfCoverageToCollectionIfMissing([], null, statusOfCoverage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(statusOfCoverage);
        });

        it('should return initial array if no StatusOfCoverage is added', () => {
          const statusOfCoverageCollection: IStatusOfCoverage[] = [{ id: 123 }];
          expectedResult = service.addStatusOfCoverageToCollectionIfMissing(statusOfCoverageCollection, undefined, null);
          expect(expectedResult).toEqual(statusOfCoverageCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
