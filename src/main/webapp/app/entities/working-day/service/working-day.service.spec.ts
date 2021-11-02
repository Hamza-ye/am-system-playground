import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWorkingDay, WorkingDay } from '../working-day.model';

import { WorkingDayService } from './working-day.service';

describe('Service Tests', () => {
  describe('WorkingDay Service', () => {
    let service: WorkingDayService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorkingDay;
    let expectedResult: IWorkingDay | IWorkingDay[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkingDayService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        dayNo: 0,
        dayLabel: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a WorkingDay', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new WorkingDay()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WorkingDay', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dayNo: 1,
            dayLabel: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WorkingDay', () => {
        const patchObject = Object.assign(
          {
            dayLabel: 'BBBBBB',
          },
          new WorkingDay()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WorkingDay', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dayNo: 1,
            dayLabel: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a WorkingDay', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkingDayToCollectionIfMissing', () => {
        it('should add a WorkingDay to an empty array', () => {
          const workingDay: IWorkingDay = { id: 123 };
          expectedResult = service.addWorkingDayToCollectionIfMissing([], workingDay);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workingDay);
        });

        it('should not add a WorkingDay to an array that contains it', () => {
          const workingDay: IWorkingDay = { id: 123 };
          const workingDayCollection: IWorkingDay[] = [
            {
              ...workingDay,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkingDayToCollectionIfMissing(workingDayCollection, workingDay);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WorkingDay to an array that doesn't contain it", () => {
          const workingDay: IWorkingDay = { id: 123 };
          const workingDayCollection: IWorkingDay[] = [{ id: 456 }];
          expectedResult = service.addWorkingDayToCollectionIfMissing(workingDayCollection, workingDay);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workingDay);
        });

        it('should add only unique WorkingDay to an array', () => {
          const workingDayArray: IWorkingDay[] = [{ id: 123 }, { id: 456 }, { id: 75511 }];
          const workingDayCollection: IWorkingDay[] = [{ id: 123 }];
          expectedResult = service.addWorkingDayToCollectionIfMissing(workingDayCollection, ...workingDayArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const workingDay: IWorkingDay = { id: 123 };
          const workingDay2: IWorkingDay = { id: 456 };
          expectedResult = service.addWorkingDayToCollectionIfMissing([], workingDay, workingDay2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workingDay);
          expect(expectedResult).toContain(workingDay2);
        });

        it('should accept null and undefined values', () => {
          const workingDay: IWorkingDay = { id: 123 };
          expectedResult = service.addWorkingDayToCollectionIfMissing([], null, workingDay, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workingDay);
        });

        it('should return initial array if no WorkingDay is added', () => {
          const workingDayCollection: IWorkingDay[] = [{ id: 123 }];
          expectedResult = service.addWorkingDayToCollectionIfMissing(workingDayCollection, undefined, null);
          expect(expectedResult).toEqual(workingDayCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
