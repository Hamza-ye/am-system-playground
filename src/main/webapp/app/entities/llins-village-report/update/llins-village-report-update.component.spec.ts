jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LlinsVillageReportService } from '../service/llins-village-report.service';
import { ILlinsVillageReport, LlinsVillageReport } from '../llins-village-report.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsVillageTarget } from 'app/entities/llins-village-target/llins-village-target.model';
import { LlinsVillageTargetService } from 'app/entities/llins-village-target/service/llins-village-target.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';

import { LlinsVillageReportUpdateComponent } from './llins-village-report-update.component';

describe('Component Tests', () => {
  describe('LlinsVillageReport Management Update Component', () => {
    let comp: LlinsVillageReportUpdateComponent;
    let fixture: ComponentFixture<LlinsVillageReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let llinsVillageReportService: LlinsVillageReportService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let llinsVillageTargetService: LlinsVillageTargetService;
    let teamService: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsVillageReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LlinsVillageReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsVillageReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      llinsVillageReportService = TestBed.inject(LlinsVillageReportService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      llinsVillageTargetService = TestBed.inject(LlinsVillageTargetService);
      teamService = TestBed.inject(TeamService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const llinsVillageReport: ILlinsVillageReport = { id: 456 };
        const createdBy: IUser = { id: 85834 };
        llinsVillageReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 44312 };
        llinsVillageReport.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 11110 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const llinsVillageReport: ILlinsVillageReport = { id: 456 };
        const dayReached: IWorkingDay = { id: 29721 };
        llinsVillageReport.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 90644 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LlinsVillageTarget query and add missing value', () => {
        const llinsVillageReport: ILlinsVillageReport = { id: 456 };
        const targetDetails: ILlinsVillageTarget = { id: 5370 };
        llinsVillageReport.targetDetails = targetDetails;

        const llinsVillageTargetCollection: ILlinsVillageTarget[] = [{ id: 89490 }];
        jest.spyOn(llinsVillageTargetService, 'query').mockReturnValue(of(new HttpResponse({ body: llinsVillageTargetCollection })));
        const additionalLlinsVillageTargets = [targetDetails];
        const expectedCollection: ILlinsVillageTarget[] = [...additionalLlinsVillageTargets, ...llinsVillageTargetCollection];
        jest.spyOn(llinsVillageTargetService, 'addLlinsVillageTargetToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        expect(llinsVillageTargetService.query).toHaveBeenCalled();
        expect(llinsVillageTargetService.addLlinsVillageTargetToCollectionIfMissing).toHaveBeenCalledWith(
          llinsVillageTargetCollection,
          ...additionalLlinsVillageTargets
        );
        expect(comp.llinsVillageTargetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const llinsVillageReport: ILlinsVillageReport = { id: 456 };
        const executingTeam: ITeam = { id: 79576 };
        llinsVillageReport.executingTeam = executingTeam;

        const teamCollection: ITeam[] = [{ id: 36402 }];
        jest.spyOn(teamService, 'query').mockReturnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [executingTeam];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        jest.spyOn(teamService, 'addTeamToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const llinsVillageReport: ILlinsVillageReport = { id: 456 };
        const createdBy: IUser = { id: 11605 };
        llinsVillageReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 9249 };
        llinsVillageReport.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 26276 };
        llinsVillageReport.dayReached = dayReached;
        const targetDetails: ILlinsVillageTarget = { id: 36942 };
        llinsVillageReport.targetDetails = targetDetails;
        const executingTeam: ITeam = { id: 96898 };
        llinsVillageReport.executingTeam = executingTeam;

        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(llinsVillageReport));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.llinsVillageTargetsSharedCollection).toContain(targetDetails);
        expect(comp.teamsSharedCollection).toContain(executingTeam);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsVillageReport>>();
        const llinsVillageReport = { id: 123 };
        jest.spyOn(llinsVillageReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsVillageReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(llinsVillageReportService.update).toHaveBeenCalledWith(llinsVillageReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsVillageReport>>();
        const llinsVillageReport = new LlinsVillageReport();
        jest.spyOn(llinsVillageReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsVillageReport }));
        saveSubject.complete();

        // THEN
        expect(llinsVillageReportService.create).toHaveBeenCalledWith(llinsVillageReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsVillageReport>>();
        const llinsVillageReport = { id: 123 };
        jest.spyOn(llinsVillageReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsVillageReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(llinsVillageReportService.update).toHaveBeenCalledWith(llinsVillageReport);
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

      describe('trackLlinsVillageTargetById', () => {
        it('Should return tracked LlinsVillageTarget primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLlinsVillageTargetById(0, entity);
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
