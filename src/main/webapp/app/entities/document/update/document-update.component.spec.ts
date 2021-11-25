jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentService } from '../service/document.service';
import { IDocument, Document } from '../document.model';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { FileResourceService } from 'app/entities/file-resource/service/file-resource.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { DocumentUpdateComponent } from './document-update.component';

describe('Component Tests', () => {
  describe('Document Management Update Component', () => {
    let comp: DocumentUpdateComponent;
    let fixture: ComponentFixture<DocumentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentService: DocumentService;
    let fileResourceService: FileResourceService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentService = TestBed.inject(DocumentService);
      fileResourceService = TestBed.inject(FileResourceService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call FileResource query and add missing value', () => {
        const document: IDocument = { id: 456 };
        const fileResource: IFileResource = { id: 34418 };
        document.fileResource = fileResource;

        const fileResourceCollection: IFileResource[] = [{ id: 17422 }];
        jest.spyOn(fileResourceService, 'query').mockReturnValue(of(new HttpResponse({ body: fileResourceCollection })));
        const additionalFileResources = [fileResource];
        const expectedCollection: IFileResource[] = [...additionalFileResources, ...fileResourceCollection];
        jest.spyOn(fileResourceService, 'addFileResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ document });
        comp.ngOnInit();

        expect(fileResourceService.query).toHaveBeenCalled();
        expect(fileResourceService.addFileResourceToCollectionIfMissing).toHaveBeenCalledWith(
          fileResourceCollection,
          ...additionalFileResources
        );
        expect(comp.fileResourcesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const document: IDocument = { id: 456 };
        const createdBy: IUser = { id: 65926 };
        document.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 79546 };
        document.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 53121 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ document });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const document: IDocument = { id: 456 };
        const fileResource: IFileResource = { id: 81712 };
        document.fileResource = fileResource;
        const createdBy: IUser = { id: 69742 };
        document.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 85018 };
        document.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ document });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(document));
        expect(comp.fileResourcesSharedCollection).toContain(fileResource);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Document>>();
        const document = { id: 123 };
        jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ document });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: document }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentService.update).toHaveBeenCalledWith(document);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Document>>();
        const document = new Document();
        jest.spyOn(documentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ document });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: document }));
        saveSubject.complete();

        // THEN
        expect(documentService.create).toHaveBeenCalledWith(document);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Document>>();
        const document = { id: 123 };
        jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ document });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentService.update).toHaveBeenCalledWith(document);
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
