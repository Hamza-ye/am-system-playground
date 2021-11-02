import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { CHVTeamType } from 'app/entities/enumerations/chv-team-type.model';
import { ICHVTeam, CHVTeam } from '../chv-team.model';

import { CHVTeamService } from './chv-team.service';

describe('Service Tests', () => {
  describe('CHVTeam Service', () => {
    let service: CHVTeamService;
    let httpMock: HttpTestingController;
    let elemDefault: ICHVTeam;
    let expectedResult: ICHVTeam | ICHVTeam[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CHVTeamService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        teamNo: 'AAAAAAA',
        teamType: CHVTeamType.SUPERVISOR,
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

      it('should create a CHVTeam', () => {
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

        service.create(new CHVTeam()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CHVTeam', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            teamNo: 'BBBBBB',
            teamType: 'BBBBBB',
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

      it('should partial update a CHVTeam', () => {
        const patchObject = Object.assign(
          {
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            teamNo: 'BBBBBB',
          },
          new CHVTeam()
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

      it('should return a list of CHVTeam', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            teamNo: 'BBBBBB',
            teamType: 'BBBBBB',
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

      it('should delete a CHVTeam', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCHVTeamToCollectionIfMissing', () => {
        it('should add a CHVTeam to an empty array', () => {
          const cHVTeam: ICHVTeam = { id: 123 };
          expectedResult = service.addCHVTeamToCollectionIfMissing([], cHVTeam);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHVTeam);
        });

        it('should not add a CHVTeam to an array that contains it', () => {
          const cHVTeam: ICHVTeam = { id: 123 };
          const cHVTeamCollection: ICHVTeam[] = [
            {
              ...cHVTeam,
            },
            { id: 456 },
          ];
          expectedResult = service.addCHVTeamToCollectionIfMissing(cHVTeamCollection, cHVTeam);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CHVTeam to an array that doesn't contain it", () => {
          const cHVTeam: ICHVTeam = { id: 123 };
          const cHVTeamCollection: ICHVTeam[] = [{ id: 456 }];
          expectedResult = service.addCHVTeamToCollectionIfMissing(cHVTeamCollection, cHVTeam);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHVTeam);
        });

        it('should add only unique CHVTeam to an array', () => {
          const cHVTeamArray: ICHVTeam[] = [{ id: 123 }, { id: 456 }, { id: 76600 }];
          const cHVTeamCollection: ICHVTeam[] = [{ id: 123 }];
          expectedResult = service.addCHVTeamToCollectionIfMissing(cHVTeamCollection, ...cHVTeamArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const cHVTeam: ICHVTeam = { id: 123 };
          const cHVTeam2: ICHVTeam = { id: 456 };
          expectedResult = service.addCHVTeamToCollectionIfMissing([], cHVTeam, cHVTeam2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(cHVTeam);
          expect(expectedResult).toContain(cHVTeam2);
        });

        it('should accept null and undefined values', () => {
          const cHVTeam: ICHVTeam = { id: 123 };
          expectedResult = service.addCHVTeamToCollectionIfMissing([], null, cHVTeam, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(cHVTeam);
        });

        it('should return initial array if no CHVTeam is added', () => {
          const cHVTeamCollection: ICHVTeam[] = [{ id: 123 }];
          expectedResult = service.addCHVTeamToCollectionIfMissing(cHVTeamCollection, undefined, null);
          expect(expectedResult).toEqual(cHVTeamCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
