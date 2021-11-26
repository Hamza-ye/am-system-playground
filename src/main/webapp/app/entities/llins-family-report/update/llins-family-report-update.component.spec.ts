jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LlinsFamilyReportService } from '../service/llins-family-report.service';
import { ILlinsFamilyReport, LlinsFamilyReport } from '../llins-family-report.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsFamilyTarget } from 'app/entities/llins-family-target/llins-family-target.model';
import { LlinsFamilyTargetService } from 'app/entities/llins-family-target/service/llins-family-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { LlinsFamilyReportUpdateComponent } from './llins-family-report-update.component';

describe('Component Tests', () => {
  describe('LlinsFamilyReport Management Update Component', () => {
    let comp: LlinsFamilyReportUpdateComponent;
    let fixture: ComponentFixture<LlinsFamilyReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let llinsFamilyReportService: LlinsFamilyReportService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let llinsFamilyTargetService: LlinsFamilyTargetService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsFamilyReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LlinsFamilyReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsFamilyReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      llinsFamilyReportService = TestBed.inject(LlinsFamilyReportService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      llinsFamilyTargetService = TestBed.inject(LlinsFamilyTargetService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const llinsFamilyReport: ILlinsFamilyReport = { id: 456 };
        const createdBy: IUser = { id: 49962 };
        llinsFamilyReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 74288 };
        llinsFamilyReport.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 73936 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const llinsFamilyReport: ILlinsFamilyReport = { id: 456 };
        const dayReached: IWorkingDay = { id: 8634 };
        llinsFamilyReport.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 61888 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LlinsFamilyTarget query and add missing value', () => {
        const llinsFamilyReport: ILlinsFamilyReport = { id: 456 };
        const targetDetails: ILlinsFamilyTarget = { id: 52109 };
        llinsFamilyReport.targetDetails = targetDetails;

        const llinsFamilyTargetCollection: ILlinsFamilyTarget[] = [{ id: 44996 }];
        jest.spyOn(llinsFamilyTargetService, 'query').mockReturnValue(of(new HttpResponse({ body: llinsFamilyTargetCollection })));
        const additionalLlinsFamilyTargets = [targetDetails];
        const expectedCollection: ILlinsFamilyTarget[] = [...additionalLlinsFamilyTargets, ...llinsFamilyTargetCollection];
        jest.spyOn(llinsFamilyTargetService, 'addLlinsFamilyTargetToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        expect(llinsFamilyTargetService.query).toHaveBeenCalled();
        expect(llinsFamilyTargetService.addLlinsFamilyTargetToCollectionIfMissing).toHaveBeenCalledWith(
          llinsFamilyTargetCollection,
          ...additionalLlinsFamilyTargets
        );
        expect(comp.llinsFamilyTargetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const llinsFamilyReport: ILlinsFamilyReport = { id: 456 };
        const executingTeam: ITeam = { id: 27824 };
        llinsFamilyReport.executingTeam = executingTeam;

        const teamCollection: ITeam[] = [{ id: 48632 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [executingTeam];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const llinsFamilyReport: ILlinsFamilyReport = { id: 456 };
        const createdBy: IUser = { id: 39903 };
        llinsFamilyReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 40418 };
        llinsFamilyReport.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 67997 };
        llinsFamilyReport.dayReached = dayReached;
        const targetDetails: ILlinsFamilyTarget = { id: 58004 };
        llinsFamilyReport.targetDetails = targetDetails;
        const executingTeam: ITeam = { id: 84781 };
        llinsFamilyReport.executingTeam = executingTeam;

        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(llinsFamilyReport));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.llinsFamilyTargetsSharedCollection).toContain(targetDetails);
        expect(comp.teamsSharedCollection).toContain(executingTeam);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyReport>>();
        const llinsFamilyReport = { id: 123 };
        jest.spyOn(llinsFamilyReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsFamilyReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(llinsFamilyReportService.update).toHaveBeenCalledWith(llinsFamilyReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyReport>>();
        const llinsFamilyReport = new LlinsFamilyReport();
        jest.spyOn(llinsFamilyReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsFamilyReport }));
        saveSubject.complete();

        // THEN
        expect(llinsFamilyReportService.create).toHaveBeenCalledWith(llinsFamilyReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyReport>>();
        const llinsFamilyReport = { id: 123 };
        jest.spyOn(llinsFamilyReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(llinsFamilyReportService.update).toHaveBeenCalledWith(llinsFamilyReport);
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

      describe('trackLlinsFamilyTargetById', () => {
        it('Should return tracked LlinsFamilyTarget primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLlinsFamilyTargetById(0, entity);
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
