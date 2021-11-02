jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganisationUnitGroupService } from '../service/organisation-unit-group.service';
import { IOrganisationUnitGroup, OrganisationUnitGroup } from '../organisation-unit-group.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { OrganisationUnitGroupUpdateComponent } from './organisation-unit-group-update.component';

describe('Component Tests', () => {
  describe('OrganisationUnitGroup Management Update Component', () => {
    let comp: OrganisationUnitGroupUpdateComponent;
    let fixture: ComponentFixture<OrganisationUnitGroupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organisationUnitGroupService: OrganisationUnitGroupService;
    let userService: UserService;
    let organisationUnitService: OrganisationUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitGroupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganisationUnitGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitGroupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organisationUnitGroupService = TestBed.inject(OrganisationUnitGroupService);
      userService = TestBed.inject(UserService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const organisationUnitGroup: IOrganisationUnitGroup = { id: 456 };
        const user: IUser = { id: 30633 };
        organisationUnitGroup.user = user;
        const lastUpdatedBy: IUser = { id: 40879 };
        organisationUnitGroup.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 44343 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnitGroup });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnit query and add missing value', () => {
        const organisationUnitGroup: IOrganisationUnitGroup = { id: 456 };
        const members: IOrganisationUnit[] = [{ id: 55028 }];
        organisationUnitGroup.members = members;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 65531 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [...members];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnitGroup });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const organisationUnitGroup: IOrganisationUnitGroup = { id: 456 };
        const user: IUser = { id: 37351 };
        organisationUnitGroup.user = user;
        const lastUpdatedBy: IUser = { id: 83362 };
        organisationUnitGroup.lastUpdatedBy = lastUpdatedBy;
        const members: IOrganisationUnit = { id: 94922 };
        organisationUnitGroup.members = [members];

        activatedRoute.data = of({ organisationUnitGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organisationUnitGroup));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.organisationUnitsSharedCollection).toContain(members);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitGroup>>();
        const organisationUnitGroup = { id: 123 };
        jest.spyOn(organisationUnitGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnitGroup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organisationUnitGroupService.update).toHaveBeenCalledWith(organisationUnitGroup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitGroup>>();
        const organisationUnitGroup = new OrganisationUnitGroup();
        jest.spyOn(organisationUnitGroupService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnitGroup }));
        saveSubject.complete();

        // THEN
        expect(organisationUnitGroupService.create).toHaveBeenCalledWith(organisationUnitGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitGroup>>();
        const organisationUnitGroup = { id: 123 };
        jest.spyOn(organisationUnitGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organisationUnitGroupService.update).toHaveBeenCalledWith(organisationUnitGroup);
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

      describe('trackOrganisationUnitById', () => {
        it('Should return tracked OrganisationUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganisationUnitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedOrganisationUnit', () => {
        it('Should return option if no OrganisationUnit is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedOrganisationUnit(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected OrganisationUnit for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedOrganisationUnit(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this OrganisationUnit is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedOrganisationUnit(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
