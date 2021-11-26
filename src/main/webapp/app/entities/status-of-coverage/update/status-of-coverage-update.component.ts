import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStatusOfCoverage, StatusOfCoverage } from '../status-of-coverage.model';
import { StatusOfCoverageService } from '../service/status-of-coverage.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-status-of-coverage-update',
  templateUrl: './status-of-coverage-update.component.html',
})
export class StatusOfCoverageUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    status: [null, [Validators.required]],
    createdBy: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected statusOfCoverageService: StatusOfCoverageService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statusOfCoverage }) => {
      this.updateForm(statusOfCoverage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const statusOfCoverage = this.createFromForm();
    if (statusOfCoverage.id !== undefined) {
      this.subscribeToSaveResponse(this.statusOfCoverageService.update(statusOfCoverage));
    } else {
      this.subscribeToSaveResponse(this.statusOfCoverageService.create(statusOfCoverage));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatusOfCoverage>>): void {
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

  protected updateForm(statusOfCoverage: IStatusOfCoverage): void {
    this.editForm.patchValue({
      id: statusOfCoverage.id,
      code: statusOfCoverage.code,
      status: statusOfCoverage.status,
      createdBy: statusOfCoverage.createdBy,
      lastUpdatedBy: statusOfCoverage.lastUpdatedBy,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      statusOfCoverage.createdBy,
      statusOfCoverage.lastUpdatedBy
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

  protected createFromForm(): IStatusOfCoverage {
    return {
      ...new StatusOfCoverage(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      status: this.editForm.get(['status'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
