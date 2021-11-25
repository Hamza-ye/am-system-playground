jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PeopleGroupService } from '../service/people-group.service';
import { IPeopleGroup, PeopleGroup } from '../people-group.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { PeopleGroupUpdateComponent } from './people-group-update.component';

describe('Component Tests', () => {
  describe('PeopleGroup Management Update Component', () => {
    let comp: PeopleGroupUpdateComponent;
    let fixture: ComponentFixture<PeopleGroupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let peopleGroupService: PeopleGroupService;
    let userService: UserService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeopleGroupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PeopleGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeopleGroupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      peopleGroupService = TestBed.inject(PeopleGroupService);
      userService = TestBed.inject(UserService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const peopleGroup: IPeopleGroup = { id: 456 };
        const createdBy: IUser = { id: 77122 };
        peopleGroup.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 56598 };
        peopleGroup.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 287 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ peopleGroup });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const peopleGroup: IPeopleGroup = { id: 456 };
        const members: IPerson[] = [{ id: 36589 }];
        peopleGroup.members = members;

        const personCollection: IPerson[] = [{ id: 77165 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [...members];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ peopleGroup });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PeopleGroup query and add missing value', () => {
        const peopleGroup: IPeopleGroup = { id: 456 };
        const managedGroups: IPeopleGroup[] = [{ id: 58256 }];
        peopleGroup.managedGroups = managedGroups;

        const peopleGroupCollection: IPeopleGroup[] = [{ id: 85661 }];
        jest.spyOn(peopleGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleGroupCollection })));
        const additionalPeopleGroups = [...managedGroups];
        const expectedCollection: IPeopleGroup[] = [...additionalPeopleGroups, ...peopleGroupCollection];
        jest.spyOn(peopleGroupService, 'addPeopleGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ peopleGroup });
        comp.ngOnInit();

        expect(peopleGroupService.query).toHaveBeenCalled();
        expect(peopleGroupService.addPeopleGroupToCollectionIfMissing).toHaveBeenCalledWith(
          peopleGroupCollection,
          ...additionalPeopleGroups
        );
        expect(comp.peopleGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const peopleGroup: IPeopleGroup = { id: 456 };
        const createdBy: IUser = { id: 59384 };
        peopleGroup.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 99392 };
        peopleGroup.lastUpdatedBy = lastUpdatedBy;
        const members: IPerson = { id: 89282 };
        peopleGroup.members = [members];
        const managedGroups: IPeopleGroup = { id: 11651 };
        peopleGroup.managedGroups = [managedGroups];

        activatedRoute.data = of({ peopleGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(peopleGroup));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.peopleSharedCollection).toContain(members);
        expect(comp.peopleGroupsSharedCollection).toContain(managedGroups);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PeopleGroup>>();
        const peopleGroup = { id: 123 };
        jest.spyOn(peopleGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ peopleGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: peopleGroup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(peopleGroupService.update).toHaveBeenCalledWith(peopleGroup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PeopleGroup>>();
        const peopleGroup = new PeopleGroup();
        jest.spyOn(peopleGroupService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ peopleGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: peopleGroup }));
        saveSubject.complete();

        // THEN
        expect(peopleGroupService.create).toHaveBeenCalledWith(peopleGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PeopleGroup>>();
        const peopleGroup = { id: 123 };
        jest.spyOn(peopleGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ peopleGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(peopleGroupService.update).toHaveBeenCalledWith(peopleGroup);
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

      describe('trackPeopleGroupById', () => {
        it('Should return tracked PeopleGroup primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPeopleGroupById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPerson', () => {
        it('Should return option if no Person is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPerson(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Person for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPerson(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Person is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPerson(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedPeopleGroup', () => {
        it('Should return option if no PeopleGroup is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPeopleGroup(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected PeopleGroup for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPeopleGroup(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this PeopleGroup is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPeopleGroup(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
