jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';
import { ICHVMalariaReportVersion1, CHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';

import { CHVMalariaReportVersion1UpdateComponent } from './chv-malaria-report-version-1-update.component';

describe('Component Tests', () => {
  describe('CHVMalariaReportVersion1 Management Update Component', () => {
    let comp: CHVMalariaReportVersion1UpdateComponent;
    let fixture: ComponentFixture<CHVMalariaReportVersion1UpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cHVMalariaReportVersion1Service: CHVMalariaReportVersion1Service;
    let userService: UserService;
    let cHVService: CHVService;
    let periodService: PeriodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVMalariaReportVersion1UpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CHVMalariaReportVersion1UpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVMalariaReportVersion1UpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cHVMalariaReportVersion1Service = TestBed.inject(CHVMalariaReportVersion1Service);
      userService = TestBed.inject(UserService);
      cHVService = TestBed.inject(CHVService);
      periodService = TestBed.inject(PeriodService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 456 };
        const createdBy: IUser = { id: 24703 };
        cHVMalariaReportVersion1.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 50752 };
        cHVMalariaReportVersion1.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 80360 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVMalariaReportVersion1 });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CHV query and add missing value', () => {
        const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 456 };
        const chv: ICHV = { id: 45406 };
        cHVMalariaReportVersion1.chv = chv;

        const cHVCollection: ICHV[] = [{ id: 92783 }];
        jest.spyOn(cHVService, 'query').mockReturnValue(of(new HttpResponse({ body: cHVCollection })));
        const additionalCHVS = [chv];
        const expectedCollection: ICHV[] = [...additionalCHVS, ...cHVCollection];
        jest.spyOn(cHVService, 'addCHVToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVMalariaReportVersion1 });
        comp.ngOnInit();

        expect(cHVService.query).toHaveBeenCalled();
        expect(cHVService.addCHVToCollectionIfMissing).toHaveBeenCalledWith(cHVCollection, ...additionalCHVS);
        expect(comp.cHVSSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Period query and add missing value', () => {
        const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 456 };
        const period: IPeriod = { id: 88875 };
        cHVMalariaReportVersion1.period = period;

        const periodCollection: IPeriod[] = [{ id: 91383 }];
        jest.spyOn(periodService, 'query').mockReturnValue(of(new HttpResponse({ body: periodCollection })));
        const additionalPeriods = [period];
        const expectedCollection: IPeriod[] = [...additionalPeriods, ...periodCollection];
        jest.spyOn(periodService, 'addPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVMalariaReportVersion1 });
        comp.ngOnInit();

        expect(periodService.query).toHaveBeenCalled();
        expect(periodService.addPeriodToCollectionIfMissing).toHaveBeenCalledWith(periodCollection, ...additionalPeriods);
        expect(comp.periodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cHVMalariaReportVersion1: ICHVMalariaReportVersion1 = { id: 456 };
        const createdBy: IUser = { id: 2173 };
        cHVMalariaReportVersion1.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 628 };
        cHVMalariaReportVersion1.lastUpdatedBy = lastUpdatedBy;
        const chv: ICHV = { id: 84182 };
        cHVMalariaReportVersion1.chv = chv;
        const period: IPeriod = { id: 46926 };
        cHVMalariaReportVersion1.period = period;

        activatedRoute.data = of({ cHVMalariaReportVersion1 });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cHVMalariaReportVersion1));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.cHVSSharedCollection).toContain(chv);
        expect(comp.periodsSharedCollection).toContain(period);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVMalariaReportVersion1>>();
        const cHVMalariaReportVersion1 = { id: 123 };
        jest.spyOn(cHVMalariaReportVersion1Service, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVMalariaReportVersion1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHVMalariaReportVersion1 }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cHVMalariaReportVersion1Service.update).toHaveBeenCalledWith(cHVMalariaReportVersion1);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVMalariaReportVersion1>>();
        const cHVMalariaReportVersion1 = new CHVMalariaReportVersion1();
        jest.spyOn(cHVMalariaReportVersion1Service, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVMalariaReportVersion1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHVMalariaReportVersion1 }));
        saveSubject.complete();

        // THEN
        expect(cHVMalariaReportVersion1Service.create).toHaveBeenCalledWith(cHVMalariaReportVersion1);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVMalariaReportVersion1>>();
        const cHVMalariaReportVersion1 = { id: 123 };
        jest.spyOn(cHVMalariaReportVersion1Service, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVMalariaReportVersion1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cHVMalariaReportVersion1Service.update).toHaveBeenCalledWith(cHVMalariaReportVersion1);
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

      describe('trackCHVById', () => {
        it('Should return tracked CHV primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCHVById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPeriodById', () => {
        it('Should return tracked Period primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPeriodById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
