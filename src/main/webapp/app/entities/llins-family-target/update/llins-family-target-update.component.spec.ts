jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LlinsFamilyTargetService } from '../service/llins-family-target.service';
import { ILlinsFamilyTarget, LlinsFamilyTarget } from '../llins-family-target.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { LlinsFamilyTargetUpdateComponent } from './llins-family-target-update.component';

describe('Component Tests', () => {
  describe('LlinsFamilyTarget Management Update Component', () => {
    let comp: LlinsFamilyTargetUpdateComponent;
    let fixture: ComponentFixture<LlinsFamilyTargetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let llinsFamilyTargetService: LlinsFamilyTargetService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let familyService: FamilyService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsFamilyTargetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LlinsFamilyTargetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsFamilyTargetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      llinsFamilyTargetService = TestBed.inject(LlinsFamilyTargetService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      familyService = TestBed.inject(FamilyService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const llinsFamilyTarget: ILlinsFamilyTarget = { id: 456 };
        const createdBy: IUser = { id: 60057 };
        llinsFamilyTarget.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 42752 };
        llinsFamilyTarget.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 82449 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const llinsFamilyTarget: ILlinsFamilyTarget = { id: 456 };
        const dayPlanned: IWorkingDay = { id: 62023 };
        llinsFamilyTarget.dayPlanned = dayPlanned;

        const workingDayCollection: IWorkingDay[] = [{ id: 87926 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayPlanned];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Family query and add missing value', () => {
        const llinsFamilyTarget: ILlinsFamilyTarget = { id: 456 };
        const family: IFamily = { id: 70040 };
        llinsFamilyTarget.family = family;

        const familyCollection: IFamily[] = [{ id: 36021 }];
        jest.spyOn(familyService, 'query').mockReturnValue(of(new HttpResponse({ body: familyCollection })));
        const additionalFamilies = [family];
        const expectedCollection: IFamily[] = [...additionalFamilies, ...familyCollection];
        jest.spyOn(familyService, 'addFamilyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        expect(familyService.query).toHaveBeenCalled();
        expect(familyService.addFamilyToCollectionIfMissing).toHaveBeenCalledWith(familyCollection, ...additionalFamilies);
        expect(comp.familiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const llinsFamilyTarget: ILlinsFamilyTarget = { id: 456 };
        const teamAssigned: ITeam = { id: 14494 };
        llinsFamilyTarget.teamAssigned = teamAssigned;

        const teamCollection: ITeam[] = [{ id: 15842 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [teamAssigned];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const llinsFamilyTarget: ILlinsFamilyTarget = { id: 456 };
        const createdBy: IUser = { id: 68018 };
        llinsFamilyTarget.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 25917 };
        llinsFamilyTarget.lastUpdatedBy = lastUpdatedBy;
        const dayPlanned: IWorkingDay = { id: 9246 };
        llinsFamilyTarget.dayPlanned = dayPlanned;
        const family: IFamily = { id: 38747 };
        llinsFamilyTarget.family = family;
        const teamAssigned: ITeam = { id: 71021 };
        llinsFamilyTarget.teamAssigned = teamAssigned;

        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(llinsFamilyTarget));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayPlanned);
        expect(comp.familiesSharedCollection).toContain(family);
        expect(comp.teamsSharedCollection).toContain(teamAssigned);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyTarget>>();
        const llinsFamilyTarget = { id: 123 };
        jest.spyOn(llinsFamilyTargetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsFamilyTarget }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(llinsFamilyTargetService.update).toHaveBeenCalledWith(llinsFamilyTarget);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyTarget>>();
        const llinsFamilyTarget = new LlinsFamilyTarget();
        jest.spyOn(llinsFamilyTargetService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsFamilyTarget }));
        saveSubject.complete();

        // THEN
        expect(llinsFamilyTargetService.create).toHaveBeenCalledWith(llinsFamilyTarget);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyTarget>>();
        const llinsFamilyTarget = { id: 123 };
        jest.spyOn(llinsFamilyTargetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(llinsFamilyTargetService.update).toHaveBeenCalledWith(llinsFamilyTarget);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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

      describe('trackFamilyById', () => {
        it('Should return tracked Family primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFamilyById(0, entity);
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
