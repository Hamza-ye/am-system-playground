jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrganisationUnitService } from '../service/organisation-unit.service';
import { IOrganisationUnit, OrganisationUnit } from '../organisation-unit.model';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { FileResourceService } from 'app/entities/file-resource/service/file-resource.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IMalariaUnit } from 'app/entities/malaria-unit/malaria-unit.model';
import { MalariaUnitService } from 'app/entities/malaria-unit/service/malaria-unit.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';

import { OrganisationUnitUpdateComponent } from './organisation-unit-update.component';

describe('Component Tests', () => {
  describe('OrganisationUnit Management Update Component', () => {
    let comp: OrganisationUnitUpdateComponent;
    let fixture: ComponentFixture<OrganisationUnitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let organisationUnitService: OrganisationUnitService;
    let fileResourceService: FileResourceService;
    let userService: UserService;
    let malariaUnitService: MalariaUnitService;
    let cHVService: CHVService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrganisationUnitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      organisationUnitService = TestBed.inject(OrganisationUnitService);
      fileResourceService = TestBed.inject(FileResourceService);
      userService = TestBed.inject(UserService);
      malariaUnitService = TestBed.inject(MalariaUnitService);
      cHVService = TestBed.inject(CHVService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call FileResource query and add missing value', () => {
        const organisationUnit: IOrganisationUnit = { id: 456 };
        const image: IFileResource = { id: 42187 };
        organisationUnit.image = image;

        const fileResourceCollection: IFileResource[] = [{ id: 5356 }];
        jest.spyOn(fileResourceService, 'query').mockReturnValue(of(new HttpResponse({ body: fileResourceCollection })));
        const additionalFileResources = [image];
        const expectedCollection: IFileResource[] = [...additionalFileResources, ...fileResourceCollection];
        jest.spyOn(fileResourceService, 'addFileResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        expect(fileResourceService.query).toHaveBeenCalled();
        expect(fileResourceService.addFileResourceToCollectionIfMissing).toHaveBeenCalledWith(
          fileResourceCollection,
          ...additionalFileResources
        );
        expect(comp.fileResourcesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrganisationUnit query and add missing value', () => {
        const organisationUnit: IOrganisationUnit = { id: 456 };
        const parent: IOrganisationUnit = { id: 71497 };
        organisationUnit.parent = parent;
        const hfHomeSubVillage: IOrganisationUnit = { id: 95615 };
        organisationUnit.hfHomeSubVillage = hfHomeSubVillage;
        const coveredByHf: IOrganisationUnit = { id: 85775 };
        organisationUnit.coveredByHf = coveredByHf;

        const organisationUnitCollection: IOrganisationUnit[] = [{ id: 70094 }];
        jest.spyOn(organisationUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: organisationUnitCollection })));
        const additionalOrganisationUnits = [parent, hfHomeSubVillage, coveredByHf];
        const expectedCollection: IOrganisationUnit[] = [...additionalOrganisationUnits, ...organisationUnitCollection];
        jest.spyOn(organisationUnitService, 'addOrganisationUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        expect(organisationUnitService.query).toHaveBeenCalled();
        expect(organisationUnitService.addOrganisationUnitToCollectionIfMissing).toHaveBeenCalledWith(
          organisationUnitCollection,
          ...additionalOrganisationUnits
        );
        expect(comp.organisationUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const organisationUnit: IOrganisationUnit = { id: 456 };
        const createdBy: IUser = { id: 21694 };
        organisationUnit.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 90199 };
        organisationUnit.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 55424 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call MalariaUnit query and add missing value', () => {
        const organisationUnit: IOrganisationUnit = { id: 456 };
        const malariaUnit: IMalariaUnit = { id: 80663 };
        organisationUnit.malariaUnit = malariaUnit;

        const malariaUnitCollection: IMalariaUnit[] = [{ id: 47770 }];
        jest.spyOn(malariaUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: malariaUnitCollection })));
        const additionalMalariaUnits = [malariaUnit];
        const expectedCollection: IMalariaUnit[] = [...additionalMalariaUnits, ...malariaUnitCollection];
        jest.spyOn(malariaUnitService, 'addMalariaUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        expect(malariaUnitService.query).toHaveBeenCalled();
        expect(malariaUnitService.addMalariaUnitToCollectionIfMissing).toHaveBeenCalledWith(
          malariaUnitCollection,
          ...additionalMalariaUnits
        );
        expect(comp.malariaUnitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Chv query and add missing value', () => {
        const organisationUnit: IOrganisationUnit = { id: 456 };
        const assignedChv: ICHV = { id: 76826 };
        organisationUnit.assignedChv = assignedChv;

        const cHVCollection: ICHV[] = [{ id: 82396 }];
        jest.spyOn(cHVService, 'query').mockReturnValue(of(new HttpResponse({ body: cHVCollection })));
        const additionalCHVS = [assignedChv];
        const expectedCollection: ICHV[] = [...additionalCHVS, ...cHVCollection];
        jest.spyOn(cHVService, 'addCHVToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        expect(cHVService.query).toHaveBeenCalled();
        expect(cHVService.addCHVToCollectionIfMissing).toHaveBeenCalledWith(cHVCollection, ...additionalCHVS);
        expect(comp.cHVSSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const organisationUnit: IOrganisationUnit = { id: 456 };
        const image: IFileResource = { id: 90665 };
        organisationUnit.image = image;
        const parent: IOrganisationUnit = { id: 86550 };
        organisationUnit.parent = parent;
        const hfHomeSubVillage: IOrganisationUnit = { id: 67176 };
        organisationUnit.hfHomeSubVillage = hfHomeSubVillage;
        const coveredByHf: IOrganisationUnit = { id: 87812 };
        organisationUnit.coveredByHf = coveredByHf;
        const createdBy: IUser = { id: 71255 };
        organisationUnit.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 15215 };
        organisationUnit.lastUpdatedBy = lastUpdatedBy;
        const malariaUnit: IMalariaUnit = { id: 37623 };
        organisationUnit.malariaUnit = malariaUnit;
        const assignedChv: ICHV = { id: 43173 };
        organisationUnit.assignedChv = assignedChv;

        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(organisationUnit));
        expect(comp.fileResourcesSharedCollection).toContain(image);
        expect(comp.organisationUnitsSharedCollection).toContain(parent);
        expect(comp.organisationUnitsSharedCollection).toContain(hfHomeSubVillage);
        expect(comp.organisationUnitsSharedCollection).toContain(coveredByHf);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.malariaUnitsSharedCollection).toContain(malariaUnit);
        expect(comp.cHVSSharedCollection).toContain(assignedChv);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnit>>();
        const organisationUnit = { id: 123 };
        jest.spyOn(organisationUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(organisationUnitService.update).toHaveBeenCalledWith(organisationUnit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnit>>();
        const organisationUnit = new OrganisationUnit();
        jest.spyOn(organisationUnitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: organisationUnit }));
        saveSubject.complete();

        // THEN
        expect(organisationUnitService.create).toHaveBeenCalledWith(organisationUnit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrganisationUnit>>();
        const organisationUnit = { id: 123 };
        jest.spyOn(organisationUnitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ organisationUnit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(organisationUnitService.update).toHaveBeenCalledWith(organisationUnit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFileResourceById', () => {
        it('Should return tracked FileResource primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFileResourceById(0, entity);
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

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMalariaUnitById', () => {
        it('Should return tracked MalariaUnit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMalariaUnitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCHVById', () => {
        it('Should return tracked Chv primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCHVById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
