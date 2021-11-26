jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LlinsVillageReportHistoryService } from '../service/llins-village-report-history.service';
import { ILlinsVillageReportHistory, LlinsVillageReportHistory } from '../llins-village-report-history.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsVillageReport } from 'app/entities/llins-village-report/llins-village-report.model';
import { LlinsVillageReportService } from 'app/entities/llins-village-report/service/llins-village-report.service';

import { LlinsVillageReportHistoryUpdateComponent } from './llins-village-report-history-update.component';

describe('Component Tests', () => {
  describe('LlinsVillageReportHistory Management Update Component', () => {
    let comp: LlinsVillageReportHistoryUpdateComponent;
    let fixture: ComponentFixture<LlinsVillageReportHistoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let llinsVillageReportHistoryService: LlinsVillageReportHistoryService;
    let userService: UserService;
    let workingDayService: WorkingDayService;
    let llinsVillageReportService: LlinsVillageReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LlinsVillageReportHistoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LlinsVillageReportHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LlinsVillageReportHistoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      llinsVillageReportHistoryService = TestBed.inject(LlinsVillageReportHistoryService);
      userService = TestBed.inject(UserService);
      workingDayService = TestBed.inject(WorkingDayService);
      llinsVillageReportService = TestBed.inject(LlinsVillageReportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 456 };
        const createdBy: IUser = { id: 16958 };
        llinsVillageReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 98025 };
        llinsVillageReportHistory.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 9434 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsVillageReportHistory });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call WorkingDay query and add missing value', () => {
        const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 456 };
        const dayReached: IWorkingDay = { id: 2835 };
        llinsVillageReportHistory.dayReached = dayReached;

        const workingDayCollection: IWorkingDay[] = [{ id: 50889 }];
        jest.spyOn(workingDayService, 'query').mockReturnValue(of(new HttpResponse({ body: workingDayCollection })));
        const additionalWorkingDays = [dayReached];
        const expectedCollection: IWorkingDay[] = [...additionalWorkingDays, ...workingDayCollection];
        jest.spyOn(workingDayService, 'addWorkingDayToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsVillageReportHistory });
        comp.ngOnInit();

        expect(workingDayService.query).toHaveBeenCalled();
        expect(workingDayService.addWorkingDayToCollectionIfMissing).toHaveBeenCalledWith(workingDayCollection, ...additionalWorkingDays);
        expect(comp.workingDaysSharedCollection).toEqual(expectedCollection);
      });

      it('Should call LlinsVillageReport query and add missing value', () => {
        const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 456 };
        const llinsVillageReport: ILlinsVillageReport = { id: 38768 };
        llinsVillageReportHistory.llinsVillageReport = llinsVillageReport;

        const llinsVillageReportCollection: ILlinsVillageReport[] = [{ id: 67238 }];
        jest.spyOn(llinsVillageReportService, 'query').mockReturnValue(of(new HttpResponse({ body: llinsVillageReportCollection })));
        const additionalLlinsVillageReports = [llinsVillageReport];
        const expectedCollection: ILlinsVillageReport[] = [...additionalLlinsVillageReports, ...llinsVillageReportCollection];
        jest.spyOn(llinsVillageReportService, 'addLlinsVillageReportToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ llinsVillageReportHistory });
        comp.ngOnInit();

        expect(llinsVillageReportService.query).toHaveBeenCalled();
        expect(llinsVillageReportService.addLlinsVillageReportToCollectionIfMissing).toHaveBeenCalledWith(
          llinsVillageReportCollection,
          ...additionalLlinsVillageReports
        );
        expect(comp.llinsVillageReportsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const llinsVillageReportHistory: ILlinsVillageReportHistory = { id: 456 };
        const createdBy: IUser = { id: 78659 };
        llinsVillageReportHistory.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 98409 };
        llinsVillageReportHistory.lastUpdatedBy = lastUpdatedBy;
        const dayReached: IWorkingDay = { id: 13195 };
        llinsVillageReportHistory.dayReached = dayReached;
        const llinsVillageReport: ILlinsVillageReport = { id: 15930 };
        llinsVillageReportHistory.llinsVillageReport = llinsVillageReport;

        activatedRoute.data = of({ llinsVillageReportHistory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(llinsVillageReportHistory));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.workingDaysSharedCollection).toContain(dayReached);
        expect(comp.llinsVillageReportsSharedCollection).toContain(llinsVillageReport);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsVillageReportHistory>>();
        const llinsVillageReportHistory = { id: 123 };
        jest.spyOn(llinsVillageReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsVillageReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsVillageReportHistory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(llinsVillageReportHistoryService.update).toHaveBeenCalledWith(llinsVillageReportHistory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsVillageReportHistory>>();
        const llinsVillageReportHistory = new LlinsVillageReportHistory();
        jest.spyOn(llinsVillageReportHistoryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsVillageReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: llinsVillageReportHistory }));
        saveSubject.complete();

        // THEN
        expect(llinsVillageReportHistoryService.create).toHaveBeenCalledWith(llinsVillageReportHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LlinsVillageReportHistory>>();
        const llinsVillageReportHistory = { id: 123 };
        jest.spyOn(llinsVillageReportHistoryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ llinsVillageReportHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(llinsVillageReportHistoryService.update).toHaveBeenCalledWith(llinsVillageReportHistory);
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

      describe('trackLlinsVillageReportById', () => {
        it('Should return tracked LlinsVillageReport primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLlinsVillageReportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
