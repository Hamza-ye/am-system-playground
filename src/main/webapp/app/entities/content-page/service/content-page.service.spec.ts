import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContentPage, ContentPage } from '../content-page.model';

import { ContentPageService } from './content-page.service';

describe('Service Tests', () => {
  describe('ContentPage Service', () => {
    let service: ContentPageService;
    let httpMock: HttpTestingController;
    let elemDefault: IContentPage;
    let expectedResult: IContentPage | IContentPage[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContentPageService);
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
        subtitle: 'AAAAAAA',
        content: 'AAAAAAA',
        active: false,
        visitedCount: 0,
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

      it('should create a ContentPage', () => {
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

        service.create(new ContentPage()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContentPage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            subtitle: 'BBBBBB',
            content: 'BBBBBB',
            active: true,
            visitedCount: 1,
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

      it('should partial update a ContentPage', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            subtitle: 'BBBBBB',
          },
          new ContentPage()
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

      it('should return a list of ContentPage', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            title: 'BBBBBB',
            subtitle: 'BBBBBB',
            content: 'BBBBBB',
            active: true,
            visitedCount: 1,
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

      it('should delete a ContentPage', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContentPageToCollectionIfMissing', () => {
        it('should add a ContentPage to an empty array', () => {
          const contentPage: IContentPage = { id: 123 };
          expectedResult = service.addContentPageToCollectionIfMissing([], contentPage);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contentPage);
        });

        it('should not add a ContentPage to an array that contains it', () => {
          const contentPage: IContentPage = { id: 123 };
          const contentPageCollection: IContentPage[] = [
            {
              ...contentPage,
            },
            { id: 456 },
          ];
          expectedResult = service.addContentPageToCollectionIfMissing(contentPageCollection, contentPage);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContentPage to an array that doesn't contain it", () => {
          const contentPage: IContentPage = { id: 123 };
          const contentPageCollection: IContentPage[] = [{ id: 456 }];
          expectedResult = service.addContentPageToCollectionIfMissing(contentPageCollection, contentPage);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contentPage);
        });

        it('should add only unique ContentPage to an array', () => {
          const contentPageArray: IContentPage[] = [{ id: 123 }, { id: 456 }, { id: 15954 }];
          const contentPageCollection: IContentPage[] = [{ id: 123 }];
          expectedResult = service.addContentPageToCollectionIfMissing(contentPageCollection, ...contentPageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contentPage: IContentPage = { id: 123 };
          const contentPage2: IContentPage = { id: 456 };
          expectedResult = service.addContentPageToCollectionIfMissing([], contentPage, contentPage2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contentPage);
          expect(expectedResult).toContain(contentPage2);
        });

        it('should accept null and undefined values', () => {
          const contentPage: IContentPage = { id: 123 };
          expectedResult = service.addContentPageToCollectionIfMissing([], null, contentPage, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contentPage);
        });

        it('should return initial array if no ContentPage is added', () => {
          const contentPageCollection: IContentPage[] = [{ id: 123 }];
          expectedResult = service.addContentPageToCollectionIfMissing(contentPageCollection, undefined, null);
          expect(expectedResult).toEqual(contentPageCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
