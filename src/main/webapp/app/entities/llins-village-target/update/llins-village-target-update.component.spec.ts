jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LLINSVillageTargetService } from '../service/llins-village-target.service';
import { ILLINSVillageTarget, LLINSVillageTarget } from '../llins-village-target.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IStatusOfCoverage } from 'app/entities/status-of-coverage/status-of-coverage.model';
import { StatusOfCoverageService } from 'app/entities/status-of-coverage/service/status-of-coverage.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { LLINSVillageTargetUpdateComponent } from './llins-village-target-update.component';

describe('Component Tests', () => {
  describe('LLINSVillageTarget Management Update Component', () => {
    let comp: LLINSVillageTargetUpdateComponent;
    let fixture: ComponentFixture<LLINSVillageTargetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lLINSVillageTargetService: LLINSVillageTargetService;
    let organisationUnitService: OrganisationUnitService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let statusOfCoverageService: StatusOfCoverageService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSVillageTargetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LLINSVillageTargetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSVillageTargetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lLINSVillageTargetService = TestBed.inject(LLINSVillageTargetService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      statusOfCoverageService = TestBed.inject(StatusOfCoverageService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call OrganisationUnit query and add missing value', () => {
        const lLINSVillageTarget: ILLINSVillageTarget = { id: 456 };
        const organisationUnit: IOrganisationUnit = { id: 11032 };
        lLINSVillageTarget.organisationUnit = organisationUnit;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 44349 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [organisationUnit];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const lLINSVillageTarget: ILLINSVillageTarget = { id: 456 };
        const createdBy: IUser = { id: 23827 };
        lLINSVillageTarget.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 96102 };
        lLINSVillageTarget.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 25715 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const lLINSVillageTarget: ILLINSVillageTarget = { id: 456 };
        const dayPlanned: IWorkingDay = { id: 29584 };
        lLINSVillageTarget.dayPlanned = dayPlanned;

        const workingDayCollection: IWorkingDay[] = [{ id: 46198 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayPlanned];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call StatusOfCoverage query and add missing value', () => {
        const lLINSVillageTarget: ILLINSVillageTarget = { id: 456 };
        const statusOfCoverage: IStatusOfCoverage = { id: 44282 };
        lLINSVillageTarget.statusOfCoverage = statusOfCoverage;

        const statusOfCoverageCollection: IStatusOfCoverage[] = [{ id: 9906 }];
        jest.spyOn(statusOfCoverageService, 'query').mockReturnValue(of(new HttpResponse({ body: statusOfCoverageCollection })));
        const additionalStatusOfCoverages = [statusOfCoverage];
        const expectedCollection: IStatusOfCoverage[] = [...additionalStatusOfCoverages, ...statusOfCoverageCollection];
        jest.spyOn(statusOfCoverageService, 'addStatusOfCoverageToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        expect(statusOfCoverageService.query).toHaveBeenCalled();
        expect(statusOfCoverageService.addStatusOfCoverageToCollectionIfMissing).toHaveBeenCalledWith(
          statusOfCoverageCollection,
          ...additionalStatusOfCoverages
        );
        expect(comp.statusOfCoveragesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const lLINSVillageTarget: ILLINSVillageTarget = { id: 456 };
        const teamAssigned: ITeam = { id: 29884 };
        lLINSVillageTarget.teamAssigned = teamAssigned;

        const teamCollection: ITeam[] = [{ id: 276 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [teamAssigned];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lLINSVillageTarget: ILLINSVillageTarget = { id: 456 };
        const organisationUnit: IOrganisationUnit = { id: 56506 };
        lLINSVillageTarget.organisationUnit = organisationUnit;
        const createdBy: IUser = { id: 51409 };
        lLINSVillageTarget.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 73818 };
        lLINSVillageTarget.lastUpdatedBy = lastUpdatedBy;
        const dayPlanned: IWorkingDay = { id: 91735 };
        lLINSVillageTarget.dayPlanned = dayPlanned;
        const statusOfCoverage: IStatusOfCoverage = { id: 52302 };
        lLINSVillageTarget.statusOfCoverage = statusOfCoverage;
        const teamAssigned: ITeam = { id: 9347 };
        lLINSVillageTarget.teamAssigned = teamAssigned;

        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lLINSVillageTarget));
        expect(comp.organisationUnitsSharedCollection).toContain(organisationUnit);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayPlanned);
        expect(comp.statusOfCoveragesSharedCollection).toContain(statusOfCoverage);
        expect(comp.teamsSharedCollection).toContain(teamAssigned);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageTarget>>();
        const lLINSVillageTarget = { id: 123 };
        jest.spyOn(lLINSVillageTargetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSVillageTarget }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lLINSVillageTargetService.update).toHaveBeenCalledWith(lLINSVillageTarget);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageTarget>>();
        const lLINSVillageTarget = new LLINSVillageTarget();
        jest.spyOn(lLINSVillageTargetService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSVillageTarget }));
        saveSubject.complete();

        // THEN
        expect(lLINSVillageTargetService.create).toHaveBeenCalledWith(lLINSVillageTarget);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageTarget>>();
        const lLINSVillageTarget = { id: 123 };
        jest.spyOn(lLINSVillageTargetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lLINSVillageTargetService.update).toHaveBeenCalledWith(lLINSVillageTarget);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrganisationUnitById', () => {
        it('Should return tracked OrganisationUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganisationUnitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackWorkingDayById', () => {
        it('Should return tracked WorkingDay primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWorkingDayById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackStatusOfCoverageById', () => {
        it('Should return tracked StatusOfCoverage primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStatusOfCoverageById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTeamById', () => {
        it('Should return tracked Team primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
