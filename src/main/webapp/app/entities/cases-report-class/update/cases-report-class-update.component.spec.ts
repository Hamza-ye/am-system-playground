jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CasesReportClassService } from '../service/cases-report-class.service';
import { ICasesReportClass, CasesReportClass } from '../cases-report-class.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CasesReportClassUpdateComponent } from './cases-report-class-update.component';

describe('Component Tests', () => {
  describe('CasesReportClass Management Update Component', () => {
    let comp: CasesReportClassUpdateComponent;
    let fixture: ComponentFixture<CasesReportClassUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let casesReportClassService: CasesReportClassService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CasesReportClassUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CasesReportClassUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CasesReportClassUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      casesReportClassService = TestBed.inject(CasesReportClassService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const casesReportClass: ICasesReportClass = { id: 456 };
        const createdBy: IUser = { id: 96889 };
        casesReportClass.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 41756 };
        casesReportClass.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 5977 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ casesReportClass });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const casesReportClass: ICasesReportClass = { id: 456 };
        const createdBy: IUser = { id: 78500 };
        casesReportClass.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 28535 };
        casesReportClass.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ casesReportClass });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(casesReportClass));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CasesReportClass>>();
        const casesReportClass = { id: 123 };
        jest.spyOn(casesReportClassService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ casesReportClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: casesReportClass }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(casesReportClassService.update).toHaveBeenCalledWith(casesReportClass);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CasesReportClass>>();
        const casesReportClass = new CasesReportClass();
        jest.spyOn(casesReportClassService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ casesReportClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: casesReportClass }));
        saveSubject.complete();

        // THEN
        expect(casesReportClassService.create).toHaveBeenCalledWith(casesReportClass);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CasesReportClass>>();
        const casesReportClass = { id: 123 };
        jest.spyOn(casesReportClassService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ casesReportClass });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(casesReportClassService.update).toHaveBeenCalledWith(casesReportClass);
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
    });
  });
});
