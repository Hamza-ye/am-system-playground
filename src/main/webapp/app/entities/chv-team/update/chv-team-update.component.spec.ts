jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChvTeamService } from '../service/chv-team.service';
import { IChvTeam, ChvTeam } from '../chv-team.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { IChv } from 'app/entities/chv/chv.model';
import { ChvService } from 'app/entities/chv/service/chv.service';

import { ChvTeamUpdateComponent } from './chv-team-update.component';

describe('Component Tests', () => {
  describe('ChvTeam Management Update Component', () => {
    let comp: ChvTeamUpdateComponent;
    let fixture: ComponentFixture<ChvTeamUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let chvTeamService: ChvTeamService;
    let userService: UserService;
    let personService: PersonService;
    let chvService: ChvService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvTeamUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChvTeamUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvTeamUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      chvTeamService = TestBed.inject(ChvTeamService);
      userService = TestBed.inject(UserService);
      personService = TestBed.inject(PersonService);
      chvService = TestBed.inject(ChvService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const chvTeam: IChvTeam = { id: 456 };
        const createdBy: IUser = { id: 10463 };
        chvTeam.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 41907 };
        chvTeam.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 66729 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvTeam });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const chvTeam: IChvTeam = { id: 456 };
        const person: IPerson = { id: 15224 };
        chvTeam.person = person;

        const personCollection: IPerson[] = [{ id: 73866 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvTeam });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Chv query and add missing value', () => {
        const chvTeam: IChvTeam = { id: 456 };
        const responsibleForChvs: IChv[] = [{ id: 14101 }];
        chvTeam.responsibleForChvs = responsibleForChvs;

        const chvCollection: IChv[] = [{ id: 42484 }];
        jest.spyOn(chvService, 'query').mockReturnValue(of(new HttpResponse({ body: chvCollection })));
        const additionalChvs = [...responsibleForChvs];
        const expectedCollection: IChv[] = [...additionalChvs, ...chvCollection];
        jest.spyOn(chvService, 'addChvToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvTeam });
        comp.ngOnInit();

        expect(chvService.query).toHaveBeenCalled();
        expect(chvService.addChvToCollectionIfMissing).toHaveBeenCalledWith(chvCollection, ...additionalChvs);
        expect(comp.chvsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const chvTeam: IChvTeam = { id: 456 };
        const createdBy: IUser = { id: 46351 };
        chvTeam.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 62764 };
        chvTeam.lastUpdatedBy = lastUpdatedBy;
        const person: IPerson = { id: 63620 };
        chvTeam.person = person;
        const responsibleForChvs: IChv = { id: 62970 };
        chvTeam.responsibleForChvs = [responsibleForChvs];

        activatedRoute.data = of({ chvTeam });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(chvTeam));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.peopleSharedCollection).toContain(person);
        expect(comp.chvsSharedCollection).toContain(responsibleForChvs);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvTeam>>();
        const chvTeam = { id: 123 };
        jest.spyOn(chvTeamService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvTeam });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chvTeam }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(chvTeamService.update).toHaveBeenCalledWith(chvTeam);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvTeam>>();
        const chvTeam = new ChvTeam();
        jest.spyOn(chvTeamService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvTeam });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chvTeam }));
        saveSubject.complete();

        // THEN
        expect(chvTeamService.create).toHaveBeenCalledWith(chvTeam);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvTeam>>();
        const chvTeam = { id: 123 };
        jest.spyOn(chvTeamService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvTeam });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(chvTeamService.update).toHaveBeenCalledWith(chvTeam);
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

      describe('trackChvById', () => {
        it('Should return tracked Chv primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackChvById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedChv', () => {
        it('Should return option if no Chv is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedChv(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Chv for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedChv(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Chv is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedChv(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
