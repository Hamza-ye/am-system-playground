import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExternalFileResource, ExternalFileResource } from '../external-file-resource.model';

import { ExternalFileResourceService } from './external-file-resource.service';

describe('Service Tests', () => {
  describe('ExternalFileResource Service', () => {
    let service: ExternalFileResourceService;
    let httpMock: HttpTestingController;
    let elemDefault: IExternalFileResource;
    let expectedResult: IExternalFileResource | IExternalFileResource[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExternalFileResourceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        accessToken: 'AAAAAAA',
        expires: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            expires: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ExternalFileResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            expires: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            expires: currentDate,
          },
          returnedFromService
        );

        service.create(new ExternalFileResource()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExternalFileResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            accessToken: 'BBBBBB',
            expires: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            expires: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ExternalFileResource', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            accessToken: 'BBBBBB',
          },
          new ExternalFileResource()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            expires: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExternalFileResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            accessToken: 'BBBBBB',
            expires: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            expires: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ExternalFileResource', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExternalFileResourceToCollectionIfMissing', () => {
        it('should add a ExternalFileResource to an empty array', () => {
          const externalFileResource: IExternalFileResource = { id: 123 };
          expectedResult = service.addExternalFileResourceToCollectionIfMissing([], externalFileResource);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(externalFileResource);
        });

        it('should not add a ExternalFileResource to an array that contains it', () => {
          const externalFileResource: IExternalFileResource = { id: 123 };
          const externalFileResourceCollection: IExternalFileResource[] = [
            {
              ...externalFileResource,
            },
            { id: 456 },
          ];
          expectedResult = service.addExternalFileResourceToCollectionIfMissing(externalFileResourceCollection, externalFileResource);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExternalFileResource to an array that doesn't contain it", () => {
          const externalFileResource: IExternalFileResource = { id: 123 };
          const externalFileResourceCollection: IExternalFileResource[] = [{ id: 456 }];
          expectedResult = service.addExternalFileResourceToCollectionIfMissing(externalFileResourceCollection, externalFileResource);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(externalFileResource);
        });

        it('should add only unique ExternalFileResource to an array', () => {
          const externalFileResourceArray: IExternalFileResource[] = [{ id: 123 }, { id: 456 }, { id: 77781 }];
          const externalFileResourceCollection: IExternalFileResource[] = [{ id: 123 }];
          expectedResult = service.addExternalFileResourceToCollectionIfMissing(
            externalFileResourceCollection,
            ...externalFileResourceArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const externalFileResource: IExternalFileResource = { id: 123 };
          const externalFileResource2: IExternalFileResource = { id: 456 };
          expectedResult = service.addExternalFileResourceToCollectionIfMissing([], externalFileResource, externalFileResource2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(externalFileResource);
          expect(expectedResult).toContain(externalFileResource2);
        });

        it('should accept null and undefined values', () => {
          const externalFileResource: IExternalFileResource = { id: 123 };
          expectedResult = service.addExternalFileResourceToCollectionIfMissing([], null, externalFileResource, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(externalFileResource);
        });

        it('should return initial array if no ExternalFileResource is added', () => {
          const externalFileResourceCollection: IExternalFileResource[] = [{ id: 123 }];
          expectedResult = service.addExternalFileResourceToCollectionIfMissing(externalFileResourceCollection, undefined, null);
          expect(expectedResult).toEqual(externalFileResourceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
