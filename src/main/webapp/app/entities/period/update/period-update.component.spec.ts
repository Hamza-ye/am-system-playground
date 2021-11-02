jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PeriodService } from '../service/period.service';
import { IPeriod, Period } from '../period.model';
import { IPeriodType } from 'app/entities/period-type/period-type.model';
import { PeriodTypeService } from 'app/entities/period-type/service/period-type.service';

import { PeriodUpdateComponent } from './period-update.component';

describe('Component Tests', () => {
  describe('Period Management Update Component', () => {
    let comp: PeriodUpdateComponent;
    let fixture: ComponentFixture<PeriodUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let periodService: PeriodService;
    let periodTypeService: PeriodTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeriodUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PeriodUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeriodUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      periodService = TestBed.inject(PeriodService);
      periodTypeService = TestBed.inject(PeriodTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PeriodType query and add missing value', () => {
        const period: IPeriod = { id: 456 };
        const periodType: IPeriodType = { id: 90147 };
        period.periodType = periodType;

        const periodTypeCollection: IPeriodType[] = [{ id: 88666 }];
        jest.spyOn(periodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: periodTypeCollection })));
        const additionalPeriodTypes = [periodType];
        const expectedCollection: IPeriodType[] = [...additionalPeriodTypes, ...periodTypeCollection];
        jest.spyOn(periodTypeService, 'addPeriodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ period });
        comp.ngOnInit();

        expect(periodTypeService.query).toHaveBeenCalled();
        expect(periodTypeService.addPeriodTypeToCollectionIfMissing).toHaveBeenCalledWith(periodTypeCollection, ...additionalPeriodTypes);
        expect(comp.periodTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const period: IPeriod = { id: 456 };
        const periodType: IPeriodType = { id: 78071 };
        period.periodType = periodType;

        activatedRoute.data = of({ period });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(period));
        expect(comp.periodTypesSharedCollection).toContain(periodType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Period>>();
        const period = { id: 123 };
        jest.spyOn(periodService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ period });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: period }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(periodService.update).toHaveBeenCalledWith(period);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Period>>();
        const period = new Period();
        jest.spyOn(periodService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ period });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: period }));
        saveSubject.complete();

        // THEN
        expect(periodService.create).toHaveBeenCalledWith(period);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Period>>();
        const period = { id: 123 };
        jest.spyOn(periodService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ period });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(periodService.update).toHaveBeenCalledWith(period);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPeriodTypeById', () => {
        it('Should return tracked PeriodType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPeriodTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
