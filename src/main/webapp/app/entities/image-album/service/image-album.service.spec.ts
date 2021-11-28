import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImageAlbum, ImageAlbum } from '../image-album.model';

import { ImageAlbumService } from './image-album.service';

describe('Service Tests', () => {
  describe('ImageAlbum Service', () => {
    let service: ImageAlbumService;
    let httpMock: HttpTestingController;
    let elemDefault: IImageAlbum;
    let expectedResult: IImageAlbum | IImageAlbum[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ImageAlbumService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        title: 'AAAAAAA',
        coverImageUid: 'AAAAAAA',
        subtitle: 'AAAAAAA',
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

      it('should create a ImageAlbum', () => {
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

        service.create(new ImageAlbum()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ImageAlbum', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            coverImageUid: 'BBBBBB',
            subtitle: 'BBBBBB',
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

      it('should partial update a ImageAlbum', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
          },
          new ImageAlbum()
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

      it('should return a list of ImageAlbum', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            coverImageUid: 'BBBBBB',
            subtitle: 'BBBBBB',
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

      it('should delete a ImageAlbum', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addImageAlbumToCollectionIfMissing', () => {
        it('should add a ImageAlbum to an empty array', () => {
          const imageAlbum: IImageAlbum = { id: 123 };
          expectedResult = service.addImageAlbumToCollectionIfMissing([], imageAlbum);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(imageAlbum);
        });

        it('should not add a ImageAlbum to an array that contains it', () => {
          const imageAlbum: IImageAlbum = { id: 123 };
          const imageAlbumCollection: IImageAlbum[] = [
            {
              ...imageAlbum,
            },
            { id: 456 },
          ];
          expectedResult = service.addImageAlbumToCollectionIfMissing(imageAlbumCollection, imageAlbum);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ImageAlbum to an array that doesn't contain it", () => {
          const imageAlbum: IImageAlbum = { id: 123 };
          const imageAlbumCollection: IImageAlbum[] = [{ id: 456 }];
          expectedResult = service.addImageAlbumToCollectionIfMissing(imageAlbumCollection, imageAlbum);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(imageAlbum);
        });

        it('should add only unique ImageAlbum to an array', () => {
          const imageAlbumArray: IImageAlbum[] = [{ id: 123 }, { id: 456 }, { id: 1313 }];
          const imageAlbumCollection: IImageAlbum[] = [{ id: 123 }];
          expectedResult = service.addImageAlbumToCollectionIfMissing(imageAlbumCollection, ...imageAlbumArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const imageAlbum: IImageAlbum = { id: 123 };
          const imageAlbum2: IImageAlbum = { id: 456 };
          expectedResult = service.addImageAlbumToCollectionIfMissing([], imageAlbum, imageAlbum2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(imageAlbum);
          expect(expectedResult).toContain(imageAlbum2);
        });

        it('should accept null and undefined values', () => {
          const imageAlbum: IImageAlbum = { id: 123 };
          expectedResult = service.addImageAlbumToCollectionIfMissing([], null, imageAlbum, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(imageAlbum);
        });

        it('should return initial array if no ImageAlbum is added', () => {
          const imageAlbumCollection: IImageAlbum[] = [{ id: 123 }];
          expectedResult = service.addImageAlbumToCollectionIfMissing(imageAlbumCollection, undefined, null);
          expect(expectedResult).toEqual(imageAlbumCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
