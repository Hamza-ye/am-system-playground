jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExternalFileResourceService } from '../service/external-file-resource.service';
import { IExternalFileResource, ExternalFileResource } from '../external-file-resource.model';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { FileResourceService } from 'app/entities/file-resource/service/file-resource.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ExternalFileResourceUpdateComponent } from './external-file-resource-update.component';

describe('Component Tests', () => {
  describe('ExternalFileResource Management Update Component', () => {
    let comp: ExternalFileResourceUpdateComponent;
    let fixture: ComponentFixture<ExternalFileResourceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let externalFileResourceService: ExternalFileResourceService;
    let fileResourceService: FileResourceService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExternalFileResourceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExternalFileResourceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExternalFileResourceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      externalFileResourceService = TestBed.inject(ExternalFileResourceService);
      fileResourceService = TestBed.inject(FileResourceService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call fileResource query and add missing value', () => {
        const externalFileResource: IExternalFileResource = { id: 456 };
        const fileResource: IFileResource = { id: 69515 };
        externalFileResource.fileResource = fileResource;

        const fileResourceCollection: IFileResource[] = [{ id: 48077 }];
        jest.spyOn(fileResourceService, 'query').mockReturnValue(of(new HttpResponse({ body: fileResourceCollection })));
        const expectedCollection: IFileResource[] = [fileResource, ...fileResourceCollection];
        jest.spyOn(fileResourceService, 'addFileResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ externalFileResource });
        comp.ngOnInit();

        expect(fileResourceService.query).toHaveBeenCalled();
        expect(fileResourceService.addFileResourceToCollectionIfMissing).toHaveBeenCalledWith(fileResourceCollection, fileResource);
        expect(comp.fileResourcesCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const externalFileResource: IExternalFileResource = { id: 456 };
        const createdBy: IUser = { id: 68718 };
        externalFileResource.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 46939 };
        externalFileResource.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 37321 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ externalFileResource });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const externalFileResource: IExternalFileResource = { id: 456 };
        const fileResource: IFileResource = { id: 37859 };
        externalFileResource.fileResource = fileResource;
        const createdBy: IUser = { id: 51400 };
        externalFileResource.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 36385 };
        externalFileResource.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ externalFileResource });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(externalFileResource));
        expect(comp.fileResourcesCollection).toContain(fileResource);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExternalFileResource>>();
        const externalFileResource = { id: 123 };
        jest.spyOn(externalFileResourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ externalFileResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: externalFileResource }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(externalFileResourceService.update).toHaveBeenCalledWith(externalFileResource);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExternalFileResource>>();
        const externalFileResource = new ExternalFileResource();
        jest.spyOn(externalFileResourceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ externalFileResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: externalFileResource }));
        saveSubject.complete();

        // THEN
        expect(externalFileResourceService.create).toHaveBeenCalledWith(externalFileResource);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExternalFileResource>>();
        const externalFileResource = { id: 123 };
        jest.spyOn(externalFileResourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ externalFileResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(externalFileResourceService.update).toHaveBeenCalledWith(externalFileResource);
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
