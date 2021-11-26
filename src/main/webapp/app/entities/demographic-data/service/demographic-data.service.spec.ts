import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DemographicDataLevel } from 'app/entities/enumerations/demographic-data-level.model';
import { IDemographicData, DemographicData } from '../demographic-data.model';

import { DemographicDataService } from './demographic-data.service';

describe('Service Tests', () => {
  describe('DemographicData Service', () => {
    let service: DemographicDataService;
    let httpMock: HttpTestingController;
    let elemDefault: IDemographicData;
    let expectedResult: IDemographicData | IDemographicData[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DemographicDataService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        date: currentDate,
        level: DemographicDataLevel.SUBVILLAGE_LEVEL,
        totalPopulation: 0,
        malePopulation: 0,
        femalePopulation: 0,
        lessThan5Population: 0,
        greaterThan5Population: 0,
        bw5And15Population: 0,
        greaterThan15Population: 0,
        household: 0,
        houses: 0,
        healthFacilities: 0,
        avgNoOfRooms: 0,
        avgRoomArea: 0,
        avgHouseArea: 0,
        individualsPerHousehold: 0,
        populationGrowthRate: 0,
        comment: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DemographicData', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new DemographicData()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DemographicData', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
            level: 'BBBBBB',
            totalPopulation: 1,
            malePopulation: 1,
            femalePopulation: 1,
            lessThan5Population: 1,
            greaterThan5Population: 1,
            bw5And15Population: 1,
            greaterThan15Population: 1,
            household: 1,
            houses: 1,
            healthFacilities: 1,
            avgNoOfRooms: 1,
            avgRoomArea: 1,
            avgHouseArea: 1,
            individualsPerHousehold: 1,
            populationGrowthRate: 1,
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DemographicData', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
            malePopulation: 1,
            lessThan5Population: 1,
            bw5And15Population: 1,
            houses: 1,
            healthFacilities: 1,
            avgNoOfRooms: 1,
            populationGrowthRate: 1,
            comment: 'BBBBBB',
          },
          new DemographicData()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DemographicData', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            date: currentDate.format(DATE_FORMAT),
            level: 'BBBBBB',
            totalPopulation: 1,
            malePopulation: 1,
            femalePopulation: 1,
            lessThan5Population: 1,
            greaterThan5Population: 1,
            bw5And15Population: 1,
            greaterThan15Population: 1,
            household: 1,
            houses: 1,
            healthFacilities: 1,
            avgNoOfRooms: 1,
            avgRoomArea: 1,
            avgHouseArea: 1,
            individualsPerHousehold: 1,
            populationGrowthRate: 1,
            comment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            created: currentDate,
            lastUpdated: currentDate,
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DemographicData', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDemographicDataToCollectionIfMissing', () => {
        it('should add a DemographicData to an empty array', () => {
          const demographicData: IDemographicData = { id: 123 };
          expectedResult = service.addDemographicDataToCollectionIfMissing([], demographicData);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demographicData);
        });

        it('should not add a DemographicData to an array that contains it', () => {
          const demographicData: IDemographicData = { id: 123 };
          const demographicDataCollection: IDemographicData[] = [
            {
              ...demographicData,
            },
            { id: 456 },
          ];
          expectedResult = service.addDemographicDataToCollectionIfMissing(demographicDataCollection, demographicData);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DemographicData to an array that doesn't contain it", () => {
          const demographicData: IDemographicData = { id: 123 };
          const demographicDataCollection: IDemographicData[] = [{ id: 456 }];
          expectedResult = service.addDemographicDataToCollectionIfMissing(demographicDataCollection, demographicData);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demographicData);
        });

        it('should add only unique DemographicData to an array', () => {
          const demographicDataArray: IDemographicData[] = [{ id: 123 }, { id: 456 }, { id: 8556 }];
          const demographicDataCollection: IDemographicData[] = [{ id: 123 }];
          expectedResult = service.addDemographicDataToCollectionIfMissing(demographicDataCollection, ...demographicDataArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const demographicData: IDemographicData = { id: 123 };
          const demographicData2: IDemographicData = { id: 456 };
          expectedResult = service.addDemographicDataToCollectionIfMissing([], demographicData, demographicData2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(demographicData);
          expect(expectedResult).toContain(demographicData2);
        });

        it('should accept null and undefined values', () => {
          const demographicData: IDemographicData = { id: 123 };
          expectedResult = service.addDemographicDataToCollectionIfMissing([], null, demographicData, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(demographicData);
        });

        it('should return initial array if no DemographicData is added', () => {
          const demographicDataCollection: IDemographicData[] = [{ id: 123 }];
          expectedResult = service.addDemographicDataToCollectionIfMissing(demographicDataCollection, undefined, null);
          expect(expectedResult).toEqual(demographicDataCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
