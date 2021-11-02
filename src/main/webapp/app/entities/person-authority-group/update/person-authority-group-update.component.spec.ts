jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonAuthorityGroupService } from '../service/person-authority-group.service';
import { IPersonAuthorityGroup, PersonAuthorityGroup } from '../person-authority-group.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PersonAuthorityGroupUpdateComponent } from './person-authority-group-update.component';

describe('Component Tests', () => {
  describe('PersonAuthorityGroup Management Update Component', () => {
    let comp: PersonAuthorityGroupUpdateComponent;
    let fixture: ComponentFixture<PersonAuthorityGroupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personAuthorityGroupService: PersonAuthorityGroupService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonAuthorityGroupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonAuthorityGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonAuthorityGroupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personAuthorityGroupService = TestBed.inject(PersonAuthorityGroupService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const personAuthorityGroup: IPersonAuthorityGroup = { id: 456 };
        const user: IUser = { id: 26097 };
        personAuthorityGroup.user = user;
        const lastUpdatedBy: IUser = { id: 10113 };
        personAuthorityGroup.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 79028 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ personAuthorityGroup });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const personAuthorityGroup: IPersonAuthorityGroup = { id: 456 };
        const user: IUser = { id: 95359 };
        personAuthorityGroup.user = user;
        const lastUpdatedBy: IUser = { id: 89586 };
        personAuthorityGroup.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ personAuthorityGroup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(personAuthorityGroup));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PersonAuthorityGroup>>();
        const personAuthorityGroup = { id: 123 };
        jest.spyOn(personAuthorityGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personAuthorityGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personAuthorityGroup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personAuthorityGroupService.update).toHaveBeenCalledWith(personAuthorityGroup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PersonAuthorityGroup>>();
        const personAuthorityGroup = new PersonAuthorityGroup();
        jest.spyOn(personAuthorityGroupService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personAuthorityGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: personAuthorityGroup }));
        saveSubject.complete();

        // THEN
        expect(personAuthorityGroupService.create).toHaveBeenCalledWith(personAuthorityGroup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PersonAuthorityGroup>>();
        const personAuthorityGroup = { id: 123 };
        jest.spyOn(personAuthorityGroupService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ personAuthorityGroup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personAuthorityGroupService.update).toHaveBeenCalledWith(personAuthorityGroup);
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
    });
  });
});
