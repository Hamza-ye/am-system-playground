import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPeopleGroup, PeopleGroup } from '../people-group.model';

import { PeopleGroupService } from './people-group.service';

describe('Service Tests', () => {
  describe('PeopleGroup Service', () => {
    let service: PeopleGroupService;
    let httpMock: HttpTestingController;
    let elemDefault: IPeopleGroup;
    let expectedResult: IPeopleGroup | IPeopleGroup[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PeopleGroupService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        uuid: 'AAAAAAA',
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

      it('should create a PeopleGroup', () => {
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

        service.create(new PeopleGroup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PeopleGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            uuid: 'BBBBBB',
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

      it('should partial update a PeopleGroup', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new PeopleGroup()
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

      it('should return a list of PeopleGroup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            uuid: 'BBBBBB',
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

      it('should delete a PeopleGroup', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPeopleGroupToCollectionIfMissing', () => {
        it('should add a PeopleGroup to an empty array', () => {
          const peopleGroup: IPeopleGroup = { id: 123 };
          expectedResult = service.addPeopleGroupToCollectionIfMissing([], peopleGroup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(peopleGroup);
        });

        it('should not add a PeopleGroup to an array that contains it', () => {
          const peopleGroup: IPeopleGroup = { id: 123 };
          const peopleGroupCollection: IPeopleGroup[] = [
            {
              ...peopleGroup,
            },
            { id: 456 },
          ];
          expectedResult = service.addPeopleGroupToCollectionIfMissing(peopleGroupCollection, peopleGroup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PeopleGroup to an array that doesn't contain it", () => {
          const peopleGroup: IPeopleGroup = { id: 123 };
          const peopleGroupCollection: IPeopleGroup[] = [{ id: 456 }];
          expectedResult = service.addPeopleGroupToCollectionIfMissing(peopleGroupCollection, peopleGroup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(peopleGroup);
        });

        it('should add only unique PeopleGroup to an array', () => {
          const peopleGroupArray: IPeopleGroup[] = [{ id: 123 }, { id: 456 }, { id: 55866 }];
          const peopleGroupCollection: IPeopleGroup[] = [{ id: 123 }];
          expectedResult = service.addPeopleGroupToCollectionIfMissing(peopleGroupCollection, ...peopleGroupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const peopleGroup: IPeopleGroup = { id: 123 };
          const peopleGroup2: IPeopleGroup = { id: 456 };
          expectedResult = service.addPeopleGroupToCollectionIfMissing([], peopleGroup, peopleGroup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(peopleGroup);
          expect(expectedResult).toContain(peopleGroup2);
        });

        it('should accept null and undefined values', () => {
          const peopleGroup: IPeopleGroup = { id: 123 };
          expectedResult = service.addPeopleGroupToCollectionIfMissing([], null, peopleGroup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(peopleGroup);
        });

        it('should return initial array if no PeopleGroup is added', () => {
          const peopleGroupCollection: IPeopleGroup[] = [{ id: 123 }];
          expectedResult = service.addPeopleGroupToCollectionIfMissing(peopleGroupCollection, undefined, null);
          expect(expectedResult).toEqual(peopleGroupCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
