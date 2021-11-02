jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MalariaUnitStaffMemberService } from '../service/malaria-unit-staff-member.service';
import { IMalariaUnitStaffMember, MalariaUnitStaffMember } from '../malaria-unit-staff-member.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IMalariaUnit } from 'app/entities/malaria-unit/malaria-unit.model';
import { MalariaUnitService } from 'app/entities/malaria-unit/service/malaria-unit.service';

import { MalariaUnitStaffMemberUpdateComponent } from './malaria-unit-staff-member-update.component';

describe('Component Tests', () => {
  describe('MalariaUnitStaffMember Management Update Component', () => {
    let comp: MalariaUnitStaffMemberUpdateComponent;
    let fixture: ComponentFixture<MalariaUnitStaffMemberUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let malariaUnitStaffMemberService: MalariaUnitStaffMemberService;
    let userService: UserService;
    let personService: PersonService;
    let malariaUnitService: MalariaUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MalariaUnitStaffMemberUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MalariaUnitStaffMemberUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MalariaUnitStaffMemberUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      malariaUnitStaffMemberService = TestBed.inject(MalariaUnitStaffMemberService);
      userService = TestBed.inject(UserService);
      personService = TestBed.inject(PersonService);
      malariaUnitService = TestBed.inject(MalariaUnitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 456 };
        const user: IUser = { id: 33980 };
        malariaUnitStaffMember.user = user;
        const lastUpdatedBy: IUser = { id: 51588 };
        malariaUnitStaffMember.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 96161 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ malariaUnitStaffMember });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 456 };
        const person: IPerson = { id: 29645 };
        malariaUnitStaffMember.person = person;

        const personCollection: IPerson[] = [{ id: 92698 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ malariaUnitStaffMember });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call MalariaUnit query and add missing value', () => {
        const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 456 };
        const malariaUnit: IMalariaUnit = { id: 32592 };
        malariaUnitStaffMember.malariaUnit = malariaUnit;

        const malariaUnitCollection: IMalariaUnit[] = [{ id: 25403 }];
        jest.spyOn(malariaUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: malariaUnitCollection })));
        const additionalMalariaUnits = [malariaUnit];
        const expectedCollection: IMalariaUnit[] = [...additionalMalariaUnits, ...malariaUnitCollection];
        jest.spyOn(malariaUnitService, 'addMalariaUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ malariaUnitStaffMember });
        comp.ngOnInit();

        expect(malariaUnitService.query).toHaveBeenCalled();
        expect(malariaUnitService.addMalariaUnitToCollectionIfMissing).toHaveBeenCalledWith(
          malariaUnitCollection,
          ...additionalMalariaUnits
        );
        expect(comp.malariaUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const malariaUnitStaffMember: IMalariaUnitStaffMember = { id: 456 };
        const user: IUser = { id: 22950 };
        malariaUnitStaffMember.user = user;
        const lastUpdatedBy: IUser = { id: 95185 };
        malariaUnitStaffMember.lastUpdatedBy = lastUpdatedBy;
        const person: IPerson = { id: 23834 };
        malariaUnitStaffMember.person = person;
        const malariaUnit: IMalariaUnit = { id: 52298 };
        malariaUnitStaffMember.malariaUnit = malariaUnit;

        activatedRoute.data = of({ malariaUnitStaffMember });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(malariaUnitStaffMember));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.peopleSharedCollection).toContain(person);
        expect(comp.malariaUnitsSharedCollection).toContain(malariaUnit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MalariaUnitStaffMember>>();
        const malariaUnitStaffMember = { id: 123 };
        jest.spyOn(malariaUnitStaffMemberService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ malariaUnitStaffMember });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: malariaUnitStaffMember }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(malariaUnitStaffMemberService.update).toHaveBeenCalledWith(malariaUnitStaffMember);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MalariaUnitStaffMember>>();
        const malariaUnitStaffMember = new MalariaUnitStaffMember();
        jest.spyOn(malariaUnitStaffMemberService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ malariaUnitStaffMember });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: malariaUnitStaffMember }));
        saveSubject.complete();

        // THEN
        expect(malariaUnitStaffMemberService.create).toHaveBeenCalledWith(malariaUnitStaffMember);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MalariaUnitStaffMember>>();
        const malariaUnitStaffMember = { id: 123 };
        jest.spyOn(malariaUnitStaffMemberService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ malariaUnitStaffMember });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(malariaUnitStaffMemberService.update).toHaveBeenCalledWith(malariaUnitStaffMember);
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

      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMalariaUnitById', () => {
        it('Should return tracked MalariaUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMalariaUnitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
