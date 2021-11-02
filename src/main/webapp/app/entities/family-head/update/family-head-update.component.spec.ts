jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FamilyHeadService } from '../service/family-head.service';
import { IFamilyHead, FamilyHead } from '../family-head.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';

import { FamilyHeadUpdateComponent } from './family-head-update.component';

describe('Component Tests', () => {
  describe('FamilyHead Management Update Component', () => {
    let comp: FamilyHeadUpdateComponent;
    let fixture: ComponentFixture<FamilyHeadUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let familyHeadService: FamilyHeadService;
    let userService: UserService;
    let familyService: FamilyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FamilyHeadUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FamilyHeadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FamilyHeadUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      familyHeadService = TestBed.inject(FamilyHeadService);
      userService = TestBed.inject(UserService);
      familyService = TestBed.inject(FamilyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const familyHead: IFamilyHead = { id: 456 };
        const user: IUser = { id: 60345 };
        familyHead.user = user;
        const lastUpdatedBy: IUser = { id: 42910 };
        familyHead.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 40836 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ familyHead });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Family query and add missing value', () => {
        const familyHead: IFamilyHead = { id: 456 };
        const family: IFamily = { id: 83997 };
        familyHead.family = family;

        const familyCollection: IFamily[] = [{ id: 47761 }];
        jest.spyOn(familyService, 'query').mockReturnValue(of(new HttpResponse({ body: familyCollection })));
        const additionalFamilies = [family];
        const expectedCollection: IFamily[] = [...additionalFamilies, ...familyCollection];
        jest.spyOn(familyService, 'addFamilyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ familyHead });
        comp.ngOnInit();

        expect(familyService.query).toHaveBeenCalled();
        expect(familyService.addFamilyToCollectionIfMissing).toHaveBeenCalledWith(familyCollection, ...additionalFamilies);
        expect(comp.familiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const familyHead: IFamilyHead = { id: 456 };
        const user: IUser = { id: 96437 };
        familyHead.user = user;
        const lastUpdatedBy: IUser = { id: 2052 };
        familyHead.lastUpdatedBy = lastUpdatedBy;
        const family: IFamily = { id: 31706 };
        familyHead.family = family;

        activatedRoute.data = of({ familyHead });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(familyHead));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.familiesSharedCollection).toContain(family);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FamilyHead>>();
        const familyHead = { id: 123 };
        jest.spyOn(familyHeadService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ familyHead });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: familyHead }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(familyHeadService.update).toHaveBeenCalledWith(familyHead);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FamilyHead>>();
        const familyHead = new FamilyHead();
        jest.spyOn(familyHeadService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ familyHead });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: familyHead }));
        saveSubject.complete();

        // THEN
        expect(familyHeadService.create).toHaveBeenCalledWith(familyHead);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FamilyHead>>();
        const familyHead = { id: 123 };
        jest.spyOn(familyHeadService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ familyHead });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(familyHeadService.update).toHaveBeenCalledWith(familyHead);
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

      describe('trackFamilyById', () => {
        it('Should return tracked Family primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFamilyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
