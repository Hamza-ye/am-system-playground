jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LLINSFamilyReportHistoryService } from '../service/llins-family-report-history.service';
import { ILLINSFamilyReportHistory, LLINSFamilyReportHistory } from '../llins-family-report-history.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSFamilyReport } from 'app/entities/llins-family-report/llins-family-report.model';
import { LLINSFamilyReportService } from 'app/entities/llins-family-report/service/llins-family-report.service';

import { LLINSFamilyReportHistoryUpdateComponent } from './llins-family-report-history-update.component';

describe('Component Tests', () => {
  describe('LLINSFamilyReportHistory Management Update Component', () => {
    let comp: LLINSFamilyReportHistoryUpdateComponent;
    let fixture: ComponentFixture<LLINSFamilyReportHistoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lLINSFamilyReportHistoryService: LLINSFamilyReportHistoryService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let lLINSFamilyReportService: LLINSFamilyReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSFamilyReportHistoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LLINSFamilyReportHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSFamilyReportHistoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lLINSFamilyReportHistoryService = TestBed.inject(LLINSFamilyReportHistoryService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      lLINSFamilyReportService = TestBed.inject(LLINSFamilyReportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 456 };
        const createdBy: IUser = { id: 16250 };
        lLINSFamilyReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 65450 };
        lLINSFamilyReportHistory.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 75030 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyReportHistory });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 456 };
        const dayReached: IWorkingDay = { id: 5714 };
        lLINSFamilyReportHistory.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 17724 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyReportHistory });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LLINSFamilyReport query and add missing value', () => {
        const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 456 };
        const llinsFamilyReport: ILLINSFamilyReport = { id: 34861 };
        lLINSFamilyReportHistory.llinsFamilyReport = llinsFamilyReport;

        const lLINSFamilyReportCollection: ILLINSFamilyReport[] = [{ id: 4412 }];
        jest.spyOn(lLINSFamilyReportService, 'query').mockReturnValue(of(new HttpResponse({ body: lLINSFamilyReportCollection })));
        const additionalLLINSFamilyReports = [llinsFamilyReport];
        const expectedCollection: ILLINSFamilyReport[] = [...additionalLLINSFamilyReports, ...lLINSFamilyReportCollection];
        jest.spyOn(lLINSFamilyReportService, 'addLLINSFamilyReportToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSFamilyReportHistory });
        comp.ngOnInit();

        expect(lLINSFamilyReportService.query).toHaveBeenCalled();
        expect(lLINSFamilyReportService.addLLINSFamilyReportToCollectionIfMissing).toHaveBeenCalledWith(
          lLINSFamilyReportCollection,
          ...additionalLLINSFamilyReports
        );
        expect(comp.lLINSFamilyReportsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lLINSFamilyReportHistory: ILLINSFamilyReportHistory = { id: 456 };
        const createdBy: IUser = { id: 54858 };
        lLINSFamilyReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 20016 };
        lLINSFamilyReportHistory.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 45590 };
        lLINSFamilyReportHistory.dayReached = dayReached;
        const llinsFamilyReport: ILLINSFamilyReport = { id: 49191 };
        lLINSFamilyReportHistory.llinsFamilyReport = llinsFamilyReport;

        activatedRoute.data = of({ lLINSFamilyReportHistory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lLINSFamilyReportHistory));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.lLINSFamilyReportsSharedCollection).toContain(llinsFamilyReport);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyReportHistory>>();
        const lLINSFamilyReportHistory = { id: 123 };
        jest.spyOn(lLINSFamilyReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSFamilyReportHistory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lLINSFamilyReportHistoryService.update).toHaveBeenCalledWith(lLINSFamilyReportHistory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyReportHistory>>();
        const lLINSFamilyReportHistory = new LLINSFamilyReportHistory();
        jest.spyOn(lLINSFamilyReportHistoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSFamilyReportHistory }));
        saveSubject.complete();

        // THEN
        expect(lLINSFamilyReportHistoryService.create).toHaveBeenCalledWith(lLINSFamilyReportHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSFamilyReportHistory>>();
        const lLINSFamilyReportHistory = { id: 123 };
        jest.spyOn(lLINSFamilyReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSFamilyReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lLINSFamilyReportHistoryService.update).toHaveBeenCalledWith(lLINSFamilyReportHistory);
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

      describe('trackLLINSFamilyReportById', () => {
        it('Should return tracked LLINSFamilyReport primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLLINSFamilyReportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
