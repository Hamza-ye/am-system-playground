jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LLINSFamilyReportService } from '../service/llins-family-report.service';
import { ILLINSFamilyReport, LLINSFamilyReport } from '../llins-family-report.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSFamilyTarget } from 'app/entities/llins-family-target/llins-family-target.model';
import { LLINSFamilyTargetService } from 'app/entities/llins-family-target/service/llins-family-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { LLINSFamilyReportUpdateComponent } from './llins-family-report-update.component';

describe('Component Tests', () => {
  describe('LLINSFamilyReport Management Update Component', () => {
    let comp: LLINSFamilyReportUpdateComponent;
    let fixture: ComponentFixture<LLINSFamilyReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lLINSFamilyReportService: LLINSFamilyReportService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let lLINSFamilyTargetService: LLINSFamilyTargetService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSFamilyReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LLINSFamilyReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSFamilyReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lLINSFamilyReportService = TestBed.inject(LLINSFamilyReportService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      lLINSFamilyTargetService = TestBed.inject(LLINSFamilyTargetService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const lLINSFamilyReport: ILLINSFamilyReport = { id: 456 };
        const createdBy: IUser = { id: 30424 };
        lLINSFamilyReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 35407 };
        lLINSFamilyReport.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 32607 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const lLINSFamilyReport: ILLINSFamilyReport = { id: 456 };
        const dayReached: IWorkingDay = { id: 96127 };
        lLINSFamilyReport.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 43048 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LLINSFamilyTarget query and add missing value', () => {
        const lLINSFamilyReport: ILLINSFamilyReport = { id: 456 };
        const targetDetails: ILLINSFamilyTarget = { id: 69700 };
        lLINSFamilyReport.targetDetails = targetDetails;

        const lLINSFamilyTargetCollection: ILLINSFamilyTarget[] = [{ id: 71440 }];
        jest.spyOn(lLINSFamilyTargetService, 'query').mockReturnValue(of(new HttpResponse({ body: lLINSFamilyTargetCollection })));
        const additionalLLINSFamilyTargets = [targetDetails];
        const expectedCollection: ILLINSFamilyTarget[] = [...additionalLLINSFamilyTargets, ...lLINSFamilyTargetCollection];
        jest.spyOn(lLINSFamilyTargetService, 'addLLINSFamilyTargetToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        expect(lLINSFamilyTargetService.query).toHaveBeenCalled();
        expect(lLINSFamilyTargetService.addLLINSFamilyTargetToCollectionIfMissing).toHaveBeenCalledWith(
          lLINSFamilyTargetCollection,
          ...additionalLLINSFamilyTargets
        );
        expect(comp.lLINSFamilyTargetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const lLINSFamilyReport: ILLINSFamilyReport = { id: 456 };
        const executingTeam: ITeam = { id: 42816 };
        lLINSFamilyReport.executingTeam = executingTeam;

        const teamCollection: ITeam[] = [{ id: 46453 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [executingTeam];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lLINSFamilyReport: ILLINSFamilyReport = { id: 456 };
        const createdBy: IUser = { id: 82195 };
        lLINSFamilyReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 37574 };
        lLINSFamilyReport.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 21885 };
        lLINSFamilyReport.dayReached = dayReached;
        const targetDetails: ILLINSFamilyTarget = { id: 5237 };
        lLINSFamilyReport.targetDetails = targetDetails;
        const executingTeam: ITeam = { id: 25688 };
        lLINSFamilyReport.executingTeam = executingTeam;

        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lLINSFamilyReport));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.lLINSFamilyTargetsSharedCollection).toContain(targetDetails);
        expect(comp.teamsSharedCollection).toContain(executingTeam);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyReport>>();
        const lLINSFamilyReport = { id: 123 };
        jest.spyOn(lLINSFamilyReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSFamilyReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lLINSFamilyReportService.update).toHaveBeenCalledWith(lLINSFamilyReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyReport>>();
        const lLINSFamilyReport = new LLINSFamilyReport();
        jest.spyOn(lLINSFamilyReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSFamilyReport }));
        saveSubject.complete();

        // THEN
        expect(lLINSFamilyReportService.create).toHaveBeenCalledWith(lLINSFamilyReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyReport>>();
        const lLINSFamilyReport = { id: 123 };
        jest.spyOn(lLINSFamilyReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lLINSFamilyReportService.update).toHaveBeenCalledWith(lLINSFamilyReport);
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

      describe('trackLLINSFamilyTargetById', () => {
        it('Should return tracked LLINSFamilyTarget primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLLINSFamilyTargetById(0, entity);
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
