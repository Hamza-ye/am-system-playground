jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FileResourceService } from '../service/file-resource.service';
import { IFileResource, FileResource } from '../file-resource.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { FileResourceUpdateComponent } from './file-resource-update.component';

describe('Component Tests', () => {
  describe('FileResource Management Update Component', () => {
    let comp: FileResourceUpdateComponent;
    let fixture: ComponentFixture<FileResourceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fileResourceService: FileResourceService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FileResourceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FileResourceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileResourceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fileResourceService = TestBed.inject(FileResourceService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const fileResource: IFileResource = { id: 456 };
        const createdBy: IUser = { id: 35768 };
        fileResource.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 17645 };
        fileResource.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 16722 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ fileResource });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const fileResource: IFileResource = { id: 456 };
        const createdBy: IUser = { id: 58352 };
        fileResource.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 55927 };
        fileResource.lastUpdatedBy = lastUpdatedBy;

        activatedRoute.data = of({ fileResource });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fileResource));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileResource>>();
        const fileResource = { id: 123 };
        jest.spyOn(fileResourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileResource }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fileResourceService.update).toHaveBeenCalledWith(fileResource);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileResource>>();
        const fileResource = new FileResource();
        jest.spyOn(fileResourceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileResource }));
        saveSubject.complete();

        // THEN
        expect(fileResourceService.create).toHaveBeenCalledWith(fileResource);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FileResource>>();
        const fileResource = { id: 123 };
        jest.spyOn(fileResourceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fileResourceService.update).toHaveBeenCalledWith(fileResource);
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
