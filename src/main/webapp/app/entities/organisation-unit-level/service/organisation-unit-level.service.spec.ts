import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrganisationUnitLevel, OrganisationUnitLevel } from '../organisation-unit-level.model';

import { OrganisationUnitLevelService } from './organisation-unit-level.service';

describe('Service Tests', () => {
  describe('OrganisationUnitLevel Service', () => {
    let service: OrganisationUnitLevelService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrganisationUnitLevel;
    let expectedResult: IOrganisationUnitLevel | IOrganisationUnitLevel[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrganisationUnitLevelService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        level: 0,
        offlineLevels: 0,
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

      it('should create a OrganisationUnitLevel', () => {
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

        service.create(new OrganisationUnitLevel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OrganisationUnitLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            level: 1,
            offlineLevels: 1,
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

      it('should partial update a OrganisationUnitLevel', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            name: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            level: 1,
          },
          new OrganisationUnitLevel()
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

      it('should return a list of OrganisationUnitLevel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            level: 1,
            offlineLevels: 1,
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

      it('should delete a OrganisationUnitLevel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrganisationUnitLevelToCollectionIfMissing', () => {
        it('should add a OrganisationUnitLevel to an empty array', () => {
          const organisationUnitLevel: IOrganisationUnitLevel = { id: 123 };
          expectedResult = service.addOrganisationUnitLevelToCollectionIfMissing([], organisationUnitLevel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnitLevel);
        });

        it('should not add a OrganisationUnitLevel to an array that contains it', () => {
          const organisationUnitLevel: IOrganisationUnitLevel = { id: 123 };
          const organisationUnitLevelCollection: IOrganisationUnitLevel[] = [
            {
              ...organisationUnitLevel,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrganisationUnitLevelToCollectionIfMissing(organisationUnitLevelCollection, organisationUnitLevel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OrganisationUnitLevel to an array that doesn't contain it", () => {
          const organisationUnitLevel: IOrganisationUnitLevel = { id: 123 };
          const organisationUnitLevelCollection: IOrganisationUnitLevel[] = [{ id: 456 }];
          expectedResult = service.addOrganisationUnitLevelToCollectionIfMissing(organisationUnitLevelCollection, organisationUnitLevel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnitLevel);
        });

        it('should add only unique OrganisationUnitLevel to an array', () => {
          const organisationUnitLevelArray: IOrganisationUnitLevel[] = [{ id: 123 }, { id: 456 }, { id: 43816 }];
          const organisationUnitLevelCollection: IOrganisationUnitLevel[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitLevelToCollectionIfMissing(
            organisationUnitLevelCollection,
            ...organisationUnitLevelArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const organisationUnitLevel: IOrganisationUnitLevel = { id: 123 };
          const organisationUnitLevel2: IOrganisationUnitLevel = { id: 456 };
          expectedResult = service.addOrganisationUnitLevelToCollectionIfMissing([], organisationUnitLevel, organisationUnitLevel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(organisationUnitLevel);
          expect(expectedResult).toContain(organisationUnitLevel2);
        });

        it('should accept null and undefined values', () => {
          const organisationUnitLevel: IOrganisationUnitLevel = { id: 123 };
          expectedResult = service.addOrganisationUnitLevelToCollectionIfMissing([], null, organisationUnitLevel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(organisationUnitLevel);
        });

        it('should return initial array if no OrganisationUnitLevel is added', () => {
          const organisationUnitLevelCollection: IOrganisationUnitLevel[] = [{ id: 123 }];
          expectedResult = service.addOrganisationUnitLevelToCollectionIfMissing(organisationUnitLevelCollection, undefined, null);
          expect(expectedResult).toEqual(organisationUnitLevelCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
