jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CHVTeamService } from '../service/chv-team.service';
import { ICHVTeam, CHVTeam } from '../chv-team.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';

import { CHVTeamUpdateComponent } from './chv-team-update.component';

describe('Component Tests', () => {
  describe('CHVTeam Management Update Component', () => {
    let comp: CHVTeamUpdateComponent;
    let fixture: ComponentFixture<CHVTeamUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cHVTeamService: CHVTeamService;
    let userService: UserService;
    let personService: PersonService;
    let cHVService: CHVService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVTeamUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CHVTeamUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVTeamUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cHVTeamService = TestBed.inject(CHVTeamService);
      userService = TestBed.inject(UserService);
      personService = TestBed.inject(PersonService);
      cHVService = TestBed.inject(CHVService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const cHVTeam: ICHVTeam = { id: 456 };
        const createdBy: IUser = { id: 36917 };
        cHVTeam.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 31318 };
        cHVTeam.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 9674 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVTeam });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const cHVTeam: ICHVTeam = { id: 456 };
        const person: IPerson = { id: 82619 };
        cHVTeam.person = person;

        const personCollection: IPerson[] = [{ id: 80723 }];
        jest.spyOn(personService, 'query').mockReturnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        jest.spyOn(personService, 'addPersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVTeam });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CHV query and add missing value', () => {
        const cHVTeam: ICHVTeam = { id: 456 };
        const responsibleForChvs: ICHV[] = [{ id: 67602 }];
        cHVTeam.responsibleForChvs = responsibleForChvs;

        const cHVCollection: ICHV[] = [{ id: 28823 }];
        jest.spyOn(cHVService, 'query').mockReturnValue(of(new HttpResponse({ body: cHVCollection })));
        const additionalCHVS = [...responsibleForChvs];
        const expectedCollection: ICHV[] = [...additionalCHVS, ...cHVCollection];
        jest.spyOn(cHVService, 'addCHVToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVTeam });
        comp.ngOnInit();

        expect(cHVService.query).toHaveBeenCalled();
        expect(cHVService.addCHVToCollectionIfMissing).toHaveBeenCalledWith(cHVCollection, ...additionalCHVS);
        expect(comp.cHVSSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cHVTeam: ICHVTeam = { id: 456 };
        const createdBy: IUser = { id: 77039 };
        cHVTeam.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 38654 };
        cHVTeam.lastUpdatedBy = lastUpdatedBy;
        const person: IPerson = { id: 55290 };
        cHVTeam.person = person;
        const responsibleForChvs: ICHV = { id: 99667 };
        cHVTeam.responsibleForChvs = [responsibleForChvs];

        activatedRoute.data = of({ cHVTeam });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cHVTeam));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.peopleSharedCollection).toContain(person);
        expect(comp.cHVSSharedCollection).toContain(responsibleForChvs);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVTeam>>();
        const cHVTeam = { id: 123 };
        jest.spyOn(cHVTeamService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVTeam });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHVTeam }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cHVTeamService.update).toHaveBeenCalledWith(cHVTeam);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVTeam>>();
        const cHVTeam = new CHVTeam();
        jest.spyOn(cHVTeamService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVTeam });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHVTeam }));
        saveSubject.complete();

        // THEN
        expect(cHVTeamService.create).toHaveBeenCalledWith(cHVTeam);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVTeam>>();
        const cHVTeam = { id: 123 };
        jest.spyOn(cHVTeamService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVTeam });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cHVTeamService.update).toHaveBeenCalledWith(cHVTeam);
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

      describe('trackCHVById', () => {
        it('Should return tracked CHV primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCHVById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedCHV', () => {
        it('Should return option if no CHV is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCHV(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected CHV for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCHV(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this CHV is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCHV(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
