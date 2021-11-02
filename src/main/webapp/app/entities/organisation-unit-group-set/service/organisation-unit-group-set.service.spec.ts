import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrganisationUnitGroupSet, OrganisationUnitGroupSet } from '../organisation-unit-group-set.model';

import { OrganisationUnitGroupSetService } from './organisation-unit-group-set.service';

describe('Service Tests', () => {
  describe('OrganisationUnitGroupSet Service', () => {
    let service: OrganisationUnitGroupSetService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganisationUnitGroupSet;
    let expectedResult: IOrganisationUnitGroupSet | IOrganisationUnitGroupSet[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganisationUnitGroupSetService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        compulsory: false,
        includeSubhierarchyInAnalytics: false,
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

      it('should create a OrganisationUnitGroupSet', () => {
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

        service.create(new OrganisationUnitGroupSet()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganisationUnitGroupSet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            compulsory: true,
            includeSubhierarchyInAnalytics: true,
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

      it('should partial update a OrganisationUnitGroupSet', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            compulsory: true,
          },
          new OrganisationUnitGroupSet()
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

      it('should return a list of OrganisationUnitGroupSet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            compulsory: true,
            includeSubhierarchyInAnalytics: true,
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

      it('should delete a OrganisationUnitGroupSet', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganisationUnitGroupSetToCollectionIfMissing', () => {
        it('should add a OrganisationUnitGroupSet to an empty array', () => {
          const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 123 };
          expectedResult = service.addOrganisationUnitGroupSetToCollectionIfMissing([], organisationUnitGroupSet);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnitGroupSet);
        });

        it('should not add a OrganisationUnitGroupSet to an array that contains it', () => {
          const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 123 };
          const organisationUnitGroupSetCollection: IOrganisationUnitGroupSet[] = [
            {
              ...organisationUnitGroupSet,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganisationUnitGroupSetToCollectionIfMissing(
            organisationUnitGroupSetCollection,
            organisationUnitGroupSet
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganisationUnitGroupSet to an array that doesn't contain it", () => {
          const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 123 };
          const organisationUnitGroupSetCollection: IOrganisationUnitGroupSet[] = [{ id: 456 }];
          expectedResult = service.addOrganisationUnitGroupSetToCollectionIfMissing(
            organisationUnitGroupSetCollection,
            organisationUnitGroupSet
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnitGroupSet);
        });

        it('should add only unique OrganisationUnitGroupSet to an array', () => {
          const organisationUnitGroupSetArray: IOrganisationUnitGroupSet[] = [{ id: 123 }, { id: 456 }, { id: 61775 }];
          const organisationUnitGroupSetCollection: IOrganisationUnitGroupSet[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitGroupSetToCollectionIfMissing(
            organisationUnitGroupSetCollection,
            ...organisationUnitGroupSetArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 123 };
          const organisationUnitGroupSet2: IOrganisationUnitGroupSet = { id: 456 };
          expectedResult = service.addOrganisationUnitGroupSetToCollectionIfMissing(
            [],
            organisationUnitGroupSet,
            organisationUnitGroupSet2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnitGroupSet);
          expect(expectedResult).toContain(organisationUnitGroupSet2);
        });

        it('should accept null and undefined values', () => {
          const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 123 };
          expectedResult = service.addOrganisationUnitGroupSetToCollectionIfMissing([], null, organisationUnitGroupSet, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnitGroupSet);
        });

        it('should return initial array if no OrganisationUnitGroupSet is added', () => {
          const organisationUnitGroupSetCollection: IOrganisationUnitGroupSet[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitGroupSetToCollectionIfMissing(organisationUnitGroupSetCollection, undefined, null);
          expect(expectedResult).toEqual(organisationUnitGroupSetCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
