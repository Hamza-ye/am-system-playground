jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChvMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';
import { IChvMalariaReportVersion1, ChvMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IChv } from 'app/entities/chv/chv.model';
import { ChvService } from 'app/entities/chv/service/chv.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';

import { ChvMalariaReportVersion1UpdateComponent } from './chv-malaria-report-version-1-update.component';

describe('Component Tests', () => {
  describe('ChvMalariaReportVersion1 Management Update Component', () => {
    let comp: ChvMalariaReportVersion1UpdateComponent;
    let fixture: ComponentFixture<ChvMalariaReportVersion1UpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let chvMalariaReportVersion1Service: ChvMalariaReportVersion1Service;
    let userService: UserService;
    let chvService: ChvService;
    let periodService: PeriodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvMalariaReportVersion1UpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChvMalariaReportVersion1UpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvMalariaReportVersion1UpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      chvMalariaReportVersion1Service = TestBed.inject(ChvMalariaReportVersion1Service);
      userService = TestBed.inject(UserService);
      chvService = TestBed.inject(ChvService);
      periodService = TestBed.inject(PeriodService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 456 };
        const createdBy: IUser = { id: 97266 };
        chvMalariaReportVersion1.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 35297 };
        chvMalariaReportVersion1.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 42546 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvMalariaReportVersion1 });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Chv query and add missing value', () => {
        const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 456 };
        const chv: IChv = { id: 44154 };
        chvMalariaReportVersion1.chv = chv;

        const chvCollection: IChv[] = [{ id: 52841 }];
        jest.spyOn(chvService, 'query').mockReturnValue(of(new HttpResponse({ body: chvCollection })));
        const additionalChvs = [chv];
        const expectedCollection: IChv[] = [...additionalChvs, ...chvCollection];
        jest.spyOn(chvService, 'addChvToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvMalariaReportVersion1 });
        comp.ngOnInit();

        expect(chvService.query).toHaveBeenCalled();
        expect(chvService.addChvToCollectionIfMissing).toHaveBeenCalledWith(chvCollection, ...additionalChvs);
        expect(comp.chvsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Period query and add missing value', () => {
        const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 456 };
        const period: IPeriod = { id: 84183 };
        chvMalariaReportVersion1.period = period;

        const periodCollection: IPeriod[] = [{ id: 53882 }];
        jest.spyOn(periodService, 'query').mockReturnValue(of(new HttpResponse({ body: periodCollection })));
        const additionalPeriods = [period];
        const expectedCollection: IPeriod[] = [...additionalPeriods, ...periodCollection];
        jest.spyOn(periodService, 'addPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvMalariaReportVersion1 });
        comp.ngOnInit();

        expect(periodService.query).toHaveBeenCalled();
        expect(periodService.addPeriodToCollectionIfMissing).toHaveBeenCalledWith(periodCollection, ...additionalPeriods);
        expect(comp.periodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const chvMalariaReportVersion1: IChvMalariaReportVersion1 = { id: 456 };
        const createdBy: IUser = { id: 60235 };
        chvMalariaReportVersion1.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 64333 };
        chvMalariaReportVersion1.lastUpdatedBy = lastUpdatedBy;
        const chv: IChv = { id: 31726 };
        chvMalariaReportVersion1.chv = chv;
        const period: IPeriod = { id: 47278 };
        chvMalariaReportVersion1.period = period;

        activatedRoute.data = of({ chvMalariaReportVersion1 });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(chvMalariaReportVersion1));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.chvsSharedCollection).toContain(chv);
        expect(comp.periodsSharedCollection).toContain(period);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvMalariaReportVersion1>>();
        const chvMalariaReportVersion1 = { id: 123 };
        jest.spyOn(chvMalariaReportVersion1Service, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvMalariaReportVersion1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chvMalariaReportVersion1 }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(chvMalariaReportVersion1Service.update).toHaveBeenCalledWith(chvMalariaReportVersion1);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvMalariaReportVersion1>>();
        const chvMalariaReportVersion1 = new ChvMalariaReportVersion1();
        jest.spyOn(chvMalariaReportVersion1Service, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvMalariaReportVersion1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chvMalariaReportVersion1 }));
        saveSubject.complete();

        // THEN
        expect(chvMalariaReportVersion1Service.create).toHaveBeenCalledWith(chvMalariaReportVersion1);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvMalariaReportVersion1>>();
        const chvMalariaReportVersion1 = { id: 123 };
        jest.spyOn(chvMalariaReportVersion1Service, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvMalariaReportVersion1 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(chvMalariaReportVersion1Service.update).toHaveBeenCalledWith(chvMalariaReportVersion1);
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

      describe('trackChvById', () => {
        it('Should return tracked Chv primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackChvById(0, entity);
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
