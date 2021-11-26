jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';
import { ICHVMalariaCaseReport, CHVMalariaCaseReport } from '../chv-malaria-case-report.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { CasesReportClassService } from 'app/entities/cases-report-class/service/cases-report-class.service';

import { CHVMalariaCaseReportUpdateComponent } from './chv-malaria-case-report-update.component';

describe('Component Tests', () => {
  describe('CHVMalariaCaseReport Management Update Component', () => {
    let comp: CHVMalariaCaseReportUpdateComponent;
    let fixture: ComponentFixture<CHVMalariaCaseReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cHVMalariaCaseReportService: CHVMalariaCaseReportService;
    let organisationUnitService: OrganisationUnitService;
    let userService: UserService;
    let cHVService: CHVService;
    let casesReportClassService: CasesReportClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVMalariaCaseReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CHVMalariaCaseReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVMalariaCaseReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cHVMalariaCaseReportService = TestBed.inject(CHVMalariaCaseReportService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      userService = TestBed.inject(UserService);
      cHVService = TestBed.inject(CHVService);
      casesReportClassService = TestBed.inject(CasesReportClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call OrganisationUnit query and add missing value', () => {
        const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 456 };
        const subVillage: IOrganisationUnit = { id: 28246 };
        cHVMalariaCaseReport.subVillage = subVillage;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 37219 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [subVillage];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 456 };
        const createdBy: IUser = { id: 1630 };
        cHVMalariaCaseReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 42451 };
        cHVMalariaCaseReport.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 87170 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CHV query and add missing value', () => {
        const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 456 };
        const chv: ICHV = { id: 2960 };
        cHVMalariaCaseReport.chv = chv;

        const cHVCollection: ICHV[] = [{ id: 1508 }];
        jest.spyOn(cHVService, 'query').mockReturnValue(of(new HttpResponse({ body: cHVCollection })));
        const additionalCHVS = [chv];
        const expectedCollection: ICHV[] = [...additionalCHVS, ...cHVCollection];
        jest.spyOn(cHVService, 'addCHVToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        expect(cHVService.query).toHaveBeenCalled();
        expect(cHVService.addCHVToCollectionIfMissing).toHaveBeenCalledWith(cHVCollection, ...additionalCHVS);
        expect(comp.cHVSSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CasesReportClass query and add missing value', () => {
        const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 456 };
        const reportClass: ICasesReportClass = { id: 85202 };
        cHVMalariaCaseReport.reportClass = reportClass;

        const casesReportClassCollection: ICasesReportClass[] = [{ id: 14573 }];
        jest.spyOn(casesReportClassService, 'query').mockReturnValue(of(new HttpResponse({ body: casesReportClassCollection })));
        const additionalCasesReportClasses = [reportClass];
        const expectedCollection: ICasesReportClass[] = [...additionalCasesReportClasses, ...casesReportClassCollection];
        jest.spyOn(casesReportClassService, 'addCasesReportClassToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        expect(casesReportClassService.query).toHaveBeenCalled();
        expect(casesReportClassService.addCasesReportClassToCollectionIfMissing).toHaveBeenCalledWith(
          casesReportClassCollection,
          ...additionalCasesReportClasses
        );
        expect(comp.casesReportClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cHVMalariaCaseReport: ICHVMalariaCaseReport = { id: 456 };
        const subVillage: IOrganisationUnit = { id: 19180 };
        cHVMalariaCaseReport.subVillage = subVillage;
        const createdBy: IUser = { id: 40188 };
        cHVMalariaCaseReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 38956 };
        cHVMalariaCaseReport.lastUpdatedBy = lastUpdatedBy;
        const chv: ICHV = { id: 83323 };
        cHVMalariaCaseReport.chv = chv;
        const reportClass: ICasesReportClass = { id: 44869 };
        cHVMalariaCaseReport.reportClass = reportClass;

        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cHVMalariaCaseReport));
        expect(comp.organisationUnitsSharedCollection).toContain(subVillage);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.cHVSSharedCollection).toContain(chv);
        expect(comp.casesReportClassesSharedCollection).toContain(reportClass);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVMalariaCaseReport>>();
        const cHVMalariaCaseReport = { id: 123 };
        jest.spyOn(cHVMalariaCaseReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHVMalariaCaseReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cHVMalariaCaseReportService.update).toHaveBeenCalledWith(cHVMalariaCaseReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVMalariaCaseReport>>();
        const cHVMalariaCaseReport = new CHVMalariaCaseReport();
        jest.spyOn(cHVMalariaCaseReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cHVMalariaCaseReport }));
        saveSubject.complete();

        // THEN
        expect(cHVMalariaCaseReportService.create).toHaveBeenCalledWith(cHVMalariaCaseReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CHVMalariaCaseReport>>();
        const cHVMalariaCaseReport = { id: 123 };
        jest.spyOn(cHVMalariaCaseReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cHVMalariaCaseReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cHVMalariaCaseReportService.update).toHaveBeenCalledWith(cHVMalariaCaseReport);
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

      describe('trackCHVById', () => {
        it('Should return tracked CHV primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCHVById(0, entity);
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
    });
  });
});
