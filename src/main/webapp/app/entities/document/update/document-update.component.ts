import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDocument, Document } from '../document.model';
import { DocumentService } from '../service/document.service';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { FileResourceService } from 'app/entities/file-resource/service/file-resource.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-document-update',
  templateUrl: './document-update.component.html',
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;

  fileResourcesSharedCollection: IFileResource[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    url: [],
    external: [],
    contentType: [],
    attachment: [],
    fileResource: [],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected documentService: DocumentService,
    protected fileResourceService: FileResourceService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      if (document.id === undefined) {
        const today = dayjs().startOf('day');
        document.created = today;
        document.lastUpdated = today;
      }

      this.updateForm(document);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.createFromForm();
    if (document.id !== undefined) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  trackFileResourceById(index: number, item: IFileResource): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
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

  protected updateForm(document: IDocument): void {
    this.editForm.patchValue({
      id: document.id,
      uid: document.uid,
      code: document.code,
      name: document.name,
      created: document.created ? document.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: document.lastUpdated ? document.lastUpdated.format(DATE_TIME_FORMAT) : null,
      url: document.url,
      external: document.external,
      contentType: document.contentType,
      attachment: document.attachment,
      fileResource: document.fileResource,
      createdBy: document.createdBy,
      lastUpdatedBy: document.lastUpdatedBy,
    });

    this.fileResourcesSharedCollection = this.fileResourceService.addFileResourceToCollectionIfMissing(
      this.fileResourcesSharedCollection,
      document.fileResource
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      document.createdBy,
      document.lastUpdatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fileResourceService
      .query()
      .pipe(map((res: HttpResponse<IFileResource[]>) => res.body ?? []))
      .pipe(
        map((fileResources: IFileResource[]) =>
          this.fileResourceService.addFileResourceToCollectionIfMissing(fileResources, this.editForm.get('fileResource')!.value)
        )
      )
      .subscribe((fileResources: IFileResource[]) => (this.fileResourcesSharedCollection = fileResources));

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
  }

  protected createFromForm(): IDocument {
    return {
      ...new Document(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      url: this.editForm.get(['url'])!.value,
      external: this.editForm.get(['external'])!.value,
      contentType: this.editForm.get(['contentType'])!.value,
      attachment: this.editForm.get(['attachment'])!.value,
      fileResource: this.editForm.get(['fileResource'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
