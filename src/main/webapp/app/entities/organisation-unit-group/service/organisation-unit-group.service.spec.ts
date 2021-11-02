import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrganisationUnitGroup, OrganisationUnitGroup } from '../organisation-unit-group.model';

import { OrganisationUnitGroupService } from './organisation-unit-group.service';

describe('Service Tests', () => {
  describe('OrganisationUnitGroup Service', () => {
    let service: OrganisationUnitGroupService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganisationUnitGroup;
    let expectedResult: IOrganisationUnitGroup | IOrganisationUnitGroup[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganisationUnitGroupService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        shortName: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        symbol: 'AAAAAAA',
        color: 'AAAAAAA',
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

      it('should create a OrganisationUnitGroup', () => {
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

        service.create(new OrganisationUnitGroup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganisationUnitGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            symbol: 'BBBBBB',
            color: 'BBBBBB',
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

      it('should partial update a OrganisationUnitGroup', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            symbol: 'BBBBBB',
          },
          new OrganisationUnitGroup()
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

      it('should return a list of OrganisationUnitGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            symbol: 'BBBBBB',
            color: 'BBBBBB',
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

      it('should delete a OrganisationUnitGroup', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganisationUnitGroupToCollectionIfMissing', () => {
        it('should add a OrganisationUnitGroup to an empty array', () => {
          const organisationUnitGroup: IOrganisationUnitGroup = { id: 123 };
          expectedResult = service.addOrganisationUnitGroupToCollectionIfMissing([], organisationUnitGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnitGroup);
        });

        it('should not add a OrganisationUnitGroup to an array that contains it', () => {
          const organisationUnitGroup: IOrganisationUnitGroup = { id: 123 };
          const organisationUnitGroupCollection: IOrganisationUnitGroup[] = [
            {
              ...organisationUnitGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganisationUnitGroupToCollectionIfMissing(organisationUnitGroupCollection, organisationUnitGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganisationUnitGroup to an array that doesn't contain it", () => {
          const organisationUnitGroup: IOrganisationUnitGroup = { id: 123 };
          const organisationUnitGroupCollection: IOrganisationUnitGroup[] = [{ id: 456 }];
          expectedResult = service.addOrganisationUnitGroupToCollectionIfMissing(organisationUnitGroupCollection, organisationUnitGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnitGroup);
        });

        it('should add only unique OrganisationUnitGroup to an array', () => {
          const organisationUnitGroupArray: IOrganisationUnitGroup[] = [{ id: 123 }, { id: 456 }, { id: 61119 }];
          const organisationUnitGroupCollection: IOrganisationUnitGroup[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitGroupToCollectionIfMissing(
            organisationUnitGroupCollection,
            ...organisationUnitGroupArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organisationUnitGroup: IOrganisationUnitGroup = { id: 123 };
          const organisationUnitGroup2: IOrganisationUnitGroup = { id: 456 };
          expectedResult = service.addOrganisationUnitGroupToCollectionIfMissing([], organisationUnitGroup, organisationUnitGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnitGroup);
          expect(expectedResult).toContain(organisationUnitGroup2);
        });

        it('should accept null and undefined values', () => {
          const organisationUnitGroup: IOrganisationUnitGroup = { id: 123 };
          expectedResult = service.addOrganisationUnitGroupToCollectionIfMissing([], null, organisationUnitGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnitGroup);
        });

        it('should return initial array if no OrganisationUnitGroup is added', () => {
          const organisationUnitGroupCollection: IOrganisationUnitGroup[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitGroupToCollectionIfMissing(organisationUnitGroupCollection, undefined, null);
          expect(expectedResult).toEqual(organisationUnitGroupCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
