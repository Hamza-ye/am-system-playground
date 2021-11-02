jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DemographicDataService } from '../service/demographic-data.service';
import { IDemographicData, DemographicData } from '../demographic-data.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDemographicDataSource } from 'app/entities/demographic-data-source/demographic-data-source.model';
import { DemographicDataSourceService } from 'app/entities/demographic-data-source/service/demographic-data-source.service';

import { DemographicDataUpdateComponent } from './demographic-data-update.component';

describe('Component Tests', () => {
  describe('DemographicData Management Update Component', () => {
    let comp: DemographicDataUpdateComponent;
    let fixture: ComponentFixture<DemographicDataUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let demographicDataService: DemographicDataService;
    let organisationUnitService: OrganisationUnitService;
    let userService: UserService;
    let demographicDataSourceService: DemographicDataSourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DemographicDataUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DemographicDataUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DemographicDataUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      demographicDataService = TestBed.inject(DemographicDataService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      userService = TestBed.inject(UserService);
      demographicDataSourceService = TestBed.inject(DemographicDataSourceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call OrganisationUnit query and add missing value', () => {
        const demographicData: IDemographicData = { id: 456 };
        const organisationUnit: IOrganisationUnit = { id: 1939 };
        demographicData.organisationUnit = organisationUnit;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 96230 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [organisationUnit];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ demographicData });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const demographicData: IDemographicData = { id: 456 };
        const user: IUser = { id: 56296 };
        demographicData.user = user;
        const lastUpdatedBy: IUser = { id: 78577 };
        demographicData.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 28649 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ demographicData });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DemographicDataSource query and add missing value', () => {
        const demographicData: IDemographicData = { id: 456 };
        const source: IDemographicDataSource = { id: 93995 };
        demographicData.source = source;

        const demographicDataSourceCollection: IDemographicDataSource[] = [{ id: 53923 }];
        jest.spyOn(demographicDataSourceService, 'query').mockReturnValue(of(new HttpResponse({ body: demographicDataSourceCollection })));
        const additionalDemographicDataSources = [source];
        const expectedCollection: IDemographicDataSource[] = [...additionalDemographicDataSources, ...demographicDataSourceCollection];
        jest.spyOn(demographicDataSourceService, 'addDemographicDataSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ demographicData });
        comp.ngOnInit();

        expect(demographicDataSourceService.query).toHaveBeenCalled();
        expect(demographicDataSourceService.addDemographicDataSourceToCollectionIfMissing).toHaveBeenCalledWith(
          demographicDataSourceCollection,
          ...additionalDemographicDataSources
        );
        expect(comp.demographicDataSourcesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const demographicData: IDemographicData = { id: 456 };
        const organisationUnit: IOrganisationUnit = { id: 36331 };
        demographicData.organisationUnit = organisationUnit;
        const user: IUser = { id: 900 };
        demographicData.user = user;
        const lastUpdatedBy: IUser = { id: 45158 };
        demographicData.lastUpdatedBy = lastUpdatedBy;
        const source: IDemographicDataSource = { id: 15129 };
        demographicData.source = source;

        activatedRoute.data = of({ demographicData });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(demographicData));
        expect(comp.organisationUnitsSharedCollection).toContain(organisationUnit);
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.demographicDataSourcesSharedCollection).toContain(source);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemographicData>>();
        const demographicData = { id: 123 };
        jest.spyOn(demographicDataService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicData });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demographicData }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(demographicDataService.update).toHaveBeenCalledWith(demographicData);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemographicData>>();
        const demographicData = new DemographicData();
        jest.spyOn(demographicDataService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicData });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: demographicData }));
        saveSubject.complete();

        // THEN
        expect(demographicDataService.create).toHaveBeenCalledWith(demographicData);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DemographicData>>();
        const demographicData = { id: 123 };
        jest.spyOn(demographicDataService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ demographicData });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(demographicDataService.update).toHaveBeenCalledWith(demographicData);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrganisationUnitById', () => {
        it('Should return tracked OrganisationUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrganisationUnitById(0, entity);
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

      describe('trackDemographicDataSourceById', () => {
        it('Should return tracked DemographicDataSource primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDemographicDataSourceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
