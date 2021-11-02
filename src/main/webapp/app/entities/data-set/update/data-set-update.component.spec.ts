jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DataSetService } from '../service/data-set.service';
import { IDataSet, DataSet } from '../data-set.model';
import { IPeriodType } from 'app/entities/period-type/period-type.model';
import { PeriodTypeService } from 'app/entities/period-type/service/period-type.service';
import { IPeopleGroup } from 'app/entities/people-group/people-group.model';
import { PeopleGroupService } from 'app/entities/people-group/service/people-group.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { DataSetUpdateComponent } from './data-set-update.component';

describe('Component Tests', () => {
  describe('DataSet Management Update Component', () => {
    let comp: DataSetUpdateComponent;
    let fixture: ComponentFixture<DataSetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dataSetService: DataSetService;
    let periodTypeService: PeriodTypeService;
    let peopleGroupService: PeopleGroupService;
    let userService: UserService;
    let organisationUnitService: OrganisationUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DataSetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DataSetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DataSetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dataSetService = TestBed.inject(DataSetService);
      periodTypeService = TestBed.inject(PeriodTypeService);
      peopleGroupService = TestBed.inject(PeopleGroupService);
      userService = TestBed.inject(UserService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PeriodType query and add missing value', () => {
        const dataSet: IDataSet = { id: 456 };
        const periodType: IPeriodType = { id: 93977 };
        dataSet.periodType = periodType;

        const periodTypeCollection: IPeriodType[] = [{ id: 5605 }];
        jest.spyOn(periodTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: periodTypeCollection })));
        const additionalPeriodTypes = [periodType];
        const expectedCollection: IPeriodType[] = [...additionalPeriodTypes, ...periodTypeCollection];
        jest.spyOn(periodTypeService, 'addPeriodTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        expect(periodTypeService.query).toHaveBeenCalled();
        expect(periodTypeService.addPeriodTypeToCollectionIfMissing).toHaveBeenCalledWith(periodTypeCollection, ...additionalPeriodTypes);
        expect(comp.periodTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PeopleGroup query and add missing value', () => {
        const dataSet: IDataSet = { id: 456 };
        const notificationRecipients: IPeopleGroup = { id: 7351 };
        dataSet.notificationRecipients = notificationRecipients;

        const peopleGroupCollection: IPeopleGroup[] = [{ id: 30177 }];
        jest.spyOn(peopleGroupService, 'query').mockReturnValue(of(new HttpResponse({ body: peopleGroupCollection })));
        const additionalPeopleGroups = [notificationRecipients];
        const expectedCollection: IPeopleGroup[] = [...additionalPeopleGroups, ...peopleGroupCollection];
        jest.spyOn(peopleGroupService, 'addPeopleGroupToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        expect(peopleGroupService.query).toHaveBeenCalled();
        expect(peopleGroupService.addPeopleGroupToCollectionIfMissing).toHaveBeenCalledWith(
          peopleGroupCollection,
          ...additionalPeopleGroups
        );
        expect(comp.peopleGroupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const dataSet: IDataSet = { id: 456 };
        const user: IUser = { id: 59942 };
        dataSet.user = user;
        const lastUpdatedBy: IUser = { id: 49434 };
        dataSet.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 12046 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnit query and add missing value', () => {
        const dataSet: IDataSet = { id: 456 };
        const sources: IOrganisationUnit[] = [{ id: 15614 }];
        dataSet.sources = sources;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 54541 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [...sources];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dataSet: IDataSet = { id: 456 };
        const periodType: IPeriodType = { id: 18282 };
        dataSet.periodType = periodType;
        const notificationRecipients: IPeopleGroup = { id: 1845 };
        dataSet.notificationRecipients = notificationRecipients;
        const user: IUser = { id: 64363 };
        dataSet.user = user;
        const lastUpdatedBy: IUser = { id: 87846 };
        dataSet.lastUpdatedBy = lastUpdatedBy;
        const sources: IOrganisationUnit = { id: 33940 };
        dataSet.sources = [sources];

        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dataSet));
        expect(comp.periodTypesSharedCollection).toContain(periodType);
        expect(comp.peopleGroupsSharedCollection).toContain(notificationRecipients);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.organisationUnitsSharedCollection).toContain(sources);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataSet>>();
        const dataSet = { id: 123 };
        jest.spyOn(dataSetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dataSet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dataSetService.update).toHaveBeenCalledWith(dataSet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataSet>>();
        const dataSet = new DataSet();
        jest.spyOn(dataSetService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dataSet }));
        saveSubject.complete();

        // THEN
        expect(dataSetService.create).toHaveBeenCalledWith(dataSet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DataSet>>();
        const dataSet = { id: 123 };
        jest.spyOn(dataSetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dataSet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dataSetService.update).toHaveBeenCalledWith(dataSet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPeriodTypeById', () => {
        it('Should return tracked PeriodType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPeriodTypeById(0, entity);
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

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
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
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedOrganisationUnit', () => {
        it('Should return option if no OrganisationUnit is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedOrganisationUnit(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected OrganisationUnit for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedOrganisationUnit(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this OrganisationUnit is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedOrganisationUnit(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
