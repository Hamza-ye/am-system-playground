jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonService } from '../service/person.service';
import { IPerson, Person } from '../person.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';
import { IPersonAuthorityGroup } from 'app/entities/person-authority-group/person-authority-group.model';
import { PersonAuthorityGroupService } from 'app/entities/person-authority-group/service/person-authority-group.service';

import { PersonUpdateComponent } from './person-update.component';

describe('Component Tests', () => {
  describe('Person Management Update Component', () => {
    let comp: PersonUpdateComponent;
    let fixture: ComponentFixture<PersonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personService: PersonService;
    let userService: UserService;
    let organisationUnitService: OrganisationUnitService;
    let personAuthorityGroupService: PersonAuthorityGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personService = TestBed.inject(PersonService);
      userService = TestBed.inject(UserService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      personAuthorityGroupService = TestBed.inject(PersonAuthorityGroupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const userInfo: IUser = { id: 57046 };
        person.userInfo = userInfo;
        const createdBy: IUser = { id: 27076 };
        person.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 40784 };
        person.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 6249 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [userInfo, createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnit query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const organisationUnits: IOrganisationUnit[] = [{ id: 11384 }];
        person.organisationUnits = organisationUnits;
        const dataViewOrganisationUnits: IOrganisationUnit[] = [{ id: 29845 }];
        person.dataViewOrganisationUnits = dataViewOrganisationUnits;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 38674 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [...organisationUnits, ...dataViewOrganisationUnits];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PersonAuthorityGroup query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const personAuthorityGroups: IPersonAuthorityGroup[] = [{ id: 61484 }];
        person.personAuthorityGroups = personAuthorityGroups;

        const personAuthorityGroupCollection: IPersonAuthorityGroup[] = [{ id: 86344 }];
        jest.spyOn(personAuthorityGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: personAuthorityGroupCollection })));
        const additionalPersonAuthorityGroups = [...personAuthorityGroups];
        const expectedCollection: IPersonAuthorityGroup[] = [...additionalPersonAuthorityGroups, ...personAuthorityGroupCollection];
        jest.spyOn(personAuthorityGroupService, 'addPersonAuthorityGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(personAuthorityGroupService.query).toHaveBeenCalled();
        expect(personAuthorityGroupService.addPersonAuthorityGroupToCollectionIfMissing).toHaveBeenCalledWith(
          personAuthorityGroupCollection,
          ...additionalPersonAuthorityGroups
        );
        expect(comp.personAuthorityGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const person: IPerson = { id: 456 };
        const userInfo: IUser = { id: 78698 };
        person.userInfo = userInfo;
        const createdBy: IUser = { id: 30282 };
        person.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 5152 };
        person.lastUpdatedBy = lastUpdatedBy;
        const organisationUnits: IOrganisationUnit = { id: 960 };
        person.organisationUnits = [organisationUnits];
        const dataViewOrganisationUnits: IOrganisationUnit = { id: 27980 };
        person.dataViewOrganisationUnits = [dataViewOrganisationUnits];
        const personAuthorityGroups: IPersonAuthorityGroup = { id: 82506 };
        person.personAuthorityGroups = [personAuthorityGroups];

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(person));
        expect(comp.usersSharedCollection).toContain(userInfo);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.organisationUnitsSharedCollection).toContain(organisationUnits);
        expect(comp.organisationUnitsSharedCollection).toContain(dataViewOrganisationUnits);
        expect(comp.personAuthorityGroupsSharedCollection).toContain(personAuthorityGroups);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Person>>();
        const person = { id: 123 };
        jest.spyOn(personService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personService.update).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Person>>();
        const person = new Person();
        jest.spyOn(personService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(personService.create).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Person>>();
        const person = { id: 123 };
        jest.spyOn(personService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personService.update).toHaveBeenCalledWith(person);
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

      describe('trackPersonAuthorityGroupById', () => {
        it('Should return tracked PersonAuthorityGroup primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonAuthorityGroupById(0, entity);
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

      describe('getSelectedPersonAuthorityGroup', () => {
        it('Should return option if no PersonAuthorityGroup is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPersonAuthorityGroup(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected PersonAuthorityGroup for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPersonAuthorityGroup(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this PersonAuthorityGroup is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPersonAuthorityGroup(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
