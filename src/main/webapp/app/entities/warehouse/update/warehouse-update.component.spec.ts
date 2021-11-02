jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WarehouseService } from '../service/warehouse.service';
import { IWarehouse, Warehouse } from '../warehouse.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IActivity } from 'app/entities/activity/activity.model';
import { ActivityService } from 'app/entities/activity/service/activity.service';

import { WarehouseUpdateComponent } from './warehouse-update.component';

describe('Component Tests', () => {
  describe('Warehouse Management Update Component', () => {
    let comp: WarehouseUpdateComponent;
    let fixture: ComponentFixture<WarehouseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let warehouseService: WarehouseService;
    let userService: UserService;
    let activityService: ActivityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WarehouseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WarehouseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WarehouseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      warehouseService = TestBed.inject(WarehouseService);
      userService = TestBed.inject(UserService);
      activityService = TestBed.inject(ActivityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const warehouse: IWarehouse = { id: 456 };
        const user: IUser = { id: 12147 };
        warehouse.user = user;
        const lastUpdatedBy: IUser = { id: 28118 };
        warehouse.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 36573 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ warehouse });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Activity query and add missing value', () => {
        const warehouse: IWarehouse = { id: 456 };
        const activity: IActivity = { id: 97593 };
        warehouse.activity = activity;

        const activityCollection: IActivity[] = [{ id: 65976 }];
        jest.spyOn(activityService, 'query').mockReturnValue(of(new HttpResponse({ body: activityCollection })));
        const additionalActivities = [activity];
        const expectedCollection: IActivity[] = [...additionalActivities, ...activityCollection];
        jest.spyOn(activityService, 'addActivityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ warehouse });
        comp.ngOnInit();

        expect(activityService.query).toHaveBeenCalled();
        expect(activityService.addActivityToCollectionIfMissing).toHaveBeenCalledWith(activityCollection, ...additionalActivities);
        expect(comp.activitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const warehouse: IWarehouse = { id: 456 };
        const user: IUser = { id: 61585 };
        warehouse.user = user;
        const lastUpdatedBy: IUser = { id: 72414 };
        warehouse.lastUpdatedBy = lastUpdatedBy;
        const activity: IActivity = { id: 62968 };
        warehouse.activity = activity;

        activatedRoute.data = of({ warehouse });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(warehouse));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.activitiesSharedCollection).toContain(activity);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Warehouse>>();
        const warehouse = { id: 123 };
        jest.spyOn(warehouseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ warehouse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: warehouse }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(warehouseService.update).toHaveBeenCalledWith(warehouse);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Warehouse>>();
        const warehouse = new Warehouse();
        jest.spyOn(warehouseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ warehouse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: warehouse }));
        saveSubject.complete();

        // THEN
        expect(warehouseService.create).toHaveBeenCalledWith(warehouse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Warehouse>>();
        const warehouse = { id: 123 };
        jest.spyOn(warehouseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ warehouse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(warehouseService.update).toHaveBeenCalledWith(warehouse);
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

      describe('trackActivityById', () => {
        it('Should return tracked Activity primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackActivityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
