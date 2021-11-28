jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContentPageService } from '../service/content-page.service';
import { IContentPage, ContentPage } from '../content-page.model';
import { IImageAlbum } from 'app/entities/image-album/image-album.model';
import { ImageAlbumService } from 'app/entities/image-album/service/image-album.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRelatedLink } from 'app/entities/related-link/related-link.model';
import { RelatedLinkService } from 'app/entities/related-link/service/related-link.service';

import { ContentPageUpdateComponent } from './content-page-update.component';

describe('Component Tests', () => {
  describe('ContentPage Management Update Component', () => {
    let comp: ContentPageUpdateComponent;
    let fixture: ComponentFixture<ContentPageUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contentPageService: ContentPageService;
    let imageAlbumService: ImageAlbumService;
    let userService: UserService;
    let relatedLinkService: RelatedLinkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContentPageUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContentPageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContentPageUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contentPageService = TestBed.inject(ContentPageService);
      imageAlbumService = TestBed.inject(ImageAlbumService);
      userService = TestBed.inject(UserService);
      relatedLinkService = TestBed.inject(RelatedLinkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call imageAlbum query and add missing value', () => {
        const contentPage: IContentPage = { id: 456 };
        const imageAlbum: IImageAlbum = { id: 50065 };
        contentPage.imageAlbum = imageAlbum;

        const imageAlbumCollection: IImageAlbum[] = [{ id: 36501 }];
        jest.spyOn(imageAlbumService, 'query').mockReturnValue(of(new HttpResponse({ body: imageAlbumCollection })));
        const expectedCollection: IImageAlbum[] = [imageAlbum, ...imageAlbumCollection];
        jest.spyOn(imageAlbumService, 'addImageAlbumToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contentPage });
        comp.ngOnInit();

        expect(imageAlbumService.query).toHaveBeenCalled();
        expect(imageAlbumService.addImageAlbumToCollectionIfMissing).toHaveBeenCalledWith(imageAlbumCollection, imageAlbum);
        expect(comp.imageAlbumsCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const contentPage: IContentPage = { id: 456 };
        const createdBy: IUser = { id: 31191 };
        contentPage.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 10356 };
        contentPage.lastUpdatedBy = lastUpdatedBy;

        const userCollection: IUser[] = [{ id: 36903 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [createdBy, lastUpdatedBy];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contentPage });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call RelatedLink query and add missing value', () => {
        const contentPage: IContentPage = { id: 456 };
        const relatedLinks: IRelatedLink[] = [{ id: 32966 }];
        contentPage.relatedLinks = relatedLinks;

        const relatedLinkCollection: IRelatedLink[] = [{ id: 57494 }];
        jest.spyOn(relatedLinkService, 'query').mockReturnValue(of(new HttpResponse({ body: relatedLinkCollection })));
        const additionalRelatedLinks = [...relatedLinks];
        const expectedCollection: IRelatedLink[] = [...additionalRelatedLinks, ...relatedLinkCollection];
        jest.spyOn(relatedLinkService, 'addRelatedLinkToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ contentPage });
        comp.ngOnInit();

        expect(relatedLinkService.query).toHaveBeenCalled();
        expect(relatedLinkService.addRelatedLinkToCollectionIfMissing).toHaveBeenCalledWith(
          relatedLinkCollection,
          ...additionalRelatedLinks
        );
        expect(comp.relatedLinksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contentPage: IContentPage = { id: 456 };
        const imageAlbum: IImageAlbum = { id: 76406 };
        contentPage.imageAlbum = imageAlbum;
        const createdBy: IUser = { id: 43416 };
        contentPage.createdBy = createdBy;
        const lastUpdatedBy: IUser = { id: 15101 };
        contentPage.lastUpdatedBy = lastUpdatedBy;
        const relatedLinks: IRelatedLink = { id: 75818 };
        contentPage.relatedLinks = [relatedLinks];

        activatedRoute.data = of({ contentPage });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contentPage));
        expect(comp.imageAlbumsCollection).toContain(imageAlbum);
        expect(comp.usersSharedCollection).toContain(createdBy);
        expect(comp.usersSharedCollection).toContain(lastUpdatedBy);
        expect(comp.relatedLinksSharedCollection).toContain(relatedLinks);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContentPage>>();
        const contentPage = { id: 123 };
        jest.spyOn(contentPageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contentPage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contentPage }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contentPageService.update).toHaveBeenCalledWith(contentPage);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContentPage>>();
        const contentPage = new ContentPage();
        jest.spyOn(contentPageService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contentPage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contentPage }));
        saveSubject.complete();

        // THEN
        expect(contentPageService.create).toHaveBeenCalledWith(contentPage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ContentPage>>();
        const contentPage = { id: 123 };
        jest.spyOn(contentPageService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ contentPage });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contentPageService.update).toHaveBeenCalledWith(contentPage);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackImageAlbumById', () => {
        it('Should return tracked ImageAlbum primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackImageAlbumById(0, entity);
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

      describe('trackRelatedLinkById', () => {
        it('Should return tracked RelatedLink primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRelatedLinkById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedRelatedLink', () => {
        it('Should return option if no RelatedLink is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedRelatedLink(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected RelatedLink for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedRelatedLink(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this RelatedLink is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedRelatedLink(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
