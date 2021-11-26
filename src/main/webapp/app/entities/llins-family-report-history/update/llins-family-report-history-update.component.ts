import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILlinsFamilyReportHistory, LlinsFamilyReportHistory } from '../llins-family-report-history.model';
import { LlinsFamilyReportHistoryService } from '../service/llins-family-report-history.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsFamilyReport } from 'app/entities/llins-family-report/llins-family-report.model';
import { LlinsFamilyReportService } from 'app/entities/llins-family-report/service/llins-family-report.service';

@Component({
  selector: 'app-llins-family-report-history-update',
  templateUrl: './llins-family-report-history-update.component.html',
})
export class LlinsFamilyReportHistoryUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  llinsFamilyReportsSharedCollection: ILlinsFamilyReport[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    documentNo: [],
    maleIndividuals: [null, [Validators.min(0)]],
    femaleIndividuals: [null, [Validators.min(0)]],
    lessThan5Males: [null, [Validators.min(0)]],
    lessThan5Females: [null, [Validators.min(0)]],
    pregnantWomen: [null, [Validators.min(0)]],
    quantityReceived: [null, [Validators.min(0)]],
    familyType: [null, [Validators.required]],
    createdBy: [],
    lastUpdatedBy: [],
    dayReached: [null, Validators.required],
    llinsFamilyReport: [null, Validators.required],
  });

  constructor(
    protected llinsFamilyReportHistoryService: LlinsFamilyReportHistoryService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected llinsFamilyReportService: LlinsFamilyReportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsFamilyReportHistory }) => {
      if (llinsFamilyReportHistory.id === undefined) {
        const today = dayjs().startOf('day');
        llinsFamilyReportHistory.created = today;
        llinsFamilyReportHistory.lastUpdated = today;
      }

      this.updateForm(llinsFamilyReportHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const llinsFamilyReportHistory = this.createFromForm();
    if (llinsFamilyReportHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.llinsFamilyReportHistoryService.update(llinsFamilyReportHistory));
    } else {
      this.subscribeToSaveResponse(this.llinsFamilyReportHistoryService.create(llinsFamilyReportHistory));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLlinsFamilyReportById(index: number, item: ILlinsFamilyReport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILlinsFamilyReportHistory>>): void {
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

  protected updateForm(llinsFamilyReportHistory: ILlinsFamilyReportHistory): void {
    this.editForm.patchValue({
      id: llinsFamilyReportHistory.id,
      uid: llinsFamilyReportHistory.uid,
      created: llinsFamilyReportHistory.created ? llinsFamilyReportHistory.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: llinsFamilyReportHistory.lastUpdated ? llinsFamilyReportHistory.lastUpdated.format(DATE_TIME_FORMAT) : null,
      documentNo: llinsFamilyReportHistory.documentNo,
      maleIndividuals: llinsFamilyReportHistory.maleIndividuals,
      femaleIndividuals: llinsFamilyReportHistory.femaleIndividuals,
      lessThan5Males: llinsFamilyReportHistory.lessThan5Males,
      lessThan5Females: llinsFamilyReportHistory.lessThan5Females,
      pregnantWomen: llinsFamilyReportHistory.pregnantWomen,
      quantityReceived: llinsFamilyReportHistory.quantityReceived,
      familyType: llinsFamilyReportHistory.familyType,
      createdBy: llinsFamilyReportHistory.createdBy,
      lastUpdatedBy: llinsFamilyReportHistory.lastUpdatedBy,
      dayReached: llinsFamilyReportHistory.dayReached,
      llinsFamilyReport: llinsFamilyReportHistory.llinsFamilyReport,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      llinsFamilyReportHistory.createdBy,
      llinsFamilyReportHistory.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      llinsFamilyReportHistory.dayReached
    );
    this.llinsFamilyReportsSharedCollection = this.llinsFamilyReportService.addLlinsFamilyReportToCollectionIfMissing(
      this.llinsFamilyReportsSharedCollection,
      llinsFamilyReportHistory.llinsFamilyReport
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

    this.workingDayService
      .query()
      .pipe(map((res: HttpResponse<IWorkingDay[]>) => res.body ?? []))
      .pipe(
        map((workingDays: IWorkingDay[]) =>
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('dayReached')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.llinsFamilyReportService
      .query()
      .pipe(map((res: HttpResponse<ILlinsFamilyReport[]>) => res.body ?? []))
      .pipe(
        map((llinsFamilyReports: ILlinsFamilyReport[]) =>
          this.llinsFamilyReportService.addLlinsFamilyReportToCollectionIfMissing(
            llinsFamilyReports,
            this.editForm.get('llinsFamilyReport')!.value
          )
        )
      )
      .subscribe((llinsFamilyReports: ILlinsFamilyReport[]) => (this.llinsFamilyReportsSharedCollection = llinsFamilyReports));
  }

  protected createFromForm(): ILlinsFamilyReportHistory {
    return {
      ...new LlinsFamilyReportHistory(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      documentNo: this.editForm.get(['documentNo'])!.value,
      maleIndividuals: this.editForm.get(['maleIndividuals'])!.value,
      femaleIndividuals: this.editForm.get(['femaleIndividuals'])!.value,
      lessThan5Males: this.editForm.get(['lessThan5Males'])!.value,
      lessThan5Females: this.editForm.get(['lessThan5Females'])!.value,
      pregnantWomen: this.editForm.get(['pregnantWomen'])!.value,
      quantityReceived: this.editForm.get(['quantityReceived'])!.value,
      familyType: this.editForm.get(['familyType'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayReached: this.editForm.get(['dayReached'])!.value,
      llinsFamilyReport: this.editForm.get(['llinsFamilyReport'])!.value,
    };
  }
}
