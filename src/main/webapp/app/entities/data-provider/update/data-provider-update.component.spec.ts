jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DataProviderService } from '../service/data-provider.service';
import { IDataProvider, DataProvider } from '../data-provider.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFamily } from 'app/entities/family/family.model';
import { FamilyService } from 'app/entities/family/service/family.service';

import { DataProviderUpdateComponent } from './data-provider-update.component';

describe('Component Tests', () => {
  describe('DataProvider Management Update Component', () => {
    let comp: DataProviderUpdateComponent;
    let fixture: ComponentFixture<DataProviderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dataProviderService: DataProviderService;
    let userService: UserService;
    let familyService: FamilyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DataProviderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DataProviderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataProviderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dataProviderService = TestBed.inject(DataProviderService);
      userService = TestBed.inject(UserService);
      familyService = TestBed.inject(FamilyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const dataProvider: IDataProvider = { id: 456 };
        const user: IUser = { id: 33960 };
        dataProvider.user = user;
        const lastUpdatedBy: IUser = { id: 77123 };
        dataProvider.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 9363 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dataProvider });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Family query and add missing value', () => {
        const dataProvider: IDataProvider = { id: 456 };
        const family: IFamily = { id: 74761 };
        dataProvider.family = family;

        const familyCollection: IFamily[] = [{ id: 18084 }];
        jest.spyOn(familyService, 'query').mockReturnValue(of(new HttpResponse({ body: familyCollection })));
        const additionalFamilies = [family];
        const expectedCollection: IFamily[] = [...additionalFamilies, ...familyCollection];
        jest.spyOn(familyService, 'addFamilyToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dataProvider });
        comp.ngOnInit();

        expect(familyService.query).toHaveBeenCalled();
        expect(familyService.addFamilyToCollectionIfMissing).toHaveBeenCalledWith(familyCollection, ...additionalFamilies);
        expect(comp.familiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dataProvider: IDataProvider = { id: 456 };
        const user: IUser = { id: 32425 };
        dataProvider.user = user;
        const lastUpdatedBy: IUser = { id: 23762 };
        dataProvider.lastUpdatedBy = lastUpdatedBy;
        const family: IFamily = { id: 78466 };
        dataProvider.family = family;

        activatedRoute.data = of({ dataProvider });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dataProvider));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.familiesSharedCollection).toContain(family);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataProvider>>();
        const dataProvider = { id: 123 };
        jest.spyOn(dataProviderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataProvider });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dataProvider }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dataProviderService.update).toHaveBeenCalledWith(dataProvider);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataProvider>>();
        const dataProvider = new DataProvider();
        jest.spyOn(dataProviderService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataProvider });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dataProvider }));
        saveSubject.complete();

        // THEN
        expect(dataProviderService.create).toHaveBeenCalledWith(dataProvider);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataProvider>>();
        const dataProvider = { id: 123 };
        jest.spyOn(dataProviderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataProvider });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dataProviderService.update).toHaveBeenCalledWith(dataProvider);
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

      describe('trackFamilyById', () => {
        it('Should return tracked Family primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFamilyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
