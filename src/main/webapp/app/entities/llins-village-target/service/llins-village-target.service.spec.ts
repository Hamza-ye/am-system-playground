import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILLINSVillageTarget, LLINSVillageTarget } from '../llins-village-target.model';

import { LLINSVillageTargetService } from './llins-village-target.service';

describe('Service Tests', () => {
  describe('LLINSVillageTarget Service', () => {
    let service: LLINSVillageTargetService;
    let httpMock: HttpTestingController;
    let elemDefault: ILLINSVillageTarget;
    let expectedResult: ILLINSVillageTarget | ILLINSVillageTarget[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LLINSVillageTargetService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        residentsIndividuals: 0,
        idpsIndividuals: 0,
        residentsFamilies: 0,
        idpsFamilies: 0,
        noOfDaysNeeded: 0,
        quantity: 0,
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

      it('should create a LLINSVillageTarget', () => {
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

        service.create(new LLINSVillageTarget()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LLINSVillageTarget', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            residentsIndividuals: 1,
            idpsIndividuals: 1,
            residentsFamilies: 1,
            idpsFamilies: 1,
            noOfDaysNeeded: 1,
            quantity: 1,
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

      it('should partial update a LLINSVillageTarget', () => {
        const patchObject = Object.assign(
          {
            residentsIndividuals: 1,
            idpsIndividuals: 1,
            residentsFamilies: 1,
            noOfDaysNeeded: 1,
            quantity: 1,
          },
          new LLINSVillageTarget()
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

      it('should return a list of LLINSVillageTarget', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            residentsIndividuals: 1,
            idpsIndividuals: 1,
            residentsFamilies: 1,
            idpsFamilies: 1,
            noOfDaysNeeded: 1,
            quantity: 1,
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

      it('should delete a LLINSVillageTarget', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLLINSVillageTargetToCollectionIfMissing', () => {
        it('should add a LLINSVillageTarget to an empty array', () => {
          const lLINSVillageTarget: ILLINSVillageTarget = { id: 123 };
          expectedResult = service.addLLINSVillageTargetToCollectionIfMissing([], lLINSVillageTarget);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSVillageTarget);
        });

        it('should not add a LLINSVillageTarget to an array that contains it', () => {
          const lLINSVillageTarget: ILLINSVillageTarget = { id: 123 };
          const lLINSVillageTargetCollection: ILLINSVillageTarget[] = [
            {
              ...lLINSVillageTarget,
            },
            { id: 456 },
          ];
          expectedResult = service.addLLINSVillageTargetToCollectionIfMissing(lLINSVillageTargetCollection, lLINSVillageTarget);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LLINSVillageTarget to an array that doesn't contain it", () => {
          const lLINSVillageTarget: ILLINSVillageTarget = { id: 123 };
          const lLINSVillageTargetCollection: ILLINSVillageTarget[] = [{ id: 456 }];
          expectedResult = service.addLLINSVillageTargetToCollectionIfMissing(lLINSVillageTargetCollection, lLINSVillageTarget);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSVillageTarget);
        });

        it('should add only unique LLINSVillageTarget to an array', () => {
          const lLINSVillageTargetArray: ILLINSVillageTarget[] = [{ id: 123 }, { id: 456 }, { id: 76802 }];
          const lLINSVillageTargetCollection: ILLINSVillageTarget[] = [{ id: 123 }];
          expectedResult = service.addLLINSVillageTargetToCollectionIfMissing(lLINSVillageTargetCollection, ...lLINSVillageTargetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const lLINSVillageTarget: ILLINSVillageTarget = { id: 123 };
          const lLINSVillageTarget2: ILLINSVillageTarget = { id: 456 };
          expectedResult = service.addLLINSVillageTargetToCollectionIfMissing([], lLINSVillageTarget, lLINSVillageTarget2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(lLINSVillageTarget);
          expect(expectedResult).toContain(lLINSVillageTarget2);
        });

        it('should accept null and undefined values', () => {
          const lLINSVillageTarget: ILLINSVillageTarget = { id: 123 };
          expectedResult = service.addLLINSVillageTargetToCollectionIfMissing([], null, lLINSVillageTarget, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(lLINSVillageTarget);
        });

        it('should return initial array if no LLINSVillageTarget is added', () => {
          const lLINSVillageTargetCollection: ILLINSVillageTarget[] = [{ id: 123 }];
          expectedResult = service.addLLINSVillageTargetToCollectionIfMissing(lLINSVillageTargetCollection, undefined, null);
          expect(expectedResult).toEqual(lLINSVillageTargetCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
