import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICHVMalariaReportVersion1, CHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICHV } from 'app/entities/chv/chv.model';
import { CHVService } from 'app/entities/chv/service/chv.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';

@Component({
  selector: 'app-chv-malaria-report-version-1-update',
  templateUrl: './chv-malaria-report-version-1-update.component.html',
})
export class CHVMalariaReportVersion1UpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  cHVSSharedCollection: ICHV[] = [];
  periodsSharedCollection: IPeriod[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    tested: [],
    positive: [],
    drugsGiven: [],
    suppsGiven: [],
    rdtBalance: [],
    rdtReceived: [],
    rdtUsed: [],
    rdtDamagedLost: [],
    drugsBalance: [],
    drugsReceived: [],
    drugsUsed: [],
    drugsDamagedLost: [],
    suppsBalance: [],
    suppsReceived: [],
    suppsUsed: [],
    suppsDamagedLost: [],
    comment: [],
    createdBy: [],
    lastUpdatedBy: [],
    chv: [null, Validators.required],
    period: [null, Validators.required],
  });

  constructor(
    protected cHVMalariaReportVersion1Service: CHVMalariaReportVersion1Service,
    protected userService: UserService,
    protected cHVService: CHVService,
    protected periodService: PeriodService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cHVMalariaReportVersion1 }) => {
      if (cHVMalariaReportVersion1.id === undefined) {
        const today = dayjs().startOf('day');
        cHVMalariaReportVersion1.created = today;
        cHVMalariaReportVersion1.lastUpdated = today;
      }

      this.updateForm(cHVMalariaReportVersion1);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cHVMalariaReportVersion1 = this.createFromForm();
    if (cHVMalariaReportVersion1.id !== undefined) {
      this.subscribeToSaveResponse(this.cHVMalariaReportVersion1Service.update(cHVMalariaReportVersion1));
    } else {
      this.subscribeToSaveResponse(this.cHVMalariaReportVersion1Service.create(cHVMalariaReportVersion1));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackCHVById(index: number, item: ICHV): number {
    return item.id!;
  }

  trackPeriodById(index: number, item: IPeriod): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICHVMalariaReportVersion1>>): void {
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

  protected updateForm(cHVMalariaReportVersion1: ICHVMalariaReportVersion1): void {
    this.editForm.patchValue({
      id: cHVMalariaReportVersion1.id,
      uid: cHVMalariaReportVersion1.uid,
      created: cHVMalariaReportVersion1.created ? cHVMalariaReportVersion1.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: cHVMalariaReportVersion1.lastUpdated ? cHVMalariaReportVersion1.lastUpdated.format(DATE_TIME_FORMAT) : null,
      tested: cHVMalariaReportVersion1.tested,
      positive: cHVMalariaReportVersion1.positive,
      drugsGiven: cHVMalariaReportVersion1.drugsGiven,
      suppsGiven: cHVMalariaReportVersion1.suppsGiven,
      rdtBalance: cHVMalariaReportVersion1.rdtBalance,
      rdtReceived: cHVMalariaReportVersion1.rdtReceived,
      rdtUsed: cHVMalariaReportVersion1.rdtUsed,
      rdtDamagedLost: cHVMalariaReportVersion1.rdtDamagedLost,
      drugsBalance: cHVMalariaReportVersion1.drugsBalance,
      drugsReceived: cHVMalariaReportVersion1.drugsReceived,
      drugsUsed: cHVMalariaReportVersion1.drugsUsed,
      drugsDamagedLost: cHVMalariaReportVersion1.drugsDamagedLost,
      suppsBalance: cHVMalariaReportVersion1.suppsBalance,
      suppsReceived: cHVMalariaReportVersion1.suppsReceived,
      suppsUsed: cHVMalariaReportVersion1.suppsUsed,
      suppsDamagedLost: cHVMalariaReportVersion1.suppsDamagedLost,
      comment: cHVMalariaReportVersion1.comment,
      createdBy: cHVMalariaReportVersion1.createdBy,
      lastUpdatedBy: cHVMalariaReportVersion1.lastUpdatedBy,
      chv: cHVMalariaReportVersion1.chv,
      period: cHVMalariaReportVersion1.period,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      cHVMalariaReportVersion1.createdBy,
      cHVMalariaReportVersion1.lastUpdatedBy
    );
    this.cHVSSharedCollection = this.cHVService.addCHVToCollectionIfMissing(this.cHVSSharedCollection, cHVMalariaReportVersion1.chv);
    this.periodsSharedCollection = this.periodService.addPeriodToCollectionIfMissing(
      this.periodsSharedCollection,
      cHVMalariaReportVersion1.period
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

    this.cHVService
      .query()
      .pipe(map((res: HttpResponse<ICHV[]>) => res.body ?? []))
      .pipe(map((cHVS: ICHV[]) => this.cHVService.addCHVToCollectionIfMissing(cHVS, this.editForm.get('chv')!.value)))
      .subscribe((cHVS: ICHV[]) => (this.cHVSSharedCollection = cHVS));

    this.periodService
      .query()
      .pipe(map((res: HttpResponse<IPeriod[]>) => res.body ?? []))
      .pipe(map((periods: IPeriod[]) => this.periodService.addPeriodToCollectionIfMissing(periods, this.editForm.get('period')!.value)))
      .subscribe((periods: IPeriod[]) => (this.periodsSharedCollection = periods));
  }

  protected createFromForm(): ICHVMalariaReportVersion1 {
    return {
      ...new CHVMalariaReportVersion1(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tested: this.editForm.get(['tested'])!.value,
      positive: this.editForm.get(['positive'])!.value,
      drugsGiven: this.editForm.get(['drugsGiven'])!.value,
      suppsGiven: this.editForm.get(['suppsGiven'])!.value,
      rdtBalance: this.editForm.get(['rdtBalance'])!.value,
      rdtReceived: this.editForm.get(['rdtReceived'])!.value,
      rdtUsed: this.editForm.get(['rdtUsed'])!.value,
      rdtDamagedLost: this.editForm.get(['rdtDamagedLost'])!.value,
      drugsBalance: this.editForm.get(['drugsBalance'])!.value,
      drugsReceived: this.editForm.get(['drugsReceived'])!.value,
      drugsUsed: this.editForm.get(['drugsUsed'])!.value,
      drugsDamagedLost: this.editForm.get(['drugsDamagedLost'])!.value,
      suppsBalance: this.editForm.get(['suppsBalance'])!.value,
      suppsReceived: this.editForm.get(['suppsReceived'])!.value,
      suppsUsed: this.editForm.get(['suppsUsed'])!.value,
      suppsDamagedLost: this.editForm.get(['suppsDamagedLost'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      chv: this.editForm.get(['chv'])!.value,
      period: this.editForm.get(['period'])!.value,
    };
  }
}
