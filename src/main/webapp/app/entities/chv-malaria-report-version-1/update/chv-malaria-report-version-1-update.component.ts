import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IChvMalariaReportVersion1, ChvMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { ChvMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IChv } from 'app/entities/chv/chv.model';
import { ChvService } from 'app/entities/chv/service/chv.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';

@Component({
  selector: 'app-chv-malaria-report-version-1-update',
  templateUrl: './chv-malaria-report-version-1-update.component.html',
})
export class ChvMalariaReportVersion1UpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  chvsSharedCollection: IChv[] = [];
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
    protected chvMalariaReportVersion1Service: ChvMalariaReportVersion1Service,
    protected userService: UserService,
    protected chvService: ChvService,
    protected periodService: PeriodService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chvMalariaReportVersion1 }) => {
      if (chvMalariaReportVersion1.id === undefined) {
        const today = dayjs().startOf('day');
        chvMalariaReportVersion1.created = today;
        chvMalariaReportVersion1.lastUpdated = today;
      }

      this.updateForm(chvMalariaReportVersion1);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chvMalariaReportVersion1 = this.createFromForm();
    if (chvMalariaReportVersion1.id !== undefined) {
      this.subscribeToSaveResponse(this.chvMalariaReportVersion1Service.update(chvMalariaReportVersion1));
    } else {
      this.subscribeToSaveResponse(this.chvMalariaReportVersion1Service.create(chvMalariaReportVersion1));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackChvById(index: number, item: IChv): number {
    return item.id!;
  }

  trackPeriodById(index: number, item: IPeriod): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChvMalariaReportVersion1>>): void {
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

  protected updateForm(chvMalariaReportVersion1: IChvMalariaReportVersion1): void {
    this.editForm.patchValue({
      id: chvMalariaReportVersion1.id,
      uid: chvMalariaReportVersion1.uid,
      created: chvMalariaReportVersion1.created ? chvMalariaReportVersion1.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: chvMalariaReportVersion1.lastUpdated ? chvMalariaReportVersion1.lastUpdated.format(DATE_TIME_FORMAT) : null,
      tested: chvMalariaReportVersion1.tested,
      positive: chvMalariaReportVersion1.positive,
      drugsGiven: chvMalariaReportVersion1.drugsGiven,
      suppsGiven: chvMalariaReportVersion1.suppsGiven,
      rdtBalance: chvMalariaReportVersion1.rdtBalance,
      rdtReceived: chvMalariaReportVersion1.rdtReceived,
      rdtUsed: chvMalariaReportVersion1.rdtUsed,
      rdtDamagedLost: chvMalariaReportVersion1.rdtDamagedLost,
      drugsBalance: chvMalariaReportVersion1.drugsBalance,
      drugsReceived: chvMalariaReportVersion1.drugsReceived,
      drugsUsed: chvMalariaReportVersion1.drugsUsed,
      drugsDamagedLost: chvMalariaReportVersion1.drugsDamagedLost,
      suppsBalance: chvMalariaReportVersion1.suppsBalance,
      suppsReceived: chvMalariaReportVersion1.suppsReceived,
      suppsUsed: chvMalariaReportVersion1.suppsUsed,
      suppsDamagedLost: chvMalariaReportVersion1.suppsDamagedLost,
      comment: chvMalariaReportVersion1.comment,
      createdBy: chvMalariaReportVersion1.createdBy,
      lastUpdatedBy: chvMalariaReportVersion1.lastUpdatedBy,
      chv: chvMalariaReportVersion1.chv,
      period: chvMalariaReportVersion1.period,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      chvMalariaReportVersion1.createdBy,
      chvMalariaReportVersion1.lastUpdatedBy
    );
    this.chvsSharedCollection = this.chvService.addChvToCollectionIfMissing(this.chvsSharedCollection, chvMalariaReportVersion1.chv);
    this.periodsSharedCollection = this.periodService.addPeriodToCollectionIfMissing(
      this.periodsSharedCollection,
      chvMalariaReportVersion1.period
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

    this.chvService
      .query()
      .pipe(map((res: HttpResponse<IChv[]>) => res.body ?? []))
      .pipe(map((chvs: IChv[]) => this.chvService.addChvToCollectionIfMissing(chvs, this.editForm.get('chv')!.value)))
      .subscribe((chvs: IChv[]) => (this.chvsSharedCollection = chvs));

    this.periodService
      .query()
      .pipe(map((res: HttpResponse<IPeriod[]>) => res.body ?? []))
      .pipe(map((periods: IPeriod[]) => this.periodService.addPeriodToCollectionIfMissing(periods, this.editForm.get('period')!.value)))
      .subscribe((periods: IPeriod[]) => (this.periodsSharedCollection = periods));
  }

  protected createFromForm(): IChvMalariaReportVersion1 {
    return {
      ...new ChvMalariaReportVersion1(),
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
