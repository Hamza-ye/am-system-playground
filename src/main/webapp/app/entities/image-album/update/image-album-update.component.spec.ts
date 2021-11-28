jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ImageAlbumService } from '../service/image-album.service';
import { IImageAlbum, ImageAlbum } from '../image-album.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { FileResourceService } from 'app/entities/file-resource/service/file-resource.service';

import { ImageAlbumUpdateComponent } from './image-album-update.component';

describe('Component Tests', () => {
  describe('ImageAlbum Management Update Component', () => {
    let comp: ImageAlbumUpdateComponent;
    let fixture: ComponentFixture<ImageAlbumUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let imageAlbumService: ImageAlbumService;
    let userService: UserService;
    let fileResourceService: FileResourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ImageAlbumUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ImageAlbumUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ImageAlbumUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      imageAlbumService = TestBed.inject(ImageAlbumService);
      userService = TestBed.inject(UserService);
      fileResourceService = TestBed.inject(FileResourceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const imageAlbum: IImageAlbum = { id: 456 };
        const createdBy: IUser = { id: 83049 };
        imageAlbum.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 53925 };
        imageAlbum.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 51483 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ imageAlbum });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call FileResource query and add missing value', () => {
        const imageAlbum: IImageAlbum = { id: 456 };
        const images: IFileResource[] = [{ id: 66125 }];
        imageAlbum.images = images;

        const fileResourceCollection: IFileResource[] = [{ id: 17225 }];
        jest.spyOn(fileResourceService, 'query').mockReturnValue(of(new HttpResponse({ body: fileResourceCollection })));
        const additionalFileResources = [...images];
        const expectedCollection: IFileResource[] = [...additionalFileResources, ...fileResourceCollection];
        jest.spyOn(fileResourceService, 'addFileResourceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ imageAlbum });
        comp.ngOnInit();

        expect(fileResourceService.query).toHaveBeenCalled();
        expect(fileResourceService.addFileResourceToCollectionIfMissing).toHaveBeenCalledWith(
          fileResourceCollection,
          ...additionalFileResources
        );
        expect(comp.fileResourcesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const imageAlbum: IImageAlbum = { id: 456 };
        const createdBy: IUser = { id: 46729 };
        imageAlbum.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 11247 };
        imageAlbum.lastUpdatedBy = lastUpdatedBy;
        const images: IFileResource = { id: 94133 };
        imageAlbum.images = [images];

        activatedRoute.data = of({ imageAlbum });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(imageAlbum));
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.fileResourcesSharedCollection).toContain(images);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ImageAlbum>>();
        const imageAlbum = { id: 123 };
        jest.spyOn(imageAlbumService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageAlbum });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: imageAlbum }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(imageAlbumService.update).toHaveBeenCalledWith(imageAlbum);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ImageAlbum>>();
        const imageAlbum = new ImageAlbum();
        jest.spyOn(imageAlbumService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageAlbum });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: imageAlbum }));
        saveSubject.complete();

        // THEN
        expect(imageAlbumService.create).toHaveBeenCalledWith(imageAlbum);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ImageAlbum>>();
        const imageAlbum = { id: 123 };
        jest.spyOn(imageAlbumService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ imageAlbum });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(imageAlbumService.update).toHaveBeenCalledWith(imageAlbum);
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

      describe('trackFileResourceById', () => {
        it('Should return tracked FileResource primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFileResourceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedFileResource', () => {
        it('Should return option if no FileResource is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedFileResource(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected FileResource for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedFileResource(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this FileResource is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedFileResource(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
