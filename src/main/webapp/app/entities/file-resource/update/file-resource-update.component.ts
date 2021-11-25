import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFileResource, FileResource } from '../file-resource.model';
import { FileResourceService } from '../service/file-resource.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-file-resource-update',
  templateUrl: './file-resource-update.component.html',
})
export class FileResourceUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    contentType: [],
    contentLength: [],
    contentMd5: [],
    storageKey: [],
    assigned: [],
    domain: [],
    hasMultipleStorageFiles: [],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected fileResourceService: FileResourceService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileResource }) => {
      if (fileResource.id === undefined) {
        const today = dayjs().startOf('day');
        fileResource.created = today;
        fileResource.lastUpdated = today;
      }

      this.updateForm(fileResource);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileResource = this.createFromForm();
    if (fileResource.id !== undefined) {
      this.subscribeToSaveResponse(this.fileResourceService.update(fileResource));
    } else {
      this.subscribeToSaveResponse(this.fileResourceService.create(fileResource));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileResource>>): void {
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

  protected updateForm(fileResource: IFileResource): void {
    this.editForm.patchValue({
      id: fileResource.id,
      uid: fileResource.uid,
      code: fileResource.code,
      name: fileResource.name,
      created: fileResource.created ? fileResource.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: fileResource.lastUpdated ? fileResource.lastUpdated.format(DATE_TIME_FORMAT) : null,
      contentType: fileResource.contentType,
      contentLength: fileResource.contentLength,
      contentMd5: fileResource.contentMd5,
      storageKey: fileResource.storageKey,
      assigned: fileResource.assigned,
      domain: fileResource.domain,
      hasMultipleStorageFiles: fileResource.hasMultipleStorageFiles,
      createdBy: fileResource.createdBy,
      lastUpdatedBy: fileResource.lastUpdatedBy,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      fileResource.createdBy,
      fileResource.lastUpdatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IFileResource {
    return {
      ...new FileResource(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      contentType: this.editForm.get(['contentType'])!.value,
      contentLength: this.editForm.get(['contentLength'])!.value,
      contentMd5: this.editForm.get(['contentMd5'])!.value,
      storageKey: this.editForm.get(['storageKey'])!.value,
      assigned: this.editForm.get(['assigned'])!.value,
      domain: this.editForm.get(['domain'])!.value,
      hasMultipleStorageFiles: this.editForm.get(['hasMultipleStorageFiles'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
