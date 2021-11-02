import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICasesReportClass, CasesReportClass } from '../cases-report-class.model';
import { CasesReportClassService } from '../service/cases-report-class.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'app-cases-report-class-update',
  templateUrl: './cases-report-class-update.component.html',
})
export class CasesReportClassUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    code: [null, []],
    name: [],
    shortName: [null, [Validators.maxLength(50)]],
    description: [],
    created: [],
    lastUpdated: [],
    user: [],
    lastUpdatedBy: [],
  });

  constructor(
    protected casesReportClassService: CasesReportClassService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ casesReportClass }) => {
      if (casesReportClass.id === undefined) {
        const today = dayjs().startOf('day');
        casesReportClass.created = today;
        casesReportClass.lastUpdated = today;
      }

      this.updateForm(casesReportClass);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const casesReportClass = this.createFromForm();
    if (casesReportClass.id !== undefined) {
      this.subscribeToSaveResponse(this.casesReportClassService.update(casesReportClass));
    } else {
      this.subscribeToSaveResponse(this.casesReportClassService.create(casesReportClass));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICasesReportClass>>): void {
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

  protected updateForm(casesReportClass: ICasesReportClass): void {
    this.editForm.patchValue({
      id: casesReportClass.id,
      uid: casesReportClass.uid,
      code: casesReportClass.code,
      name: casesReportClass.name,
      shortName: casesReportClass.shortName,
      description: casesReportClass.description,
      created: casesReportClass.created ? casesReportClass.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: casesReportClass.lastUpdated ? casesReportClass.lastUpdated.format(DATE_TIME_FORMAT) : null,
      user: casesReportClass.user,
      lastUpdatedBy: casesReportClass.lastUpdatedBy,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      casesReportClass.user,
      casesReportClass.lastUpdatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value, this.editForm.get('lastUpdatedBy')!.value)
        )
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): ICasesReportClass {
    return {
      ...new CasesReportClass(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      description: this.editForm.get(['description'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
    };
  }
}
