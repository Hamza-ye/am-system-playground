import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IContentPage, ContentPage } from '../content-page.model';
import { ContentPageService } from '../service/content-page.service';
import { IImageAlbum } from 'app/entities/image-album/image-album.model';
import { ImageAlbumService } from 'app/entities/image-album/service/image-album.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IRelatedLink } from 'app/entities/related-link/related-link.model';
import { RelatedLinkService } from 'app/entities/related-link/service/related-link.service';
import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';

@Component({
  selector: 'app-content-page-update',
  templateUrl: './content-page-update.component.html',
})
export class ContentPageUpdateComponent implements OnInit {
  isSaving = false;

  imageAlbumsCollection: IImageAlbum[] = [];
  usersSharedCollection: IUser[] = [];
  relatedLinksSharedCollection: IRelatedLink[] = [];
  documentsSharedCollection: IDocument[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    title: [],
    subtitle: [],
    content: [null, [Validators.maxLength(3000)]],
    active: [],
    visitedCount: [],
    imageAlbum: [],
    createdBy: [],
    lastUpdatedBy: [],
    relatedLinks: [],
    attachments: [],
  });

  constructor(
    protected contentPageService: ContentPageService,
    protected imageAlbumService: ImageAlbumService,
    protected userService: UserService,
    protected relatedLinkService: RelatedLinkService,
    protected documentService: DocumentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contentPage }) => {
      if (contentPage.id === undefined) {
        const today = dayjs().startOf('day');
        contentPage.created = today;
        contentPage.lastUpdated = today;
      }

      this.updateForm(contentPage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contentPage = this.createFromForm();
    if (contentPage.id !== undefined) {
      this.subscribeToSaveResponse(this.contentPageService.update(contentPage));
    } else {
      this.subscribeToSaveResponse(this.contentPageService.create(contentPage));
    }
  }

  trackImageAlbumById(index: number, item: IImageAlbum): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackRelatedLinkById(index: number, item: IRelatedLink): number {
    return item.id!;
  }

  trackDocumentById(index: number, item: IDocument): number {
    return item.id!;
  }

  getSelectedRelatedLink(option: IRelatedLink, selectedVals?: IRelatedLink[]): IRelatedLink {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedDocument(option: IDocument, selectedVals?: IDocument[]): IDocument {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContentPage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contentPage: IContentPage): void {
    this.editForm.patchValue({
      id: contentPage.id,
      uid: contentPage.uid,
      code: contentPage.code,
      name: contentPage.name,
      created: contentPage.created ? contentPage.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: contentPage.lastUpdated ? contentPage.lastUpdated.format(DATE_TIME_FORMAT) : null,
      title: contentPage.title,
      subtitle: contentPage.subtitle,
      content: contentPage.content,
      active: contentPage.active,
      visitedCount: contentPage.visitedCount,
      imageAlbum: contentPage.imageAlbum,
      createdBy: contentPage.createdBy,
      lastUpdatedBy: contentPage.lastUpdatedBy,
      relatedLinks: contentPage.relatedLinks,
      attachments: contentPage.attachments,
    });

    this.imageAlbumsCollection = this.imageAlbumService.addImageAlbumToCollectionIfMissing(
      this.imageAlbumsCollection,
      contentPage.imageAlbum
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      contentPage.createdBy,
      contentPage.lastUpdatedBy
    );
    this.relatedLinksSharedCollection = this.relatedLinkService.addRelatedLinkToCollectionIfMissing(
      this.relatedLinksSharedCollection,
      ...(contentPage.relatedLinks ?? [])
    );
    this.documentsSharedCollection = this.documentService.addDocumentToCollectionIfMissing(
      this.documentsSharedCollection,
      ...(contentPage.attachments ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.imageAlbumService
      .query({ filter: 'contentpage-is-null' })
      .pipe(map((res: HttpResponse<IImageAlbum[]>) => res.body ?? []))
      .pipe(
        map((imageAlbums: IImageAlbum[]) =>
          this.imageAlbumService.addImageAlbumToCollectionIfMissing(imageAlbums, this.editForm.get('imageAlbum')!.value)
        )
      )
      .subscribe((imageAlbums: IImageAlbum[]) => (this.imageAlbumsCollection = imageAlbums));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(
            users,
            this.editForm.get('createdBy')!.value,
            this.editForm.get('lastUpdatedBy')!.value
          )
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.relatedLinkService
      .query()
      .pipe(map((res: HttpResponse<IRelatedLink[]>) => res.body ?? []))
      .pipe(
        map((relatedLinks: IRelatedLink[]) =>
          this.relatedLinkService.addRelatedLinkToCollectionIfMissing(relatedLinks, ...(this.editForm.get('relatedLinks')!.value ?? []))
        )
      )
      .subscribe((relatedLinks: IRelatedLink[]) => (this.relatedLinksSharedCollection = relatedLinks));

    this.documentService
      .query()
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing(documents, ...(this.editForm.get('attachments')!.value ?? []))
        )
      )
      .subscribe((documents: IDocument[]) => (this.documentsSharedCollection = documents));
  }

  protected createFromForm(): IContentPage {
    return {
      ...new ContentPage(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      title: this.editForm.get(['title'])!.value,
      subtitle: this.editForm.get(['subtitle'])!.value,
      content: this.editForm.get(['content'])!.value,
      active: this.editForm.get(['active'])!.value,
      visitedCount: this.editForm.get(['visitedCount'])!.value,
      imageAlbum: this.editForm.get(['imageAlbum'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      relatedLinks: this.editForm.get(['relatedLinks'])!.value,
      attachments: this.editForm.get(['attachments'])!.value,
    };
  }
}
