jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DataInputPeriodService } from '../service/data-input-period.service';
import { IDataInputPeriod, DataInputPeriod } from '../data-input-period.model';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';

import { DataInputPeriodUpdateComponent } from './data-input-period-update.component';

describe('Component Tests', () => {
  describe('DataInputPeriod Management Update Component', () => {
    let comp: DataInputPeriodUpdateComponent;
    let fixture: ComponentFixture<DataInputPeriodUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dataInputPeriodService: DataInputPeriodService;
    let periodService: PeriodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DataInputPeriodUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DataInputPeriodUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataInputPeriodUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dataInputPeriodService = TestBed.inject(DataInputPeriodService);
      periodService = TestBed.inject(PeriodService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Period query and add missing value', () => {
        const dataInputPeriod: IDataInputPeriod = { id: 456 };
        const period: IPeriod = { id: 63372 };
        dataInputPeriod.period = period;

        const periodCollection: IPeriod[] = [{ id: 76546 }];
        jest.spyOn(periodService, 'query').mockReturnValue(of(new HttpResponse({ body: periodCollection })));
        const additionalPeriods = [period];
        const expectedCollection: IPeriod[] = [...additionalPeriods, ...periodCollection];
        jest.spyOn(periodService, 'addPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dataInputPeriod });
        comp.ngOnInit();

        expect(periodService.query).toHaveBeenCalled();
        expect(periodService.addPeriodToCollectionIfMissing).toHaveBeenCalledWith(periodCollection, ...additionalPeriods);
        expect(comp.periodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dataInputPeriod: IDataInputPeriod = { id: 456 };
        const period: IPeriod = { id: 24795 };
        dataInputPeriod.period = period;

        activatedRoute.data = of({ dataInputPeriod });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dataInputPeriod));
        expect(comp.periodsSharedCollection).toContain(period);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataInputPeriod>>();
        const dataInputPeriod = { id: 123 };
        jest.spyOn(dataInputPeriodService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataInputPeriod });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dataInputPeriod }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dataInputPeriodService.update).toHaveBeenCalledWith(dataInputPeriod);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataInputPeriod>>();
        const dataInputPeriod = new DataInputPeriod();
        jest.spyOn(dataInputPeriodService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataInputPeriod });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dataInputPeriod }));
        saveSubject.complete();

        // THEN
        expect(dataInputPeriodService.create).toHaveBeenCalledWith(dataInputPeriod);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataInputPeriod>>();
        const dataInputPeriod = { id: 123 };
        jest.spyOn(dataInputPeriodService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataInputPeriod });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dataInputPeriodService.update).toHaveBeenCalledWith(dataInputPeriod);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
