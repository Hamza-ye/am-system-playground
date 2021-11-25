import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExternalFileResource, ExternalFileResource } from '../external-file-resource.model';
import { ExternalFileResourceService } from '../service/external-file-resource.service';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { FileResourceService } from 'app/entities/file-resource/service/file-resource.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-external-file-resource-update',
  templateUrl: './external-file-resource-update.component.html',
})
export class ExternalFileResourceUpdateComponent implements OnInit {
  isSaving = false;

  fileResourcesCollection: IFileResource[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    accessToken: [],
    expires: [],
    fileResource: [],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected externalFileResourceService: ExternalFileResourceService,
    protected fileResourceService: FileResourceService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ externalFileResource }) => {
      if (externalFileResource.id === undefined) {
        const today = dayjs().startOf('day');
        externalFileResource.created = today;
        externalFileResource.lastUpdated = today;
        externalFileResource.expires = today;
      }

      this.updateForm(externalFileResource);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const externalFileResource = this.createFromForm();
    if (externalFileResource.id !== undefined) {
      this.subscribeToSaveResponse(this.externalFileResourceService.update(externalFileResource));
    } else {
      this.subscribeToSaveResponse(this.externalFileResourceService.create(externalFileResource));
    }
  }

  trackFileResourceById(index: number, item: IFileResource): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExternalFileResource>>): void {
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

  protected updateForm(externalFileResource: IExternalFileResource): void {
    this.editForm.patchValue({
      id: externalFileResource.id,
      uid: externalFileResource.uid,
      code: externalFileResource.code,
      name: externalFileResource.name,
      created: externalFileResource.created ? externalFileResource.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: externalFileResource.lastUpdated ? externalFileResource.lastUpdated.format(DATE_TIME_FORMAT) : null,
      accessToken: externalFileResource.accessToken,
      expires: externalFileResource.expires ? externalFileResource.expires.format(DATE_TIME_FORMAT) : null,
      fileResource: externalFileResource.fileResource,
      createdBy: externalFileResource.createdBy,
      lastUpdatedBy: externalFileResource.lastUpdatedBy,
    });

    this.fileResourcesCollection = this.fileResourceService.addFileResourceToCollectionIfMissing(
      this.fileResourcesCollection,
      externalFileResource.fileResource
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      externalFileResource.createdBy,
      externalFileResource.lastUpdatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fileResourceService
      .query({ filter: 'externalfileresource-is-null' })
      .pipe(map((res: HttpResponse<IFileResource[]>) => res.body ?? []))
      .pipe(
        map((fileResources: IFileResource[]) =>
          this.fileResourceService.addFileResourceToCollectionIfMissing(fileResources, this.editForm.get('fileResource')!.value)
        )
      )
      .subscribe((fileResources: IFileResource[]) => (this.fileResourcesCollection = fileResources));

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

  protected createFromForm(): IExternalFileResource {
    return {
      ...new ExternalFileResource(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      accessToken: this.editForm.get(['accessToken'])!.value,
      expires: this.editForm.get(['expires'])!.value ? dayjs(this.editForm.get(['expires'])!.value, DATE_TIME_FORMAT) : undefined,
      fileResource: this.editForm.get(['fileResource'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
