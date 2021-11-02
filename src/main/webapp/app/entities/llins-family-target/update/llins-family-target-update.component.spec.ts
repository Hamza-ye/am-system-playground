jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LLINSFamilyTargetService } from '../service/llins-family-target.service';
import { ILLINSFamilyTarget, LLINSFamilyTarget } from '../llins-family-target.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { LLINSFamilyTargetUpdateComponent } from './llins-family-target-update.component';

describe('Component Tests', () => {
  describe('LLINSFamilyTarget Management Update Component', () => {
    let comp: LLINSFamilyTargetUpdateComponent;
    let fixture: ComponentFixture<LLINSFamilyTargetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lLINSFamilyTargetService: LLINSFamilyTargetService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let familyService: FamilyService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSFamilyTargetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LLINSFamilyTargetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSFamilyTargetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lLINSFamilyTargetService = TestBed.inject(LLINSFamilyTargetService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      familyService = TestBed.inject(FamilyService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 456 };
        const user: IUser = { id: 57566 };
        lLINSFamilyTarget.user = user;
        const lastUpdatedBy: IUser = { id: 50548 };
        lLINSFamilyTarget.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 67095 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 456 };
        const dayPlanned: IWorkingDay = { id: 16885 };
        lLINSFamilyTarget.dayPlanned = dayPlanned;

        const workingDayCollection: IWorkingDay[] = [{ id: 30852 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayPlanned];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Family query and add missing value', () => {
        const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 456 };
        const family: IFamily = { id: 49747 };
        lLINSFamilyTarget.family = family;

        const familyCollection: IFamily[] = [{ id: 62522 }];
        jest.spyOn(familyService, 'query').mockReturnValue(of(new HttpResponse({ body: familyCollection })));
        const additionalFamilies = [family];
        const expectedCollection: IFamily[] = [...additionalFamilies, ...familyCollection];
        jest.spyOn(familyService, 'addFamilyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        expect(familyService.query).toHaveBeenCalled();
        expect(familyService.addFamilyToCollectionIfMissing).toHaveBeenCalledWith(familyCollection, ...additionalFamilies);
        expect(comp.familiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 456 };
        const teamAssigned: ITeam = { id: 53637 };
        lLINSFamilyTarget.teamAssigned = teamAssigned;

        const teamCollection: ITeam[] = [{ id: 72449 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [teamAssigned];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lLINSFamilyTarget: ILLINSFamilyTarget = { id: 456 };
        const user: IUser = { id: 24595 };
        lLINSFamilyTarget.user = user;
        const lastUpdatedBy: IUser = { id: 55190 };
        lLINSFamilyTarget.lastUpdatedBy = lastUpdatedBy;
        const dayPlanned: IWorkingDay = { id: 77370 };
        lLINSFamilyTarget.dayPlanned = dayPlanned;
        const family: IFamily = { id: 49624 };
        lLINSFamilyTarget.family = family;
        const teamAssigned: ITeam = { id: 57311 };
        lLINSFamilyTarget.teamAssigned = teamAssigned;

        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lLINSFamilyTarget));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayPlanned);
        expect(comp.familiesSharedCollection).toContain(family);
        expect(comp.teamsSharedCollection).toContain(teamAssigned);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyTarget>>();
        const lLINSFamilyTarget = { id: 123 };
        jest.spyOn(lLINSFamilyTargetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSFamilyTarget }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lLINSFamilyTargetService.update).toHaveBeenCalledWith(lLINSFamilyTarget);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyTarget>>();
        const lLINSFamilyTarget = new LLINSFamilyTarget();
        jest.spyOn(lLINSFamilyTargetService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSFamilyTarget }));
        saveSubject.complete();

        // THEN
        expect(lLINSFamilyTargetService.create).toHaveBeenCalledWith(lLINSFamilyTarget);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyTarget>>();
        const lLINSFamilyTarget = { id: 123 };
        jest.spyOn(lLINSFamilyTargetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyTarget });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lLINSFamilyTargetService.update).toHaveBeenCalledWith(lLINSFamilyTarget);
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
