jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LlinsFamilyReportHistoryService } from '../service/llins-family-report-history.service';
import { ILlinsFamilyReportHistory, LlinsFamilyReportHistory } from '../llins-family-report-history.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsFamilyReport } from 'app/entities/llins-family-report/llins-family-report.model';
import { LlinsFamilyReportService } from 'app/entities/llins-family-report/service/llins-family-report.service';

import { LlinsFamilyReportHistoryUpdateComponent } from './llins-family-report-history-update.component';

describe('Component Tests', () => {
  describe('LlinsFamilyReportHistory Management Update Component', () => {
    let comp: LlinsFamilyReportHistoryUpdateComponent;
    let fixture: ComponentFixture<LlinsFamilyReportHistoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let llinsFamilyReportHistoryService: LlinsFamilyReportHistoryService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let llinsFamilyReportService: LlinsFamilyReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsFamilyReportHistoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LlinsFamilyReportHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsFamilyReportHistoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      llinsFamilyReportHistoryService = TestBed.inject(LlinsFamilyReportHistoryService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      llinsFamilyReportService = TestBed.inject(LlinsFamilyReportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 456 };
        const createdBy: IUser = { id: 68963 };
        llinsFamilyReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 78926 };
        llinsFamilyReportHistory.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 18567 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyReportHistory });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 456 };
        const dayReached: IWorkingDay = { id: 58313 };
        llinsFamilyReportHistory.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 4269 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyReportHistory });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LlinsFamilyReport query and add missing value', () => {
        const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 456 };
        const llinsFamilyReport: ILlinsFamilyReport = { id: 11264 };
        llinsFamilyReportHistory.llinsFamilyReport = llinsFamilyReport;

        const llinsFamilyReportCollection: ILlinsFamilyReport[] = [{ id: 48873 }];
        jest.spyOn(llinsFamilyReportService, 'query').mockReturnValue(of(new HttpResponse({ body: llinsFamilyReportCollection })));
        const additionalLlinsFamilyReports = [llinsFamilyReport];
        const expectedCollection: ILlinsFamilyReport[] = [...additionalLlinsFamilyReports, ...llinsFamilyReportCollection];
        jest.spyOn(llinsFamilyReportService, 'addLlinsFamilyReportToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsFamilyReportHistory });
        comp.ngOnInit();

        expect(llinsFamilyReportService.query).toHaveBeenCalled();
        expect(llinsFamilyReportService.addLlinsFamilyReportToCollectionIfMissing).toHaveBeenCalledWith(
          llinsFamilyReportCollection,
          ...additionalLlinsFamilyReports
        );
        expect(comp.llinsFamilyReportsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const llinsFamilyReportHistory: ILlinsFamilyReportHistory = { id: 456 };
        const createdBy: IUser = { id: 25713 };
        llinsFamilyReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 79131 };
        llinsFamilyReportHistory.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 68340 };
        llinsFamilyReportHistory.dayReached = dayReached;
        const llinsFamilyReport: ILlinsFamilyReport = { id: 86701 };
        llinsFamilyReportHistory.llinsFamilyReport = llinsFamilyReport;

        activatedRoute.data = of({ llinsFamilyReportHistory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(llinsFamilyReportHistory));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.llinsFamilyReportsSharedCollection).toContain(llinsFamilyReport);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyReportHistory>>();
        const llinsFamilyReportHistory = { id: 123 };
        jest.spyOn(llinsFamilyReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsFamilyReportHistory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(llinsFamilyReportHistoryService.update).toHaveBeenCalledWith(llinsFamilyReportHistory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyReportHistory>>();
        const llinsFamilyReportHistory = new LlinsFamilyReportHistory();
        jest.spyOn(llinsFamilyReportHistoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsFamilyReportHistory }));
        saveSubject.complete();

        // THEN
        expect(llinsFamilyReportHistoryService.create).toHaveBeenCalledWith(llinsFamilyReportHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsFamilyReportHistory>>();
        const llinsFamilyReportHistory = { id: 123 };
        jest.spyOn(llinsFamilyReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsFamilyReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(llinsFamilyReportHistoryService.update).toHaveBeenCalledWith(llinsFamilyReportHistory);
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

      describe('trackLlinsFamilyReportById', () => {
        it('Should return tracked LlinsFamilyReport primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLlinsFamilyReportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
