import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TeamType } from 'app/entities/enumerations/team-type.model';
import { ITeam, Team } from '../team.model';

import { TeamService } from './team.service';

describe('Service Tests', () => {
  describe('Team Service', () => {
    let service: TeamService;
    let httpMock: HttpTestingController;
    let elemDefault: ITeam;
    let expectedResult: ITeam | ITeam[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TeamService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uid: 'AAAAAAA',
        code: 'AAAAAAA',
        name: 'AAAAAAA',
        created: currentDate,
        lastUpdated: currentDate,
        teamNo: 0,
        teamType: TeamType.FIELD_TEAM_LEADER,
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

      it('should create a Team', () => {
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

        service.create(new Team()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Team', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            teamNo: 1,
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

      it('should partial update a Team', () => {
        const patchObject = Object.assign(
          {
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            teamType: 'BBBBBB',
          },
          new Team()
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

      it('should return a list of Team', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uid: 'BBBBBB',
            code: 'BBBBBB',
            name: 'BBBBBB',
            created: currentDate.format(DATE_TIME_FORMAT),
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
            teamNo: 1,
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

      it('should delete a Team', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTeamToCollectionIfMissing', () => {
        it('should add a Team to an empty array', () => {
          const team: ITeam = { id: 123 };
          expectedResult = service.addTeamToCollectionIfMissing([], team);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(team);
        });

        it('should not add a Team to an array that contains it', () => {
          const team: ITeam = { id: 123 };
          const teamCollection: ITeam[] = [
            {
              ...team,
            },
            { id: 456 },
          ];
          expectedResult = service.addTeamToCollectionIfMissing(teamCollection, team);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Team to an array that doesn't contain it", () => {
          const team: ITeam = { id: 123 };
          const teamCollection: ITeam[] = [{ id: 456 }];
          expectedResult = service.addTeamToCollectionIfMissing(teamCollection, team);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(team);
        });

        it('should add only unique Team to an array', () => {
          const teamArray: ITeam[] = [{ id: 123 }, { id: 456 }, { id: 79830 }];
          const teamCollection: ITeam[] = [{ id: 123 }];
          expectedResult = service.addTeamToCollectionIfMissing(teamCollection, ...teamArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const team: ITeam = { id: 123 };
          const team2: ITeam = { id: 456 };
          expectedResult = service.addTeamToCollectionIfMissing([], team, team2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(team);
          expect(expectedResult).toContain(team2);
        });

        it('should accept null and undefined values', () => {
          const team: ITeam = { id: 123 };
          expectedResult = service.addTeamToCollectionIfMissing([], null, team, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(team);
        });

        it('should return initial array if no Team is added', () => {
          const teamCollection: ITeam[] = [{ id: 123 }];
          expectedResult = service.addTeamToCollectionIfMissing(teamCollection, undefined, null);
          expect(expectedResult).toEqual(teamCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
