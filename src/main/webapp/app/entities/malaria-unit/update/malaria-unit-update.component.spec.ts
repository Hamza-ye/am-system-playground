jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MalariaUnitService } from '../service/malaria-unit.service';
import { IMalariaUnit, MalariaUnit } from '../malaria-unit.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { MalariaUnitUpdateComponent } from './malaria-unit-update.component';

describe('Component Tests', () => {
  describe('MalariaUnit Management Update Component', () => {
    let comp: MalariaUnitUpdateComponent;
    let fixture: ComponentFixture<MalariaUnitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let malariaUnitService: MalariaUnitService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MalariaUnitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MalariaUnitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MalariaUnitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      malariaUnitService = TestBed.inject(MalariaUnitService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const malariaUnit: IMalariaUnit = { id: 456 };
        const createdBy: IUser = { id: 27356 };
        malariaUnit.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 54507 };
        malariaUnit.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 79624 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ malariaUnit });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const malariaUnit: IMalariaUnit = { id: 456 };
        const createdBy: IUser = { id: 75757 };
        malariaUnit.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 1503 };
        malariaUnit.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ malariaUnit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(malariaUnit));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MalariaUnit>>();
        const malariaUnit = { id: 123 };
        jest.spyOn(malariaUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ malariaUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: malariaUnit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(malariaUnitService.update).toHaveBeenCalledWith(malariaUnit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MalariaUnit>>();
        const malariaUnit = new MalariaUnit();
        jest.spyOn(malariaUnitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ malariaUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: malariaUnit }));
        saveSubject.complete();

        // THEN
        expect(malariaUnitService.create).toHaveBeenCalledWith(malariaUnit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MalariaUnit>>();
        const malariaUnit = { id: 123 };
        jest.spyOn(malariaUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ malariaUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(malariaUnitService.update).toHaveBeenCalledWith(malariaUnit);
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
