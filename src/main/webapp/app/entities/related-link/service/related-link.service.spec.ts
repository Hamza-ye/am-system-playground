import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRelatedLink, RelatedLink } from '../related-link.model';

import { RelatedLinkService } from './related-link.service';

describe('Service Tests', () => {
  describe('RelatedLink Service', () => {
    let service: RelatedLinkService;
    let httpMock: HttpTestingController;
    let elemDefault: IRelatedLink;
    let expectedResult: IRelatedLink | IRelatedLink[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RelatedLinkService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        url: 'AAAAAAA',
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

      it('should create a RelatedLink', () => {
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

        service.create(new RelatedLink()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RelatedLink', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            url: 'BBBBBB',
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

      it('should partial update a RelatedLink', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            url: 'BBBBBB',
          },
          new RelatedLink()
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

      it('should return a list of RelatedLink', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            url: 'BBBBBB',
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

      it('should delete a RelatedLink', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRelatedLinkToCollectionIfMissing', () => {
        it('should add a RelatedLink to an empty array', () => {
          const relatedLink: IRelatedLink = { id: 123 };
          expectedResult = service.addRelatedLinkToCollectionIfMissing([], relatedLink);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relatedLink);
        });

        it('should not add a RelatedLink to an array that contains it', () => {
          const relatedLink: IRelatedLink = { id: 123 };
          const relatedLinkCollection: IRelatedLink[] = [
            {
              ...relatedLink,
            },
            { id: 456 },
          ];
          expectedResult = service.addRelatedLinkToCollectionIfMissing(relatedLinkCollection, relatedLink);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RelatedLink to an array that doesn't contain it", () => {
          const relatedLink: IRelatedLink = { id: 123 };
          const relatedLinkCollection: IRelatedLink[] = [{ id: 456 }];
          expectedResult = service.addRelatedLinkToCollectionIfMissing(relatedLinkCollection, relatedLink);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relatedLink);
        });

        it('should add only unique RelatedLink to an array', () => {
          const relatedLinkArray: IRelatedLink[] = [{ id: 123 }, { id: 456 }, { id: 12111 }];
          const relatedLinkCollection: IRelatedLink[] = [{ id: 123 }];
          expectedResult = service.addRelatedLinkToCollectionIfMissing(relatedLinkCollection, ...relatedLinkArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const relatedLink: IRelatedLink = { id: 123 };
          const relatedLink2: IRelatedLink = { id: 456 };
          expectedResult = service.addRelatedLinkToCollectionIfMissing([], relatedLink, relatedLink2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relatedLink);
          expect(expectedResult).toContain(relatedLink2);
        });

        it('should accept null and undefined values', () => {
          const relatedLink: IRelatedLink = { id: 123 };
          expectedResult = service.addRelatedLinkToCollectionIfMissing([], null, relatedLink, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relatedLink);
        });

        it('should return initial array if no RelatedLink is added', () => {
          const relatedLinkCollection: IRelatedLink[] = [{ id: 123 }];
          expectedResult = service.addRelatedLinkToCollectionIfMissing(relatedLinkCollection, undefined, null);
          expect(expectedResult).toEqual(relatedLinkCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
