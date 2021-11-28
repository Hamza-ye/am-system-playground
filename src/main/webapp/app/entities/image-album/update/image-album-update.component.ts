import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IImageAlbum, ImageAlbum } from '../image-album.model';
import { ImageAlbumService } from '../service/image-album.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFileResource } from 'app/entities/file-resource/file-resource.model';
import { FileResourceService } from 'app/entities/file-resource/service/file-resource.service';

@Component({
  selector: 'app-image-album-update',
  templateUrl: './image-album-update.component.html',
})
export class ImageAlbumUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  fileResourcesSharedCollection: IFileResource[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    title: [],
    coverImageUid: [],
    subtitle: [],
    createdBy: [],
    lastUpdatedBy: [],
    images: [],
  });

  constructor(
    protected imageAlbumService: ImageAlbumService,
    protected userService: UserService,
    protected fileResourceService: FileResourceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imageAlbum }) => {
      if (imageAlbum.id === undefined) {
        const today = dayjs().startOf('day');
        imageAlbum.created = today;
        imageAlbum.lastUpdated = today;
      }

      this.updateForm(imageAlbum);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imageAlbum = this.createFromForm();
    if (imageAlbum.id !== undefined) {
      this.subscribeToSaveResponse(this.imageAlbumService.update(imageAlbum));
    } else {
      this.subscribeToSaveResponse(this.imageAlbumService.create(imageAlbum));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackFileResourceById(index: number, item: IFileResource): number {
    return item.id!;
  }

  getSelectedFileResource(option: IFileResource, selectedVals?: IFileResource[]): IFileResource {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImageAlbum>>): void {
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

  protected updateForm(imageAlbum: IImageAlbum): void {
    this.editForm.patchValue({
      id: imageAlbum.id,
      uid: imageAlbum.uid,
      code: imageAlbum.code,
      name: imageAlbum.name,
      created: imageAlbum.created ? imageAlbum.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: imageAlbum.lastUpdated ? imageAlbum.lastUpdated.format(DATE_TIME_FORMAT) : null,
      title: imageAlbum.title,
      coverImageUid: imageAlbum.coverImageUid,
      subtitle: imageAlbum.subtitle,
      createdBy: imageAlbum.createdBy,
      lastUpdatedBy: imageAlbum.lastUpdatedBy,
      images: imageAlbum.images,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      imageAlbum.createdBy,
      imageAlbum.lastUpdatedBy
    );
    this.fileResourcesSharedCollection = this.fileResourceService.addFileResourceToCollectionIfMissing(
      this.fileResourcesSharedCollection,
      ...(imageAlbum.images ?? [])
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

    this.fileResourceService
      .query()
      .pipe(map((res: HttpResponse<IFileResource[]>) => res.body ?? []))
      .pipe(
        map((fileResources: IFileResource[]) =>
          this.fileResourceService.addFileResourceToCollectionIfMissing(fileResources, ...(this.editForm.get('images')!.value ?? []))
        )
      )
      .subscribe((fileResources: IFileResource[]) => (this.fileResourcesSharedCollection = fileResources));
  }

  protected createFromForm(): IImageAlbum {
    return {
      ...new ImageAlbum(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      title: this.editForm.get(['title'])!.value,
      coverImageUid: this.editForm.get(['coverImageUid'])!.value,
      subtitle: this.editForm.get(['subtitle'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      images: this.editForm.get(['images'])!.value,
    };
  }
}
