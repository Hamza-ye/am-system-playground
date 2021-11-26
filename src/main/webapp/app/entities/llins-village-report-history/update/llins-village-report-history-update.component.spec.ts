jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LLINSVillageReportHistoryService } from '../service/llins-village-report-history.service';
import { ILLINSVillageReportHistory, LLINSVillageReportHistory } from '../llins-village-report-history.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSVillageReport } from 'app/entities/llins-village-report/llins-village-report.model';
import { LLINSVillageReportService } from 'app/entities/llins-village-report/service/llins-village-report.service';

import { LLINSVillageReportHistoryUpdateComponent } from './llins-village-report-history-update.component';

describe('Component Tests', () => {
  describe('LLINSVillageReportHistory Management Update Component', () => {
    let comp: LLINSVillageReportHistoryUpdateComponent;
    let fixture: ComponentFixture<LLINSVillageReportHistoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lLINSVillageReportHistoryService: LLINSVillageReportHistoryService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let lLINSVillageReportService: LLINSVillageReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LLINSVillageReportHistoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LLINSVillageReportHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LLINSVillageReportHistoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lLINSVillageReportHistoryService = TestBed.inject(LLINSVillageReportHistoryService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      lLINSVillageReportService = TestBed.inject(LLINSVillageReportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 456 };
        const createdBy: IUser = { id: 61056 };
        lLINSVillageReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 34837 };
        lLINSVillageReportHistory.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 99170 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageReportHistory });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 456 };
        const dayReached: IWorkingDay = { id: 32528 };
        lLINSVillageReportHistory.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 98181 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageReportHistory });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LLINSVillageReport query and add missing value', () => {
        const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 456 };
        const llinsVillageReport: ILLINSVillageReport = { id: 46685 };
        lLINSVillageReportHistory.llinsVillageReport = llinsVillageReport;

        const lLINSVillageReportCollection: ILLINSVillageReport[] = [{ id: 43852 }];
        jest.spyOn(lLINSVillageReportService, 'query').mockReturnValue(of(new HttpResponse({ body: lLINSVillageReportCollection })));
        const additionalLLINSVillageReports = [llinsVillageReport];
        const expectedCollection: ILLINSVillageReport[] = [...additionalLLINSVillageReports, ...lLINSVillageReportCollection];
        jest.spyOn(lLINSVillageReportService, 'addLLINSVillageReportToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ lLINSVillageReportHistory });
        comp.ngOnInit();

        expect(lLINSVillageReportService.query).toHaveBeenCalled();
        expect(lLINSVillageReportService.addLLINSVillageReportToCollectionIfMissing).toHaveBeenCalledWith(
          lLINSVillageReportCollection,
          ...additionalLLINSVillageReports
        );
        expect(comp.lLINSVillageReportsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const lLINSVillageReportHistory: ILLINSVillageReportHistory = { id: 456 };
        const createdBy: IUser = { id: 69971 };
        lLINSVillageReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 43182 };
        lLINSVillageReportHistory.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 95777 };
        lLINSVillageReportHistory.dayReached = dayReached;
        const llinsVillageReport: ILLINSVillageReport = { id: 67462 };
        lLINSVillageReportHistory.llinsVillageReport = llinsVillageReport;

        activatedRoute.data = of({ lLINSVillageReportHistory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(lLINSVillageReportHistory));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.lLINSVillageReportsSharedCollection).toContain(llinsVillageReport);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageReportHistory>>();
        const lLINSVillageReportHistory = { id: 123 };
        jest.spyOn(lLINSVillageReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSVillageReportHistory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lLINSVillageReportHistoryService.update).toHaveBeenCalledWith(lLINSVillageReportHistory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageReportHistory>>();
        const lLINSVillageReportHistory = new LLINSVillageReportHistory();
        jest.spyOn(lLINSVillageReportHistoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: lLINSVillageReportHistory }));
        saveSubject.complete();

        // THEN
        expect(lLINSVillageReportHistoryService.create).toHaveBeenCalledWith(lLINSVillageReportHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LLINSVillageReportHistory>>();
        const lLINSVillageReportHistory = { id: 123 };
        jest.spyOn(lLINSVillageReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ lLINSVillageReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lLINSVillageReportHistoryService.update).toHaveBeenCalledWith(lLINSVillageReportHistory);
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

      describe('trackLLINSVillageReportById', () => {
        it('Should return tracked LLINSVillageReport primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLLINSVillageReportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
