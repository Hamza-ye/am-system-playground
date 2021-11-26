jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FamilyService } from '../service/family.service';
import { IFamily, Family } from '../family.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { FamilyUpdateComponent } from './family-update.component';

describe('Component Tests', () => {
  describe('Family Management Update Component', () => {
    let comp: FamilyUpdateComponent;
    let fixture: ComponentFixture<FamilyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let familyService: FamilyService;
    let organisationUnitService: OrganisationUnitService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FamilyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FamilyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FamilyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      familyService = TestBed.inject(FamilyService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call OrganisationUnit query and add missing value', () => {
        const family: IFamily = { id: 456 };
        const organisationUnit: IOrganisationUnit = { id: 7504 };
        family.organisationUnit = organisationUnit;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 26838 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [organisationUnit];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ family });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const family: IFamily = { id: 456 };
        const createdBy: IUser = { id: 92589 };
        family.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 71283 };
        family.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 86993 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ family });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const family: IFamily = { id: 456 };
        const organisationUnit: IOrganisationUnit = { id: 55856 };
        family.organisationUnit = organisationUnit;
        const createdBy: IUser = { id: 90333 };
        family.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 5764 };
        family.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ family });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(family));
        expect(comp.organisationUnitsSharedCollection).toContain(organisationUnit);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Family>>();
        const family = { id: 123 };
        jest.spyOn(familyService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ family });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: family }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(familyService.update).toHaveBeenCalledWith(family);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Family>>();
        const family = new Family();
        jest.spyOn(familyService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ family });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: family }));
        saveSubject.complete();

        // THEN
        expect(familyService.create).toHaveBeenCalledWith(family);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Family>>();
        const family = { id: 123 };
        jest.spyOn(familyService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ family });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(familyService.update).toHaveBeenCalledWith(family);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrganisationUnitById', () => {
        it('Should return tracked OrganisationUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganisationUnitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
