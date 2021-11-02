import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPersonAuthorityGroup, PersonAuthorityGroup } from '../person-authority-group.model';

import { PersonAuthorityGroupService } from './person-authority-group.service';

describe('Service Tests', () => {
  describe('PersonAuthorityGroup Service', () => {
    let service: PersonAuthorityGroupService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersonAuthorityGroup;
    let expectedResult: IPersonAuthorityGroup | IPersonAuthorityGroup[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonAuthorityGroupService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
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

      it('should create a PersonAuthorityGroup', () => {
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

        service.create(new PersonAuthorityGroup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PersonAuthorityGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PersonAuthorityGroup', () => {
        const patchObject = Object.assign(
          {
            code: 'BBBBBB',
            name: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          new PersonAuthorityGroup()
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

      it('should return a list of PersonAuthorityGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PersonAuthorityGroup', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonAuthorityGroupToCollectionIfMissing', () => {
        it('should add a PersonAuthorityGroup to an empty array', () => {
          const personAuthorityGroup: IPersonAuthorityGroup = { id: 123 };
          expectedResult = service.addPersonAuthorityGroupToCollectionIfMissing([], personAuthorityGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personAuthorityGroup);
        });

        it('should not add a PersonAuthorityGroup to an array that contains it', () => {
          const personAuthorityGroup: IPersonAuthorityGroup = { id: 123 };
          const personAuthorityGroupCollection: IPersonAuthorityGroup[] = [
            {
              ...personAuthorityGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonAuthorityGroupToCollectionIfMissing(personAuthorityGroupCollection, personAuthorityGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PersonAuthorityGroup to an array that doesn't contain it", () => {
          const personAuthorityGroup: IPersonAuthorityGroup = { id: 123 };
          const personAuthorityGroupCollection: IPersonAuthorityGroup[] = [{ id: 456 }];
          expectedResult = service.addPersonAuthorityGroupToCollectionIfMissing(personAuthorityGroupCollection, personAuthorityGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personAuthorityGroup);
        });

        it('should add only unique PersonAuthorityGroup to an array', () => {
          const personAuthorityGroupArray: IPersonAuthorityGroup[] = [{ id: 123 }, { id: 456 }, { id: 66568 }];
          const personAuthorityGroupCollection: IPersonAuthorityGroup[] = [{ id: 123 }];
          expectedResult = service.addPersonAuthorityGroupToCollectionIfMissing(
            personAuthorityGroupCollection,
            ...personAuthorityGroupArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const personAuthorityGroup: IPersonAuthorityGroup = { id: 123 };
          const personAuthorityGroup2: IPersonAuthorityGroup = { id: 456 };
          expectedResult = service.addPersonAuthorityGroupToCollectionIfMissing([], personAuthorityGroup, personAuthorityGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(personAuthorityGroup);
          expect(expectedResult).toContain(personAuthorityGroup2);
        });

        it('should accept null and undefined values', () => {
          const personAuthorityGroup: IPersonAuthorityGroup = { id: 123 };
          expectedResult = service.addPersonAuthorityGroupToCollectionIfMissing([], null, personAuthorityGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(personAuthorityGroup);
        });

        it('should return initial array if no PersonAuthorityGroup is added', () => {
          const personAuthorityGroupCollection: IPersonAuthorityGroup[] = [{ id: 123 }];
          expectedResult = service.addPersonAuthorityGroupToCollectionIfMissing(personAuthorityGroupCollection, undefined, null);
          expect(expectedResult).toEqual(personAuthorityGroupCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
