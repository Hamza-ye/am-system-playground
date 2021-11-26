import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { FamilyType } from 'app/entities/enumerations/family-type.model';
import { StatusOfFamilyTarget } from 'app/entities/enumerations/status-of-family-target.model';
import { ILlinsFamilyTarget, LlinsFamilyTarget } from '../llins-family-target.model';

import { LlinsFamilyTargetService } from './llins-family-target.service';

describe('Service Tests', () => {
  describe('LlinsFamilyTarget Service', () => {
    let service: LlinsFamilyTargetService;
    let httpMock: HttpTestingController;
    let elemDefault: ILlinsFamilyTarget;
    let expectedResult: ILlinsFamilyTarget | ILlinsFamilyTarget[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LlinsFamilyTargetService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        residentsIndividualsPlanned: 0,
        idpsIndividualsPlanned: 0,
        quantityPlanned: 0,
        familyType: FamilyType.RESIDENT,
        statusOfFamilyTarget: StatusOfFamilyTarget.REACHED,
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

      it('should create a LlinsFamilyTarget', () => {
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

        service.create(new LlinsFamilyTarget()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LlinsFamilyTarget', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            residentsIndividualsPlanned: 1,
            idpsIndividualsPlanned: 1,
            quantityPlanned: 1,
            familyType: 'BBBBBB',
            statusOfFamilyTarget: 'BBBBBB',
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

      it('should partial update a LlinsFamilyTarget', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            idpsIndividualsPlanned: 1,
            familyType: 'BBBBBB',
          },
          new LlinsFamilyTarget()
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

      it('should return a list of LlinsFamilyTarget', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            residentsIndividualsPlanned: 1,
            idpsIndividualsPlanned: 1,
            quantityPlanned: 1,
            familyType: 'BBBBBB',
            statusOfFamilyTarget: 'BBBBBB',
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

      it('should delete a LlinsFamilyTarget', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLlinsFamilyTargetToCollectionIfMissing', () => {
        it('should add a LlinsFamilyTarget to an empty array', () => {
          const llinsFamilyTarget: ILlinsFamilyTarget = { id: 123 };
          expectedResult = service.addLlinsFamilyTargetToCollectionIfMissing([], llinsFamilyTarget);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsFamilyTarget);
        });

        it('should not add a LlinsFamilyTarget to an array that contains it', () => {
          const llinsFamilyTarget: ILlinsFamilyTarget = { id: 123 };
          const llinsFamilyTargetCollection: ILlinsFamilyTarget[] = [
            {
              ...llinsFamilyTarget,
            },
            { id: 456 },
          ];
          expectedResult = service.addLlinsFamilyTargetToCollectionIfMissing(llinsFamilyTargetCollection, llinsFamilyTarget);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsFamilyTarget to an array that doesn't contain it", () => {
          const llinsFamilyTarget: ILlinsFamilyTarget = { id: 123 };
          const llinsFamilyTargetCollection: ILlinsFamilyTarget[] = [{ id: 456 }];
          expectedResult = service.addLlinsFamilyTargetToCollectionIfMissing(llinsFamilyTargetCollection, llinsFamilyTarget);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsFamilyTarget);
        });

        it('should add only unique LlinsFamilyTarget to an array', () => {
          const llinsFamilyTargetArray: ILlinsFamilyTarget[] = [{ id: 123 }, { id: 456 }, { id: 30876 }];
          const llinsFamilyTargetCollection: ILlinsFamilyTarget[] = [{ id: 123 }];
          expectedResult = service.addLlinsFamilyTargetToCollectionIfMissing(llinsFamilyTargetCollection, ...llinsFamilyTargetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const llinsFamilyTarget: ILlinsFamilyTarget = { id: 123 };
          const llinsFamilyTarget2: ILlinsFamilyTarget = { id: 456 };
          expectedResult = service.addLlinsFamilyTargetToCollectionIfMissing([], llinsFamilyTarget, llinsFamilyTarget2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsFamilyTarget);
          expect(expectedResult).toContain(llinsFamilyTarget2);
        });

        it('should accept null and undefined values', () => {
          const llinsFamilyTarget: ILlinsFamilyTarget = { id: 123 };
          expectedResult = service.addLlinsFamilyTargetToCollectionIfMissing([], null, llinsFamilyTarget, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsFamilyTarget);
        });

        it('should return initial array if no LlinsFamilyTarget is added', () => {
          const llinsFamilyTargetCollection: ILlinsFamilyTarget[] = [{ id: 123 }];
          expectedResult = service.addLlinsFamilyTargetToCollectionIfMissing(llinsFamilyTargetCollection, undefined, null);
          expect(expectedResult).toEqual(llinsFamilyTargetCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
