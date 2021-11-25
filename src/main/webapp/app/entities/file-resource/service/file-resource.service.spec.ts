import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { FileResourceDomain } from 'app/entities/enumerations/file-resource-domain.model';
import { IFileResource, FileResource } from '../file-resource.model';

import { FileResourceService } from './file-resource.service';

describe('Service Tests', () => {
  describe('FileResource Service', () => {
    let service: FileResourceService;
    let httpMock: HttpTestingController;
    let elemDefault: IFileResource;
    let expectedResult: IFileResource | IFileResource[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FileResourceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        contentType: 'AAAAAAA',
        contentLength: 'AAAAAAA',
        contentMd5: 'AAAAAAA',
        storageKey: 'AAAAAAA',
        assigned: false,
        domain: FileResourceDomain.DATA_VALUE,
        hasMultipleStorageFiles: false,
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

      it('should create a FileResource', () => {
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

        service.create(new FileResource()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FileResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            contentType: 'BBBBBB',
            contentLength: 'BBBBBB',
            contentMd5: 'BBBBBB',
            storageKey: 'BBBBBB',
            assigned: true,
            domain: 'BBBBBB',
            hasMultipleStorageFiles: true,
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

      it('should partial update a FileResource', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            contentLength: 'BBBBBB',
            contentMd5: 'BBBBBB',
            assigned: true,
            hasMultipleStorageFiles: true,
          },
          new FileResource()
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

      it('should return a list of FileResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            contentType: 'BBBBBB',
            contentLength: 'BBBBBB',
            contentMd5: 'BBBBBB',
            storageKey: 'BBBBBB',
            assigned: true,
            domain: 'BBBBBB',
            hasMultipleStorageFiles: true,
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

      it('should delete a FileResource', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFileResourceToCollectionIfMissing', () => {
        it('should add a FileResource to an empty array', () => {
          const fileResource: IFileResource = { id: 123 };
          expectedResult = service.addFileResourceToCollectionIfMissing([], fileResource);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fileResource);
        });

        it('should not add a FileResource to an array that contains it', () => {
          const fileResource: IFileResource = { id: 123 };
          const fileResourceCollection: IFileResource[] = [
            {
              ...fileResource,
            },
            { id: 456 },
          ];
          expectedResult = service.addFileResourceToCollectionIfMissing(fileResourceCollection, fileResource);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FileResource to an array that doesn't contain it", () => {
          const fileResource: IFileResource = { id: 123 };
          const fileResourceCollection: IFileResource[] = [{ id: 456 }];
          expectedResult = service.addFileResourceToCollectionIfMissing(fileResourceCollection, fileResource);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fileResource);
        });

        it('should add only unique FileResource to an array', () => {
          const fileResourceArray: IFileResource[] = [{ id: 123 }, { id: 456 }, { id: 81706 }];
          const fileResourceCollection: IFileResource[] = [{ id: 123 }];
          expectedResult = service.addFileResourceToCollectionIfMissing(fileResourceCollection, ...fileResourceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fileResource: IFileResource = { id: 123 };
          const fileResource2: IFileResource = { id: 456 };
          expectedResult = service.addFileResourceToCollectionIfMissing([], fileResource, fileResource2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fileResource);
          expect(expectedResult).toContain(fileResource2);
        });

        it('should accept null and undefined values', () => {
          const fileResource: IFileResource = { id: 123 };
          expectedResult = service.addFileResourceToCollectionIfMissing([], null, fileResource, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fileResource);
        });

        it('should return initial array if no FileResource is added', () => {
          const fileResourceCollection: IFileResource[] = [{ id: 123 }];
          expectedResult = service.addFileResourceToCollectionIfMissing(fileResourceCollection, undefined, null);
          expect(expectedResult).toEqual(fileResourceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
