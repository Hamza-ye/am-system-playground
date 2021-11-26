jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DengueCasesReportService } from '../service/dengue-cases-report.service';
import { IDengueCasesReport, DengueCasesReport } from '../dengue-cases-report.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { CasesReportClassService } from 'app/entities/cases-report-class/service/cases-report-class.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IDataSet } from 'app/entities/data-set/data-set.model';
import { DataSetService } from 'app/entities/data-set/service/data-set.service';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { DengueCasesReportUpdateComponent } from './dengue-cases-report-update.component';

describe('Component Tests', () => {
  describe('DengueCasesReport Management Update Component', () => {
    let comp: DengueCasesReportUpdateComponent;
    let fixture: ComponentFixture<DengueCasesReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dengueCasesReportService: DengueCasesReportService;
    let userService: UserService;
    let casesReportClassService: CasesReportClassService;
    let periodService: PeriodService;
    let dataSetService: DataSetService;
    let organisationUnitService: OrganisationUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DengueCasesReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DengueCasesReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DengueCasesReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dengueCasesReportService = TestBed.inject(DengueCasesReportService);
      userService = TestBed.inject(UserService);
      casesReportClassService = TestBed.inject(CasesReportClassService);
      periodService = TestBed.inject(PeriodService);
      dataSetService = TestBed.inject(DataSetService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const dengueCasesReport: IDengueCasesReport = { id: 456 };
        const createdBy: IUser = { id: 57938 };
        dengueCasesReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 47424 };
        dengueCasesReport.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 38950 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CasesReportClass query and add missing value', () => {
        const dengueCasesReport: IDengueCasesReport = { id: 456 };
        const reportClass: ICasesReportClass = { id: 19341 };
        dengueCasesReport.reportClass = reportClass;

        const casesReportClassCollection: ICasesReportClass[] = [{ id: 22193 }];
        jest.spyOn(casesReportClassService, 'query').mockReturnValue(of(new HttpResponse({ body: casesReportClassCollection })));
        const additionalCasesReportClasses = [reportClass];
        const expectedCollection: ICasesReportClass[] = [...additionalCasesReportClasses, ...casesReportClassCollection];
        jest.spyOn(casesReportClassService, 'addCasesReportClassToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        expect(casesReportClassService.query).toHaveBeenCalled();
        expect(casesReportClassService.addCasesReportClassToCollectionIfMissing).toHaveBeenCalledWith(
          casesReportClassCollection,
          ...additionalCasesReportClasses
        );
        expect(comp.casesReportClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Period query and add missing value', () => {
        const dengueCasesReport: IDengueCasesReport = { id: 456 };
        const period: IPeriod = { id: 7999 };
        dengueCasesReport.period = period;

        const periodCollection: IPeriod[] = [{ id: 8399 }];
        jest.spyOn(periodService, 'query').mockReturnValue(of(new HttpResponse({ body: periodCollection })));
        const additionalPeriods = [period];
        const expectedCollection: IPeriod[] = [...additionalPeriods, ...periodCollection];
        jest.spyOn(periodService, 'addPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        expect(periodService.query).toHaveBeenCalled();
        expect(periodService.addPeriodToCollectionIfMissing).toHaveBeenCalledWith(periodCollection, ...additionalPeriods);
        expect(comp.periodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DataSet query and add missing value', () => {
        const dengueCasesReport: IDengueCasesReport = { id: 456 };
        const dataSet: IDataSet = { id: 89694 };
        dengueCasesReport.dataSet = dataSet;

        const dataSetCollection: IDataSet[] = [{ id: 1585 }];
        jest.spyOn(dataSetService, 'query').mockReturnValue(of(new HttpResponse({ body: dataSetCollection })));
        const additionalDataSets = [dataSet];
        const expectedCollection: IDataSet[] = [...additionalDataSets, ...dataSetCollection];
        jest.spyOn(dataSetService, 'addDataSetToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        expect(dataSetService.query).toHaveBeenCalled();
        expect(dataSetService.addDataSetToCollectionIfMissing).toHaveBeenCalledWith(dataSetCollection, ...additionalDataSets);
        expect(comp.dataSetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnit query and add missing value', () => {
        const dengueCasesReport: IDengueCasesReport = { id: 456 };
        const organisationUnit: IOrganisationUnit = { id: 97638 };
        dengueCasesReport.organisationUnit = organisationUnit;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 51909 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [organisationUnit];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dengueCasesReport: IDengueCasesReport = { id: 456 };
        const createdBy: IUser = { id: 64437 };
        dengueCasesReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 49940 };
        dengueCasesReport.lastUpdatedBy = lastUpdatedBy;
        const reportClass: ICasesReportClass = { id: 42788 };
        dengueCasesReport.reportClass = reportClass;
        const period: IPeriod = { id: 64358 };
        dengueCasesReport.period = period;
        const dataSet: IDataSet = { id: 64981 };
        dengueCasesReport.dataSet = dataSet;
        const organisationUnit: IOrganisationUnit = { id: 80762 };
        dengueCasesReport.organisationUnit = organisationUnit;

        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dengueCasesReport));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.casesReportClassesSharedCollection).toContain(reportClass);
        expect(comp.periodsSharedCollection).toContain(period);
        expect(comp.dataSetsSharedCollection).toContain(dataSet);
        expect(comp.organisationUnitsSharedCollection).toContain(organisationUnit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DengueCasesReport>>();
        const dengueCasesReport = { id: 123 };
        jest.spyOn(dengueCasesReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dengueCasesReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dengueCasesReportService.update).toHaveBeenCalledWith(dengueCasesReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DengueCasesReport>>();
        const dengueCasesReport = new DengueCasesReport();
        jest.spyOn(dengueCasesReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dengueCasesReport }));
        saveSubject.complete();

        // THEN
        expect(dengueCasesReportService.create).toHaveBeenCalledWith(dengueCasesReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DengueCasesReport>>();
        const dengueCasesReport = { id: 123 };
        jest.spyOn(dengueCasesReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ dengueCasesReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dengueCasesReportService.update).toHaveBeenCalledWith(dengueCasesReport);
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

      describe('trackCasesReportClassById', () => {
        it('Should return tracked CasesReportClass primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCasesReportClassById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPeriodById', () => {
        it('Should return tracked Period primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPeriodById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDataSetById', () => {
        it('Should return tracked DataSet primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDataSetById(0, entity);
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
  });
});
