import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDemographicDataSource, DemographicDataSource } from '../demographic-data-source.model';
import { DemographicDataSourceService } from '../service/demographic-data-source.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-demographic-data-source-update',
  templateUrl: './demographic-data-source-update.component.html',
})
export class DemographicDataSourceUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    created: [],
    lastUpdated: [],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected demographicDataSourceService: DemographicDataSourceService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demographicDataSource }) => {
      if (demographicDataSource.id === undefined) {
        const today = dayjs().startOf('day');
        demographicDataSource.created = today;
        demographicDataSource.lastUpdated = today;
      }

      this.updateForm(demographicDataSource);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demographicDataSource = this.createFromForm();
    if (demographicDataSource.id !== undefined) {
      this.subscribeToSaveResponse(this.demographicDataSourceService.update(demographicDataSource));
    } else {
      this.subscribeToSaveResponse(this.demographicDataSourceService.create(demographicDataSource));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemographicDataSource>>): void {
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

  protected updateForm(demographicDataSource: IDemographicDataSource): void {
    this.editForm.patchValue({
      id: demographicDataSource.id,
      uid: demographicDataSource.uid,
      code: demographicDataSource.code,
      name: demographicDataSource.name,
      created: demographicDataSource.created ? demographicDataSource.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: demographicDataSource.lastUpdated ? demographicDataSource.lastUpdated.format(DATE_TIME_FORMAT) : null,
      createdBy: demographicDataSource.createdBy,
      lastUpdatedBy: demographicDataSource.lastUpdatedBy,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      demographicDataSource.createdBy,
      demographicDataSource.lastUpdatedBy
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

  protected createFromForm(): IDemographicDataSource {
    return {
      ...new DemographicDataSource(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
