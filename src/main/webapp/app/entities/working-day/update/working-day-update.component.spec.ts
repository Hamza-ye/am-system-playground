jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkingDayService } from '../service/working-day.service';
import { IWorkingDay, WorkingDay } from '../working-day.model';

import { WorkingDayUpdateComponent } from './working-day-update.component';

describe('Component Tests', () => {
  describe('WorkingDay Management Update Component', () => {
    let comp: WorkingDayUpdateComponent;
    let fixture: ComponentFixture<WorkingDayUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workingDayService: WorkingDayService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkingDayUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkingDayUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkingDayUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workingDayService = TestBed.inject(WorkingDayService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const workingDay: IWorkingDay = { id: 456 };

        activatedRoute.data = of({ workingDay });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workingDay));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WorkingDay>>();
        const workingDay = { id: 123 };
        jest.spyOn(workingDayService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingDay });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workingDay }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workingDayService.update).toHaveBeenCalledWith(workingDay);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WorkingDay>>();
        const workingDay = new WorkingDay();
        jest.spyOn(workingDayService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingDay });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workingDay }));
        saveSubject.complete();

        // THEN
        expect(workingDayService.create).toHaveBeenCalledWith(workingDay);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<WorkingDay>>();
        const workingDay = { id: 123 };
        jest.spyOn(workingDayService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingDay });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workingDayService.update).toHaveBeenCalledWith(workingDay);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
