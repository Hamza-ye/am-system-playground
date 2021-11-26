import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ChvTeamType } from 'app/entities/enumerations/chv-team-type.model';
import { IChvTeam, ChvTeam } from '../chv-team.model';

import { ChvTeamService } from './chv-team.service';

describe('Service Tests', () => {
  describe('ChvTeam Service', () => {
    let service: ChvTeamService;
    let httpMock: HttpTestingController;
    let elemDefault: IChvTeam;
    let expectedResult: IChvTeam | IChvTeam[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ChvTeamService);
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
        teamType: ChvTeamType.SUPERVISOR,
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

      it('should create a ChvTeam', () => {
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

        service.create(new ChvTeam()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ChvTeam', () => {
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

      it('should partial update a ChvTeam', () => {
        const patchObject = Object.assign(
          {
            code: 'BBBBBB',
            description: 'BBBBBB',
            lastUpdated: currentDate.format(DATE_TIME_FORMAT),
          },
          new ChvTeam()
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

      it('should return a list of ChvTeam', () => {
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

      it('should delete a ChvTeam', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addChvTeamToCollectionIfMissing', () => {
        it('should add a ChvTeam to an empty array', () => {
          const chvTeam: IChvTeam = { id: 123 };
          expectedResult = service.addChvTeamToCollectionIfMissing([], chvTeam);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chvTeam);
        });

        it('should not add a ChvTeam to an array that contains it', () => {
          const chvTeam: IChvTeam = { id: 123 };
          const chvTeamCollection: IChvTeam[] = [
            {
              ...chvTeam,
            },
            { id: 456 },
          ];
          expectedResult = service.addChvTeamToCollectionIfMissing(chvTeamCollection, chvTeam);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ChvTeam to an array that doesn't contain it", () => {
          const chvTeam: IChvTeam = { id: 123 };
          const chvTeamCollection: IChvTeam[] = [{ id: 456 }];
          expectedResult = service.addChvTeamToCollectionIfMissing(chvTeamCollection, chvTeam);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chvTeam);
        });

        it('should add only unique ChvTeam to an array', () => {
          const chvTeamArray: IChvTeam[] = [{ id: 123 }, { id: 456 }, { id: 99031 }];
          const chvTeamCollection: IChvTeam[] = [{ id: 123 }];
          expectedResult = service.addChvTeamToCollectionIfMissing(chvTeamCollection, ...chvTeamArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const chvTeam: IChvTeam = { id: 123 };
          const chvTeam2: IChvTeam = { id: 456 };
          expectedResult = service.addChvTeamToCollectionIfMissing([], chvTeam, chvTeam2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(chvTeam);
          expect(expectedResult).toContain(chvTeam2);
        });

        it('should accept null and undefined values', () => {
          const chvTeam: IChvTeam = { id: 123 };
          expectedResult = service.addChvTeamToCollectionIfMissing([], null, chvTeam, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(chvTeam);
        });

        it('should return initial array if no ChvTeam is added', () => {
          const chvTeamCollection: IChvTeam[] = [{ id: 123 }];
          expectedResult = service.addChvTeamToCollectionIfMissing(chvTeamCollection, undefined, null);
          expect(expectedResult).toEqual(chvTeamCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
