import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { MalariaUnitMemberType } from 'app/entities/enumerations/malaria-unit-member-type.model';
import { IMalariaUnitStaffMember, MalariaUnitStaffMember } from '../malaria-unit-staff-member.model';

import { MalariaUnitStaffMemberService } from './malaria-unit-staff-member.service';

describe('Service Tests', () => {
  describe('MalariaUnitStaffMember Service', () => {
    let service: MalariaUnitStaffMemberService;
    let httpMock: HttpTestingController;
    let elemDefault: IMalariaUnitStaffMember;
    let expectedResult: IMalariaUnitStaffMember | IMalariaUnitStaffMember[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MalariaUnitStaffMemberService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        description: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        memberNo: 0,
        memberType: MalariaUnitMemberType.ADMIN,
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

      it('should create a MalariaUnitStaffMember', () => {
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

        service.create(new MalariaUnitStaffMember()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MalariaUnitStaffMember', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            memberNo: 1,
            memberType: 'BBBBBB',
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

      it('should partial update a MalariaUnitStaffMember', () => {
        const patchObject = Object.assign(
          {
            code: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
          },
          new MalariaUnitStaffMember()
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

      it('should return a list of MalariaUnitStaffMember', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            memberNo: 1,
            memberType: 'BBBBBB',
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

      it('should delete a MalariaUnitStaffMember', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMalariaUnitStaffMemberToCollectionIfMissing', () => {
        it('should add a MalariaUnitStaffMember to an empty array', () => {
          const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 123 };
          expectedResult = service.addMalariaUnitStaffMemberToCollectionIfMissing([], malariaUnitStaffMember);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(malariaUnitStaffMember);
        });

        it('should not add a MalariaUnitStaffMember to an array that contains it', () => {
          const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 123 };
          const malariaUnitStaffMemberCollection: IMalariaUnitStaffMember[] = [
            {
              ...malariaUnitStaffMember,
            },
            { id: 456 },
          ];
          expectedResult = service.addMalariaUnitStaffMemberToCollectionIfMissing(malariaUnitStaffMemberCollection, malariaUnitStaffMember);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MalariaUnitStaffMember to an array that doesn't contain it", () => {
          const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 123 };
          const malariaUnitStaffMemberCollection: IMalariaUnitStaffMember[] = [{ id: 456 }];
          expectedResult = service.addMalariaUnitStaffMemberToCollectionIfMissing(malariaUnitStaffMemberCollection, malariaUnitStaffMember);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(malariaUnitStaffMember);
        });

        it('should add only unique MalariaUnitStaffMember to an array', () => {
          const malariaUnitStaffMemberArray: IMalariaUnitStaffMember[] = [{ id: 123 }, { id: 456 }, { id: 51781 }];
          const malariaUnitStaffMemberCollection: IMalariaUnitStaffMember[] = [{ id: 123 }];
          expectedResult = service.addMalariaUnitStaffMemberToCollectionIfMissing(
            malariaUnitStaffMemberCollection,
            ...malariaUnitStaffMemberArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 123 };
          const malariaUnitStaffMember2: IMalariaUnitStaffMember = { id: 456 };
          expectedResult = service.addMalariaUnitStaffMemberToCollectionIfMissing([], malariaUnitStaffMember, malariaUnitStaffMember2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(malariaUnitStaffMember);
          expect(expectedResult).toContain(malariaUnitStaffMember2);
        });

        it('should accept null and undefined values', () => {
          const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 123 };
          expectedResult = service.addMalariaUnitStaffMemberToCollectionIfMissing([], null, malariaUnitStaffMember, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(malariaUnitStaffMember);
        });

        it('should return initial array if no MalariaUnitStaffMember is added', () => {
          const malariaUnitStaffMemberCollection: IMalariaUnitStaffMember[] = [{ id: 123 }];
          expectedResult = service.addMalariaUnitStaffMemberToCollectionIfMissing(malariaUnitStaffMemberCollection, undefined, null);
          expect(expectedResult).toEqual(malariaUnitStaffMemberCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
