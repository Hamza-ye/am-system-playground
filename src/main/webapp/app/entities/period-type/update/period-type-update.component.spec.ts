jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PeriodTypeService } from '../service/period-type.service';
import { IPeriodType, PeriodType } from '../period-type.model';

import { PeriodTypeUpdateComponent } from './period-type-update.component';

describe('Component Tests', () => {
  describe('PeriodType Management Update Component', () => {
    let comp: PeriodTypeUpdateComponent;
    let fixture: ComponentFixture<PeriodTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let periodTypeService: PeriodTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeriodTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PeriodTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeriodTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      periodTypeService = TestBed.inject(PeriodTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const periodType: IPeriodType = { id: 456 };

        activatedRoute.data = of({ periodType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(periodType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PeriodType>>();
        const periodType = { id: 123 };
        jest.spyOn(periodTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ periodType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: periodType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(periodTypeService.update).toHaveBeenCalledWith(periodType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PeriodType>>();
        const periodType = new PeriodType();
        jest.spyOn(periodTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ periodType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: periodType }));
        saveSubject.complete();

        // THEN
        expect(periodTypeService.create).toHaveBeenCalledWith(periodType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PeriodType>>();
        const periodType = { id: 123 };
        jest.spyOn(periodTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ periodType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(periodTypeService.update).toHaveBeenCalledWith(periodType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
