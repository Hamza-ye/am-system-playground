import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ActivityService } from './activity.service';
import {Activity, IActivity} from "../../../../entities/activity/activity.model";

describe('Service Tests', () => {
  describe('Activity Service', () => {
    let service: ActivityService;
    let httpMock: HttpTestingController;
    let elemDefault: IActivity;
    let expectedResult: IActivity | IActivity[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ActivityService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        startDate: currentDate,
        endDate: currentDate,
        active: false,
        displayed: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Activity', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Activity()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Activity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            active: true,
            displayed: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Activity', () => {
        const patchObject = Object.assign({}, new Activity());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Activity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            active: true,
            displayed: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Activity', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addActivityToCollectionIfMissing', () => {
        it('should add a Activity to an empty array', () => {
          const activity: IActivity = { id: 123 };
          expectedResult = service.addActivityToCollectionIfMissing([], activity);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(activity);
        });

        it('should not add a Activity to an array that contains it', () => {
          const activity: IActivity = { id: 123 };
          const activityCollection: IActivity[] = [
            {
              ...activity,
            },
            { id: 456 },
          ];
          expectedResult = service.addActivityToCollectionIfMissing(activityCollection, activity);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Activity to an array that doesn't contain it", () => {
          const activity: IActivity = { id: 123 };
          const activityCollection: IActivity[] = [{ id: 456 }];
          expectedResult = service.addActivityToCollectionIfMissing(activityCollection, activity);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(activity);
        });

        it('should add only unique Activity to an array', () => {
          const activityArray: IActivity[] = [{ id: 123 }, { id: 456 }, { id: 17617 }];
          const activityCollection: IActivity[] = [{ id: 123 }];
          expectedResult = service.addActivityToCollectionIfMissing(activityCollection, ...activityArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const activity: IActivity = { id: 123 };
          const activity2: IActivity = { id: 456 };
          expectedResult = service.addActivityToCollectionIfMissing([], activity, activity2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(activity);
          expect(expectedResult).toContain(activity2);
        });

        it('should accept null and undefined values', () => {
          const activity: IActivity = { id: 123 };
          expectedResult = service.addActivityToCollectionIfMissing([], null, activity, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(activity);
        });

        it('should return initial array if no Activity is added', () => {
          const activityCollection: IActivity[] = [{ id: 123 }];
          expectedResult = service.addActivityToCollectionIfMissing(activityCollection, undefined, null);
          expect(expectedResult).toEqual(activityCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
