jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TeamService } from '../service/team.service';
import { ITeam, Team } from '../team.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IWarehouse } from 'app/entities/warehouse/warehouse.model';
import { WarehouseService } from 'app/entities/warehouse/service/warehouse.service';

import { TeamUpdateComponent } from './team-update.component';

describe('Component Tests', () => {
  describe('Team Management Update Component', () => {
    let comp: TeamUpdateComponent;
    let fixture: ComponentFixture<TeamUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let teamService: TeamService;
    let userService: UserService;
    let personService: PersonService;
    let warehouseService: WarehouseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TeamUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      teamService = TestBed.inject(TeamService);
      userService = TestBed.inject(UserService);
      personService = TestBed.inject(PersonService);
      warehouseService = TestBed.inject(WarehouseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const team: ITeam = { id: 456 };
        const user: IUser = { id: 57888 };
        team.user = user;
        const lastUpdatedBy: IUser = { id: 23198 };
        team.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 31912 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ team });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const team: ITeam = { id: 456 };
        const person: IPerson = { id: 3496 };
        team.person = person;

        const personCollection: IPerson[] = [{ id: 61040 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ team });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Warehouse query and add missing value', () => {
        const team: ITeam = { id: 456 };
        const assignedToWarehouses: IWarehouse[] = [{ id: 63635 }];
        team.assignedToWarehouses = assignedToWarehouses;

        const warehouseCollection: IWarehouse[] = [{ id: 85715 }];
        jest.spyOn(warehouseService, 'query').mockReturnValue(of(new HttpResponse({ body: warehouseCollection })));
        const additionalWarehouses = [...assignedToWarehouses];
        const expectedCollection: IWarehouse[] = [...additionalWarehouses, ...warehouseCollection];
        jest.spyOn(warehouseService, 'addWarehouseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ team });
        comp.ngOnInit();

        expect(warehouseService.query).toHaveBeenCalled();
        expect(warehouseService.addWarehouseToCollectionIfMissing).toHaveBeenCalledWith(warehouseCollection, ...additionalWarehouses);
        expect(comp.warehousesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const team: ITeam = { id: 456 };
        const user: IUser = { id: 10214 };
        team.user = user;
        const lastUpdatedBy: IUser = { id: 36027 };
        team.lastUpdatedBy = lastUpdatedBy;
        const person: IPerson = { id: 93478 };
        team.person = person;
        const assignedToWarehouses: IWarehouse = { id: 22294 };
        team.assignedToWarehouses = [assignedToWarehouses];

        activatedRoute.data = of({ team });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(team));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.peopleSharedCollection).toContain(person);
        expect(comp.warehousesSharedCollection).toContain(assignedToWarehouses);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Team>>();
        const team = { id: 123 };
        jest.spyOn(teamService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ team });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: team }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(teamService.update).toHaveBeenCalledWith(team);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Team>>();
        const team = new Team();
        jest.spyOn(teamService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ team });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: team }));
        saveSubject.complete();

        // THEN
        expect(teamService.create).toHaveBeenCalledWith(team);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Team>>();
        const team = { id: 123 };
        jest.spyOn(teamService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ team });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(teamService.update).toHaveBeenCalledWith(team);
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

      describe('trackWarehouseById', () => {
        it('Should return tracked Warehouse primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWarehouseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedWarehouse', () => {
        it('Should return option if no Warehouse is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedWarehouse(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Warehouse for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedWarehouse(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Warehouse is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedWarehouse(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
