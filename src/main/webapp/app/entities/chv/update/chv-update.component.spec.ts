jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CHVService } from '../service/chv.service';
import { ICHV, CHV } from '../chv.model';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CHVUpdateComponent } from './chv-update.component';

describe('Component Tests', () => {
  describe('CHV Management Update Component', () => {
    let comp: CHVUpdateComponent;
    let fixture: ComponentFixture<CHVUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cHVService: CHVService;
    let personService: PersonService;
    let organisationUnitService: OrganisationUnitService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CHVUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cHVService = TestBed.inject(CHVService);
      personService = TestBed.inject(PersonService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call person query and add missing value', () => {
        const cHV: ICHV = { id: 456 };
        const person: IPerson = { id: 7324 };
        cHV.person = person;

        const personCollection: IPerson[] = [{ id: 7490 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const expectedCollection: IPerson[] = [person, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHV });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, person);
        expect(comp.peopleCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnit query and add missing value', () => {
        const cHV: ICHV = { id: 456 };
        const district: IOrganisationUnit = { id: 44024 };
        cHV.district = district;
        const homeSubvillage: IOrganisationUnit = { id: 92699 };
        cHV.homeSubvillage = homeSubvillage;
        const managedByHf: IOrganisationUnit = { id: 96270 };
        cHV.managedByHf = managedByHf;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 99218 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [district, homeSubvillage, managedByHf];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHV });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const cHV: ICHV = { id: 456 };
        const user: IUser = { id: 27479 };
        cHV.user = user;
        const lastUpdatedBy: IUser = { id: 3243 };
        cHV.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 85470 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHV });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cHV: ICHV = { id: 456 };
        const person: IPerson = { id: 20550 };
        cHV.person = person;
        const district: IOrganisationUnit = { id: 72627 };
        cHV.district = district;
        const homeSubvillage: IOrganisationUnit = { id: 7781 };
        cHV.homeSubvillage = homeSubvillage;
        const managedByHf: IOrganisationUnit = { id: 12689 };
        cHV.managedByHf = managedByHf;
        const user: IUser = { id: 60845 };
        cHV.user = user;
        const lastUpdatedBy: IUser = { id: 52994 };
        cHV.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ cHV });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cHV));
        expect(comp.peopleCollection).toContain(person);
        expect(comp.organisationUnitsSharedCollection).toContain(district);
        expect(comp.organisationUnitsSharedCollection).toContain(homeSubvillage);
        expect(comp.organisationUnitsSharedCollection).toContain(managedByHf);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHV>>();
        const cHV = { id: 123 };
        jest.spyOn(cHVService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHV });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHV }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cHVService.update).toHaveBeenCalledWith(cHV);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHV>>();
        const cHV = new CHV();
        jest.spyOn(cHVService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHV });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHV }));
        saveSubject.complete();

        // THEN
        expect(cHVService.create).toHaveBeenCalledWith(cHV);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHV>>();
        const cHV = { id: 123 };
        jest.spyOn(cHVService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHV });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cHVService.update).toHaveBeenCalledWith(cHV);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
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
