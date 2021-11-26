import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILLINSFamilyReportHistory, LLINSFamilyReportHistory } from '../llins-family-report-history.model';
import { LLINSFamilyReportHistoryService } from '../service/llins-family-report-history.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSFamilyReport } from 'app/entities/llins-family-report/llins-family-report.model';
import { LLINSFamilyReportService } from 'app/entities/llins-family-report/service/llins-family-report.service';

@Component({
  selector: 'app-llins-family-report-history-update',
  templateUrl: './llins-family-report-history-update.component.html',
})
export class LLINSFamilyReportHistoryUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  lLINSFamilyReportsSharedCollection: ILLINSFamilyReport[] = [];

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
    protected lLINSFamilyReportHistoryService: LLINSFamilyReportHistoryService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected lLINSFamilyReportService: LLINSFamilyReportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSFamilyReportHistory }) => {
      if (lLINSFamilyReportHistory.id === undefined) {
        const today = dayjs().startOf('day');
        lLINSFamilyReportHistory.created = today;
        lLINSFamilyReportHistory.lastUpdated = today;
      }

      this.updateForm(lLINSFamilyReportHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lLINSFamilyReportHistory = this.createFromForm();
    if (lLINSFamilyReportHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.lLINSFamilyReportHistoryService.update(lLINSFamilyReportHistory));
    } else {
      this.subscribeToSaveResponse(this.lLINSFamilyReportHistoryService.create(lLINSFamilyReportHistory));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLLINSFamilyReportById(index: number, item: ILLINSFamilyReport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILLINSFamilyReportHistory>>): void {
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

  protected updateForm(lLINSFamilyReportHistory: ILLINSFamilyReportHistory): void {
    this.editForm.patchValue({
      id: lLINSFamilyReportHistory.id,
      uid: lLINSFamilyReportHistory.uid,
      created: lLINSFamilyReportHistory.created ? lLINSFamilyReportHistory.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: lLINSFamilyReportHistory.lastUpdated ? lLINSFamilyReportHistory.lastUpdated.format(DATE_TIME_FORMAT) : null,
      documentNo: lLINSFamilyReportHistory.documentNo,
      maleIndividuals: lLINSFamilyReportHistory.maleIndividuals,
      femaleIndividuals: lLINSFamilyReportHistory.femaleIndividuals,
      lessThan5Males: lLINSFamilyReportHistory.lessThan5Males,
      lessThan5Females: lLINSFamilyReportHistory.lessThan5Females,
      pregnantWomen: lLINSFamilyReportHistory.pregnantWomen,
      quantityReceived: lLINSFamilyReportHistory.quantityReceived,
      familyType: lLINSFamilyReportHistory.familyType,
      createdBy: lLINSFamilyReportHistory.createdBy,
      lastUpdatedBy: lLINSFamilyReportHistory.lastUpdatedBy,
      dayReached: lLINSFamilyReportHistory.dayReached,
      llinsFamilyReport: lLINSFamilyReportHistory.llinsFamilyReport,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      lLINSFamilyReportHistory.createdBy,
      lLINSFamilyReportHistory.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      lLINSFamilyReportHistory.dayReached
    );
    this.lLINSFamilyReportsSharedCollection = this.lLINSFamilyReportService.addLLINSFamilyReportToCollectionIfMissing(
      this.lLINSFamilyReportsSharedCollection,
      lLINSFamilyReportHistory.llinsFamilyReport
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

    this.lLINSFamilyReportService
      .query()
      .pipe(map((res: HttpResponse<ILLINSFamilyReport[]>) => res.body ?? []))
      .pipe(
        map((lLINSFamilyReports: ILLINSFamilyReport[]) =>
          this.lLINSFamilyReportService.addLLINSFamilyReportToCollectionIfMissing(
            lLINSFamilyReports,
            this.editForm.get('llinsFamilyReport')!.value
          )
        )
      )
      .subscribe((lLINSFamilyReports: ILLINSFamilyReport[]) => (this.lLINSFamilyReportsSharedCollection = lLINSFamilyReports));
  }

  protected createFromForm(): ILLINSFamilyReportHistory {
    return {
      ...new LLINSFamilyReportHistory(),
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
