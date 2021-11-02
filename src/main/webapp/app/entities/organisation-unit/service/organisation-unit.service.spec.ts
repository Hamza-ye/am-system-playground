import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { OrganisationUnitType } from 'app/entities/enumerations/organisation-unit-type.model';
import { IOrganisationUnit, OrganisationUnit } from '../organisation-unit.model';

import { OrganisationUnitService } from './organisation-unit.service';

describe('Service Tests', () => {
  describe('OrganisationUnit Service', () => {
    let service: OrganisationUnitService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganisationUnit;
    let expectedResult: IOrganisationUnit | IOrganisationUnit[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganisationUnitService);
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
        path: 'AAAAAAA',
        hierarchyLevel: 0,
        openingDate: currentDate,
        comment: 'AAAAAAA',
        closedDate: currentDate,
        url: 'AAAAAAA',
        contactPerson: 'AAAAAAA',
        address: 'AAAAAAA',
        email: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        organisationUnitType: OrganisationUnitType.COUNTRY,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            openingDate: currentDate.format(DATE_FORMAT),
            closedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a OrganisationUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            openingDate: currentDate.format(DATE_FORMAT),
            closedDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            openingDate: currentDate,
            closedDate: currentDate,
          },
          returnedFromService
        );

        service.create(new OrganisationUnit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganisationUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            path: 'BBBBBB',
            hierarchyLevel: 1,
            openingDate: currentDate.format(DATE_FORMAT),
            comment: 'BBBBBB',
            closedDate: currentDate.format(DATE_FORMAT),
            url: 'BBBBBB',
            contactPerson: 'BBBBBB',
            address: 'BBBBBB',
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            organisationUnitType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            openingDate: currentDate,
            closedDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OrganisationUnit', () => {
        const patchObject = Object.assign(
          {
            code: 'BBBBBB',
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            comment: 'BBBBBB',
            contactPerson: 'BBBBBB',
            email: 'BBBBBB',
            organisationUnitType: 'BBBBBB',
          },
          new OrganisationUnit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            openingDate: currentDate,
            closedDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OrganisationUnit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            path: 'BBBBBB',
            hierarchyLevel: 1,
            openingDate: currentDate.format(DATE_FORMAT),
            comment: 'BBBBBB',
            closedDate: currentDate.format(DATE_FORMAT),
            url: 'BBBBBB',
            contactPerson: 'BBBBBB',
            address: 'BBBBBB',
            email: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            organisationUnitType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            openingDate: currentDate,
            closedDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a OrganisationUnit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganisationUnitToCollectionIfMissing', () => {
        it('should add a OrganisationUnit to an empty array', () => {
          const organisationUnit: IOrganisationUnit = { id: 123 };
          expectedResult = service.addOrganisationUnitToCollectionIfMissing([], organisationUnit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnit);
        });

        it('should not add a OrganisationUnit to an array that contains it', () => {
          const organisationUnit: IOrganisationUnit = { id: 123 };
          const organisationUnitCollection: IOrganisationUnit[] = [
            {
              ...organisationUnit,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganisationUnitToCollectionIfMissing(organisationUnitCollection, organisationUnit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganisationUnit to an array that doesn't contain it", () => {
          const organisationUnit: IOrganisationUnit = { id: 123 };
          const organisationUnitCollection: IOrganisationUnit[] = [{ id: 456 }];
          expectedResult = service.addOrganisationUnitToCollectionIfMissing(organisationUnitCollection, organisationUnit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnit);
        });

        it('should add only unique OrganisationUnit to an array', () => {
          const organisationUnitArray: IOrganisationUnit[] = [{ id: 123 }, { id: 456 }, { id: 54592 }];
          const organisationUnitCollection: IOrganisationUnit[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitToCollectionIfMissing(organisationUnitCollection, ...organisationUnitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organisationUnit: IOrganisationUnit = { id: 123 };
          const organisationUnit2: IOrganisationUnit = { id: 456 };
          expectedResult = service.addOrganisationUnitToCollectionIfMissing([], organisationUnit, organisationUnit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnit);
          expect(expectedResult).toContain(organisationUnit2);
        });

        it('should accept null and undefined values', () => {
          const organisationUnit: IOrganisationUnit = { id: 123 };
          expectedResult = service.addOrganisationUnitToCollectionIfMissing([], null, organisationUnit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnit);
        });

        it('should return initial array if no OrganisationUnit is added', () => {
          const organisationUnitCollection: IOrganisationUnit[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitToCollectionIfMissing(organisationUnitCollection, undefined, null);
          expect(expectedResult).toEqual(organisationUnitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
