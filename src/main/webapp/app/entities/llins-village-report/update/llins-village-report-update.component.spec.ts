jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LLINSVillageReportService } from '../service/llins-village-report.service';
import { ILLINSVillageReport, LLINSVillageReport } from '../llins-village-report.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSVillageTarget } from 'app/entities/llins-village-target/llins-village-target.model';
import { LLINSVillageTargetService } from 'app/entities/llins-village-target/service/llins-village-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { LLINSVillageReportUpdateComponent } from './llins-village-report-update.component';

describe('Component Tests', () => {
  describe('LLINSVillageReport Management Update Component', () => {
    let comp: LLINSVillageReportUpdateComponent;
    let fixture: ComponentFixture<LLINSVillageReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lLINSVillageReportService: LLINSVillageReportService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let lLINSVillageTargetService: LLINSVillageTargetService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSVillageReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LLINSVillageReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSVillageReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lLINSVillageReportService = TestBed.inject(LLINSVillageReportService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      lLINSVillageTargetService = TestBed.inject(LLINSVillageTargetService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const lLINSVillageReport: ILLINSVillageReport = { id: 456 };
        const createdBy: IUser = { id: 67754 };
        lLINSVillageReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 11140 };
        lLINSVillageReport.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 32182 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const lLINSVillageReport: ILLINSVillageReport = { id: 456 };
        const dayReached: IWorkingDay = { id: 36355 };
        lLINSVillageReport.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 89746 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LLINSVillageTarget query and add missing value', () => {
        const lLINSVillageReport: ILLINSVillageReport = { id: 456 };
        const targetDetails: ILLINSVillageTarget = { id: 81474 };
        lLINSVillageReport.targetDetails = targetDetails;

        const lLINSVillageTargetCollection: ILLINSVillageTarget[] = [{ id: 54285 }];
        jest.spyOn(lLINSVillageTargetService, 'query').mockReturnValue(of(new HttpResponse({ body: lLINSVillageTargetCollection })));
        const additionalLLINSVillageTargets = [targetDetails];
        const expectedCollection: ILLINSVillageTarget[] = [...additionalLLINSVillageTargets, ...lLINSVillageTargetCollection];
        jest.spyOn(lLINSVillageTargetService, 'addLLINSVillageTargetToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        expect(lLINSVillageTargetService.query).toHaveBeenCalled();
        expect(lLINSVillageTargetService.addLLINSVillageTargetToCollectionIfMissing).toHaveBeenCalledWith(
          lLINSVillageTargetCollection,
          ...additionalLLINSVillageTargets
        );
        expect(comp.lLINSVillageTargetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const lLINSVillageReport: ILLINSVillageReport = { id: 456 };
        const executingTeam: ITeam = { id: 20737 };
        lLINSVillageReport.executingTeam = executingTeam;

        const teamCollection: ITeam[] = [{ id: 43625 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [executingTeam];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lLINSVillageReport: ILLINSVillageReport = { id: 456 };
        const createdBy: IUser = { id: 77620 };
        lLINSVillageReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 40616 };
        lLINSVillageReport.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 62688 };
        lLINSVillageReport.dayReached = dayReached;
        const targetDetails: ILLINSVillageTarget = { id: 72217 };
        lLINSVillageReport.targetDetails = targetDetails;
        const executingTeam: ITeam = { id: 49134 };
        lLINSVillageReport.executingTeam = executingTeam;

        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lLINSVillageReport));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.lLINSVillageTargetsSharedCollection).toContain(targetDetails);
        expect(comp.teamsSharedCollection).toContain(executingTeam);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageReport>>();
        const lLINSVillageReport = { id: 123 };
        jest.spyOn(lLINSVillageReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSVillageReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lLINSVillageReportService.update).toHaveBeenCalledWith(lLINSVillageReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageReport>>();
        const lLINSVillageReport = new LLINSVillageReport();
        jest.spyOn(lLINSVillageReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSVillageReport }));
        saveSubject.complete();

        // THEN
        expect(lLINSVillageReportService.create).toHaveBeenCalledWith(lLINSVillageReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageReport>>();
        const lLINSVillageReport = { id: 123 };
        jest.spyOn(lLINSVillageReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lLINSVillageReportService.update).toHaveBeenCalledWith(lLINSVillageReport);
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

      describe('trackLLINSVillageTargetById', () => {
        it('Should return tracked LLINSVillageTarget primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLLINSVillageTargetById(0, entity);
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
