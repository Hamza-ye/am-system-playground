import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRelativePeriods, RelativePeriods } from '../relative-periods.model';

import { RelativePeriodsService } from './relative-periods.service';

describe('Service Tests', () => {
  describe('RelativePeriods Service', () => {
    let service: RelativePeriodsService;
    let httpMock: HttpTestingController;
    let elemDefault: IRelativePeriods;
    let expectedResult: IRelativePeriods | IRelativePeriods[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RelativePeriodsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        thisDay: false,
        yesterday: false,
        last3Days: false,
        last7Days: false,
        last14Days: false,
        thisMonth: false,
        lastMonth: false,
        thisBimonth: false,
        lastBimonth: false,
        thisQuarter: false,
        lastQuarter: false,
        thisSixMonth: false,
        lastSixMonth: false,
        weeksThisYear: false,
        monthsThisYear: false,
        biMonthsThisYear: false,
        quartersThisYear: false,
        thisYear: false,
        monthsLastYear: false,
        quartersLastYear: false,
        lastYear: false,
        last5Years: false,
        last12Months: false,
        last6Months: false,
        last3Months: false,
        last6BiMonths: false,
        last4Quarters: false,
        last2SixMonths: false,
        thisFinancialYear: false,
        lastFinancialYear: false,
        last5FinancialYears: false,
        thisWeek: false,
        lastWeek: false,
        thisBiWeek: false,
        lastBiWeek: false,
        last4Weeks: false,
        last4BiWeeks: false,
        last12Weeks: false,
        last52Weeks: false,
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

      it('should create a RelativePeriods', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new RelativePeriods()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RelativePeriods', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            thisDay: true,
            yesterday: true,
            last3Days: true,
            last7Days: true,
            last14Days: true,
            thisMonth: true,
            lastMonth: true,
            thisBimonth: true,
            lastBimonth: true,
            thisQuarter: true,
            lastQuarter: true,
            thisSixMonth: true,
            lastSixMonth: true,
            weeksThisYear: true,
            monthsThisYear: true,
            biMonthsThisYear: true,
            quartersThisYear: true,
            thisYear: true,
            monthsLastYear: true,
            quartersLastYear: true,
            lastYear: true,
            last5Years: true,
            last12Months: true,
            last6Months: true,
            last3Months: true,
            last6BiMonths: true,
            last4Quarters: true,
            last2SixMonths: true,
            thisFinancialYear: true,
            lastFinancialYear: true,
            last5FinancialYears: true,
            thisWeek: true,
            lastWeek: true,
            thisBiWeek: true,
            lastBiWeek: true,
            last4Weeks: true,
            last4BiWeeks: true,
            last12Weeks: true,
            last52Weeks: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a RelativePeriods', () => {
        const patchObject = Object.assign(
          {
            thisBimonth: true,
            lastBimonth: true,
            lastQuarter: true,
            weeksThisYear: true,
            monthsLastYear: true,
            last6Months: true,
            lastWeek: true,
            thisBiWeek: true,
            last4Weeks: true,
            last4BiWeeks: true,
            last12Weeks: true,
          },
          new RelativePeriods()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RelativePeriods', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            thisDay: true,
            yesterday: true,
            last3Days: true,
            last7Days: true,
            last14Days: true,
            thisMonth: true,
            lastMonth: true,
            thisBimonth: true,
            lastBimonth: true,
            thisQuarter: true,
            lastQuarter: true,
            thisSixMonth: true,
            lastSixMonth: true,
            weeksThisYear: true,
            monthsThisYear: true,
            biMonthsThisYear: true,
            quartersThisYear: true,
            thisYear: true,
            monthsLastYear: true,
            quartersLastYear: true,
            lastYear: true,
            last5Years: true,
            last12Months: true,
            last6Months: true,
            last3Months: true,
            last6BiMonths: true,
            last4Quarters: true,
            last2SixMonths: true,
            thisFinancialYear: true,
            lastFinancialYear: true,
            last5FinancialYears: true,
            thisWeek: true,
            lastWeek: true,
            thisBiWeek: true,
            lastBiWeek: true,
            last4Weeks: true,
            last4BiWeeks: true,
            last12Weeks: true,
            last52Weeks: true,
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

      it('should delete a RelativePeriods', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRelativePeriodsToCollectionIfMissing', () => {
        it('should add a RelativePeriods to an empty array', () => {
          const relativePeriods: IRelativePeriods = { id: 123 };
          expectedResult = service.addRelativePeriodsToCollectionIfMissing([], relativePeriods);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relativePeriods);
        });

        it('should not add a RelativePeriods to an array that contains it', () => {
          const relativePeriods: IRelativePeriods = { id: 123 };
          const relativePeriodsCollection: IRelativePeriods[] = [
            {
              ...relativePeriods,
            },
            { id: 456 },
          ];
          expectedResult = service.addRelativePeriodsToCollectionIfMissing(relativePeriodsCollection, relativePeriods);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RelativePeriods to an array that doesn't contain it", () => {
          const relativePeriods: IRelativePeriods = { id: 123 };
          const relativePeriodsCollection: IRelativePeriods[] = [{ id: 456 }];
          expectedResult = service.addRelativePeriodsToCollectionIfMissing(relativePeriodsCollection, relativePeriods);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relativePeriods);
        });

        it('should add only unique RelativePeriods to an array', () => {
          const relativePeriodsArray: IRelativePeriods[] = [{ id: 123 }, { id: 456 }, { id: 94233 }];
          const relativePeriodsCollection: IRelativePeriods[] = [{ id: 123 }];
          expectedResult = service.addRelativePeriodsToCollectionIfMissing(relativePeriodsCollection, ...relativePeriodsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const relativePeriods: IRelativePeriods = { id: 123 };
          const relativePeriods2: IRelativePeriods = { id: 456 };
          expectedResult = service.addRelativePeriodsToCollectionIfMissing([], relativePeriods, relativePeriods2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(relativePeriods);
          expect(expectedResult).toContain(relativePeriods2);
        });

        it('should accept null and undefined values', () => {
          const relativePeriods: IRelativePeriods = { id: 123 };
          expectedResult = service.addRelativePeriodsToCollectionIfMissing([], null, relativePeriods, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(relativePeriods);
        });

        it('should return initial array if no RelativePeriods is added', () => {
          const relativePeriodsCollection: IRelativePeriods[] = [{ id: 123 }];
          expectedResult = service.addRelativePeriodsToCollectionIfMissing(relativePeriodsCollection, undefined, null);
          expect(expectedResult).toEqual(relativePeriodsCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
