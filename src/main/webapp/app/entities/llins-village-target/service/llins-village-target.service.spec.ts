import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILlinsVillageTarget, LlinsVillageTarget } from '../llins-village-target.model';

import { LlinsVillageTargetService } from './llins-village-target.service';

describe('Service Tests', () => {
  describe('LlinsVillageTarget Service', () => {
    let service: LlinsVillageTargetService;
    let httpMock: HttpTestingController;
    let elemDefault: ILlinsVillageTarget;
    let expectedResult: ILlinsVillageTarget | ILlinsVillageTarget[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LlinsVillageTargetService);
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

      it('should create a LlinsVillageTarget', () => {
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

        service.create(new LlinsVillageTarget()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LlinsVillageTarget', () => {
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

      it('should partial update a LlinsVillageTarget', () => {
        const patchObject = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            residentsIndividuals: 1,
            residentsFamilies: 1,
          },
          new LlinsVillageTarget()
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

      it('should return a list of LlinsVillageTarget', () => {
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

      it('should delete a LlinsVillageTarget', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLlinsVillageTargetToCollectionIfMissing', () => {
        it('should add a LlinsVillageTarget to an empty array', () => {
          const llinsVillageTarget: ILlinsVillageTarget = { id: 123 };
          expectedResult = service.addLlinsVillageTargetToCollectionIfMissing([], llinsVillageTarget);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsVillageTarget);
        });

        it('should not add a LlinsVillageTarget to an array that contains it', () => {
          const llinsVillageTarget: ILlinsVillageTarget = { id: 123 };
          const llinsVillageTargetCollection: ILlinsVillageTarget[] = [
            {
              ...llinsVillageTarget,
            },
            { id: 456 },
          ];
          expectedResult = service.addLlinsVillageTargetToCollectionIfMissing(llinsVillageTargetCollection, llinsVillageTarget);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LlinsVillageTarget to an array that doesn't contain it", () => {
          const llinsVillageTarget: ILlinsVillageTarget = { id: 123 };
          const llinsVillageTargetCollection: ILlinsVillageTarget[] = [{ id: 456 }];
          expectedResult = service.addLlinsVillageTargetToCollectionIfMissing(llinsVillageTargetCollection, llinsVillageTarget);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsVillageTarget);
        });

        it('should add only unique LlinsVillageTarget to an array', () => {
          const llinsVillageTargetArray: ILlinsVillageTarget[] = [{ id: 123 }, { id: 456 }, { id: 75800 }];
          const llinsVillageTargetCollection: ILlinsVillageTarget[] = [{ id: 123 }];
          expectedResult = service.addLlinsVillageTargetToCollectionIfMissing(llinsVillageTargetCollection, ...llinsVillageTargetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const llinsVillageTarget: ILlinsVillageTarget = { id: 123 };
          const llinsVillageTarget2: ILlinsVillageTarget = { id: 456 };
          expectedResult = service.addLlinsVillageTargetToCollectionIfMissing([], llinsVillageTarget, llinsVillageTarget2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(llinsVillageTarget);
          expect(expectedResult).toContain(llinsVillageTarget2);
        });

        it('should accept null and undefined values', () => {
          const llinsVillageTarget: ILlinsVillageTarget = { id: 123 };
          expectedResult = service.addLlinsVillageTargetToCollectionIfMissing([], null, llinsVillageTarget, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(llinsVillageTarget);
        });

        it('should return initial array if no LlinsVillageTarget is added', () => {
          const llinsVillageTargetCollection: ILlinsVillageTarget[] = [{ id: 123 }];
          expectedResult = service.addLlinsVillageTargetToCollectionIfMissing(llinsVillageTargetCollection, undefined, null);
          expect(expectedResult).toEqual(llinsVillageTargetCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
