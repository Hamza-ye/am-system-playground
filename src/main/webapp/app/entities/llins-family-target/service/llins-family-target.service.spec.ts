import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { FamilyType } from 'app/entities/enumerations/family-type.model';
import { StatusOfFamilyTarget } from 'app/entities/enumerations/status-of-family-target.model';
import { ILLINSFamilyTarget, LLINSFamilyTarget } from '../llins-family-target.model';

import { LLINSFamilyTargetService } from './llins-family-target.service';

describe('Service Tests', () => {
  describe('LLINSFamilyTarget Service', () => {
    let service: LLINSFamilyTargetService;
    let httpMock: HttpTestingController;
    let elemDefault: ILLINSFamilyTarget;
    let expectedResult: ILLINSFamilyTarget | ILLINSFamilyTarget[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LLINSFamilyTargetService);
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

      it('should create a LLINSFamilyTarget', () => {
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

        service.create(new LLINSFamilyTarget()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LLINSFamilyTarget', () => {
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

      it('should partial update a LLINSFamilyTarget', () => {
        const patchObject = Object.assign(
          {
            residentsIndividualsPlanned: 1,
            idpsIndividualsPlanned: 1,
            familyType: 'BBBBBB',
          },
          new LLINSFamilyTarget()
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

      it('should return a list of LLINSFamilyTarget', () => {
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

      it('should delete a LLINSFamilyTarget', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLLINSFamilyTargetToCollectionIfMissing', () => {
        it('should add a LLINSFamilyTarget to an empty array', () => {
          const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 123 };
          expectedResult = service.addLLINSFamilyTargetToCollectionIfMissing([], lLINSFamilyTarget);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSFamilyTarget);
        });

        it('should not add a LLINSFamilyTarget to an array that contains it', () => {
          const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 123 };
          const lLINSFamilyTargetCollection: ILLINSFamilyTarget[] = [
            {
              ...lLINSFamilyTarget,
            },
            { id: 456 },
          ];
          expectedResult = service.addLLINSFamilyTargetToCollectionIfMissing(lLINSFamilyTargetCollection, lLINSFamilyTarget);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LLINSFamilyTarget to an array that doesn't contain it", () => {
          const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 123 };
          const lLINSFamilyTargetCollection: ILLINSFamilyTarget[] = [{ id: 456 }];
          expectedResult = service.addLLINSFamilyTargetToCollectionIfMissing(lLINSFamilyTargetCollection, lLINSFamilyTarget);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSFamilyTarget);
        });

        it('should add only unique LLINSFamilyTarget to an array', () => {
          const lLINSFamilyTargetArray: ILLINSFamilyTarget[] = [{ id: 123 }, { id: 456 }, { id: 32760 }];
          const lLINSFamilyTargetCollection: ILLINSFamilyTarget[] = [{ id: 123 }];
          expectedResult = service.addLLINSFamilyTargetToCollectionIfMissing(lLINSFamilyTargetCollection, ...lLINSFamilyTargetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 123 };
          const lLINSFamilyTarget2: ILLINSFamilyTarget = { id: 456 };
          expectedResult = service.addLLINSFamilyTargetToCollectionIfMissing([], lLINSFamilyTarget, lLINSFamilyTarget2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSFamilyTarget);
          expect(expectedResult).toContain(lLINSFamilyTarget2);
        });

        it('should accept null and undefined values', () => {
          const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 123 };
          expectedResult = service.addLLINSFamilyTargetToCollectionIfMissing([], null, lLINSFamilyTarget, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSFamilyTarget);
        });

        it('should return initial array if no LLINSFamilyTarget is added', () => {
          const lLINSFamilyTargetCollection: ILLINSFamilyTarget[] = [{ id: 123 }];
          expectedResult = service.addLLINSFamilyTargetToCollectionIfMissing(lLINSFamilyTargetCollection, undefined, null);
          expect(expectedResult).toEqual(lLINSFamilyTargetCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
