jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DemographicDataSourceService } from '../service/demographic-data-source.service';
import { IDemographicDataSource, DemographicDataSource } from '../demographic-data-source.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { DemographicDataSourceUpdateComponent } from './demographic-data-source-update.component';

describe('Component Tests', () => {
  describe('DemographicDataSource Management Update Component', () => {
    let comp: DemographicDataSourceUpdateComponent;
    let fixture: ComponentFixture<DemographicDataSourceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let demographicDataSourceService: DemographicDataSourceService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DemographicDataSourceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DemographicDataSourceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemographicDataSourceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      demographicDataSourceService = TestBed.inject(DemographicDataSourceService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const demographicDataSource: IDemographicDataSource = { id: 456 };
        const user: IUser = { id: 35206 };
        demographicDataSource.user = user;
        const lastUpdatedBy: IUser = { id: 11265 };
        demographicDataSource.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 24736 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ demographicDataSource });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const demographicDataSource: IDemographicDataSource = { id: 456 };
        const user: IUser = { id: 76292 };
        demographicDataSource.user = user;
        const lastUpdatedBy: IUser = { id: 87375 };
        demographicDataSource.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ demographicDataSource });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(demographicDataSource));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemographicDataSource>>();
        const demographicDataSource = { id: 123 };
        jest.spyOn(demographicDataSourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicDataSource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demographicDataSource }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(demographicDataSourceService.update).toHaveBeenCalledWith(demographicDataSource);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemographicDataSource>>();
        const demographicDataSource = new DemographicDataSource();
        jest.spyOn(demographicDataSourceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicDataSource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demographicDataSource }));
        saveSubject.complete();

        // THEN
        expect(demographicDataSourceService.create).toHaveBeenCalledWith(demographicDataSource);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemographicDataSource>>();
        const demographicDataSource = { id: 123 };
        jest.spyOn(demographicDataSourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicDataSource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(demographicDataSourceService.update).toHaveBeenCalledWith(demographicDataSource);
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
