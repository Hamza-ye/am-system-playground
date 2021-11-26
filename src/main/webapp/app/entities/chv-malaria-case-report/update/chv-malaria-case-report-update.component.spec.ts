jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChvMalariaCaseReportService } from '../service/chv-malaria-case-report.service';
import { IChvMalariaCaseReport, ChvMalariaCaseReport } from '../chv-malaria-case-report.model';
import { IOrganisationUnit } from 'app/entities/organisation-unit/organisation-unit.model';
import { OrganisationUnitService } from 'app/entities/organisation-unit/service/organisation-unit.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IChv } from 'app/entities/chv/chv.model';
import { ChvService } from 'app/entities/chv/service/chv.service';
import { ICasesReportClass } from 'app/entities/cases-report-class/cases-report-class.model';
import { CasesReportClassService } from 'app/entities/cases-report-class/service/cases-report-class.service';

import { ChvMalariaCaseReportUpdateComponent } from './chv-malaria-case-report-update.component';

describe('Component Tests', () => {
  describe('ChvMalariaCaseReport Management Update Component', () => {
    let comp: ChvMalariaCaseReportUpdateComponent;
    let fixture: ComponentFixture<ChvMalariaCaseReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let chvMalariaCaseReportService: ChvMalariaCaseReportService;
    let organisationUnitService: OrganisationUnitService;
    let userService: UserService;
    let chvService: ChvService;
    let casesReportClassService: CasesReportClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvMalariaCaseReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ChvMalariaCaseReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvMalariaCaseReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      chvMalariaCaseReportService = TestBed.inject(ChvMalariaCaseReportService);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      userService = TestBed.inject(UserService);
      chvService = TestBed.inject(ChvService);
      casesReportClassService = TestBed.inject(CasesReportClassService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call OrganisationUnit query and add missing value', () => {
        const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 456 };
        const subVillage: IOrganisationUnit = { id: 88465 };
        chvMalariaCaseReport.subVillage = subVillage;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 86075 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [subVillage];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 456 };
        const createdBy: IUser = { id: 29158 };
        chvMalariaCaseReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 94252 };
        chvMalariaCaseReport.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 23892 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Chv query and add missing value', () => {
        const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 456 };
        const chv: IChv = { id: 36818 };
        chvMalariaCaseReport.chv = chv;

        const chvCollection: IChv[] = [{ id: 83998 }];
        jest.spyOn(chvService, 'query').mockReturnValue(of(new HttpResponse({ body: chvCollection })));
        const additionalChvs = [chv];
        const expectedCollection: IChv[] = [...additionalChvs, ...chvCollection];
        jest.spyOn(chvService, 'addChvToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        expect(chvService.query).toHaveBeenCalled();
        expect(chvService.addChvToCollectionIfMissing).toHaveBeenCalledWith(chvCollection, ...additionalChvs);
        expect(comp.chvsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call CasesReportClass query and add missing value', () => {
        const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 456 };
        const reportClass: ICasesReportClass = { id: 78615 };
        chvMalariaCaseReport.reportClass = reportClass;

        const casesReportClassCollection: ICasesReportClass[] = [{ id: 33578 }];
        jest.spyOn(casesReportClassService, 'query').mockReturnValue(of(new HttpResponse({ body: casesReportClassCollection })));
        const additionalCasesReportClasses = [reportClass];
        const expectedCollection: ICasesReportClass[] = [...additionalCasesReportClasses, ...casesReportClassCollection];
        jest.spyOn(casesReportClassService, 'addCasesReportClassToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        expect(casesReportClassService.query).toHaveBeenCalled();
        expect(casesReportClassService.addCasesReportClassToCollectionIfMissing).toHaveBeenCalledWith(
          casesReportClassCollection,
          ...additionalCasesReportClasses
        );
        expect(comp.casesReportClassesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const chvMalariaCaseReport: IChvMalariaCaseReport = { id: 456 };
        const subVillage: IOrganisationUnit = { id: 8286 };
        chvMalariaCaseReport.subVillage = subVillage;
        const createdBy: IUser = { id: 12728 };
        chvMalariaCaseReport.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 41079 };
        chvMalariaCaseReport.lastUpdatedBy = lastUpdatedBy;
        const chv: IChv = { id: 20471 };
        chvMalariaCaseReport.chv = chv;
        const reportClass: ICasesReportClass = { id: 61854 };
        chvMalariaCaseReport.reportClass = reportClass;

        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(chvMalariaCaseReport));
        expect(comp.organisationUnitsSharedCollection).toContain(subVillage);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.chvsSharedCollection).toContain(chv);
        expect(comp.casesReportClassesSharedCollection).toContain(reportClass);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvMalariaCaseReport>>();
        const chvMalariaCaseReport = { id: 123 };
        jest.spyOn(chvMalariaCaseReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chvMalariaCaseReport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(chvMalariaCaseReportService.update).toHaveBeenCalledWith(chvMalariaCaseReport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvMalariaCaseReport>>();
        const chvMalariaCaseReport = new ChvMalariaCaseReport();
        jest.spyOn(chvMalariaCaseReportService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: chvMalariaCaseReport }));
        saveSubject.complete();

        // THEN
        expect(chvMalariaCaseReportService.create).toHaveBeenCalledWith(chvMalariaCaseReport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ChvMalariaCaseReport>>();
        const chvMalariaCaseReport = { id: 123 };
        jest.spyOn(chvMalariaCaseReportService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ chvMalariaCaseReport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(chvMalariaCaseReportService.update).toHaveBeenCalledWith(chvMalariaCaseReport);
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

      describe('trackChvById', () => {
        it('Should return tracked Chv primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackChvById(0, entity);
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
