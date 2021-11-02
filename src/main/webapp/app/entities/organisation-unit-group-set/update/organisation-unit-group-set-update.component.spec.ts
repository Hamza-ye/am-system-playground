jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganisationUnitGroupSetService } from '../service/organisation-unit-group-set.service';
import { IOrganisationUnitGroupSet, OrganisationUnitGroupSet } from '../organisation-unit-group-set.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnitGroup } from 'app/entities/organisation-unit-group/organisation-unit-group.model';
import { OrganisationUnitGroupService } from 'app/entities/organisation-unit-group/service/organisation-unit-group.service';

import { OrganisationUnitGroupSetUpdateComponent } from './organisation-unit-group-set-update.component';

describe('Component Tests', () => {
  describe('OrganisationUnitGroupSet Management Update Component', () => {
    let comp: OrganisationUnitGroupSetUpdateComponent;
    let fixture: ComponentFixture<OrganisationUnitGroupSetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organisationUnitGroupSetService: OrganisationUnitGroupSetService;
    let userService: UserService;
    let organisationUnitGroupService: OrganisationUnitGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitGroupSetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganisationUnitGroupSetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitGroupSetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organisationUnitGroupSetService = TestBed.inject(OrganisationUnitGroupSetService);
      userService = TestBed.inject(UserService);
      organisationUnitGroupService = TestBed.inject(OrganisationUnitGroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 456 };
        const user: IUser = { id: 54724 };
        organisationUnitGroupSet.user = user;
        const lastUpdatedBy: IUser = { id: 91254 };
        organisationUnitGroupSet.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 15120 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnitGroupSet });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnitGroup query and add missing value', () => {
        const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 456 };
        const organisationUnitGroups: IOrganisationUnitGroup[] = [{ id: 34735 }];
        organisationUnitGroupSet.organisationUnitGroups = organisationUnitGroups;

        const organisationUnitGroupCollection: IOrganisationUnitGroup[] = [{ id: 93950 }];
        jest.spyOn(organisationUnitGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitGroupCollection })));
        const additionalOrganisationUnitGroups = [...organisationUnitGroups];
        const expectedCollection: IOrganisationUnitGroup[] = [...additionalOrganisationUnitGroups, ...organisationUnitGroupCollection];
        jest.spyOn(organisationUnitGroupService, 'addOrganisationUnitGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnitGroupSet });
        comp.ngOnInit();

        expect(organisationUnitGroupService.query).toHaveBeenCalled();
        expect(organisationUnitGroupService.addOrganisationUnitGroupToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitGroupCollection,
          ...additionalOrganisationUnitGroups
        );
        expect(comp.organisationUnitGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const organisationUnitGroupSet: IOrganisationUnitGroupSet = { id: 456 };
        const user: IUser = { id: 69309 };
        organisationUnitGroupSet.user = user;
        const lastUpdatedBy: IUser = { id: 43555 };
        organisationUnitGroupSet.lastUpdatedBy = lastUpdatedBy;
        const organisationUnitGroups: IOrganisationUnitGroup = { id: 51579 };
        organisationUnitGroupSet.organisationUnitGroups = [organisationUnitGroups];

        activatedRoute.data = of({ organisationUnitGroupSet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organisationUnitGroupSet));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.organisationUnitGroupsSharedCollection).toContain(organisationUnitGroups);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitGroupSet>>();
        const organisationUnitGroupSet = { id: 123 };
        jest.spyOn(organisationUnitGroupSetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitGroupSet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnitGroupSet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organisationUnitGroupSetService.update).toHaveBeenCalledWith(organisationUnitGroupSet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitGroupSet>>();
        const organisationUnitGroupSet = new OrganisationUnitGroupSet();
        jest.spyOn(organisationUnitGroupSetService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitGroupSet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnitGroupSet }));
        saveSubject.complete();

        // THEN
        expect(organisationUnitGroupSetService.create).toHaveBeenCalledWith(organisationUnitGroupSet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnitGroupSet>>();
        const organisationUnitGroupSet = { id: 123 };
        jest.spyOn(organisationUnitGroupSetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnitGroupSet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organisationUnitGroupSetService.update).toHaveBeenCalledWith(organisationUnitGroupSet);
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

      describe('trackOrganisationUnitGroupById', () => {
        it('Should return tracked OrganisationUnitGroup primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganisationUnitGroupById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedOrganisationUnitGroup', () => {
        it('Should return option if no OrganisationUnitGroup is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedOrganisationUnitGroup(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected OrganisationUnitGroup for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedOrganisationUnitGroup(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this OrganisationUnitGroup is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedOrganisationUnitGroup(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
