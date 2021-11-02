import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILLINSVillageReportHistory, LLINSVillageReportHistory } from '../llins-village-report-history.model';
import { LLINSVillageReportHistoryService } from '../service/llins-village-report-history.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILLINSVillageReport } from 'app/entities/llins-village-report/llins-village-report.model';
import { LLINSVillageReportService } from 'app/entities/llins-village-report/service/llins-village-report.service';

@Component({
  selector: 'app-llins-village-report-history-update',
  templateUrl: './llins-village-report-history-update.component.html',
})
export class LLINSVillageReportHistoryUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  lLINSVillageReportsSharedCollection: ILLINSVillageReport[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    created: [],
    lastUpdated: [],
    houses: [null, [Validators.required, Validators.min(0)]],
    residentHousehold: [null, [Validators.required, Validators.min(0)]],
    idpsHousehold: [null, [Validators.required, Validators.min(0)]],
    maleIndividuals: [null, [Validators.min(0)]],
    femaleIndividuals: [null, [Validators.min(0)]],
    lessThan5Males: [null, [Validators.min(0)]],
    lessThan5Females: [null, [Validators.min(0)]],
    pregnantWomen: [null, [Validators.min(0)]],
    quantityReceived: [null, [Validators.required, Validators.min(0)]],
    user: [],
    lastUpdatedBy: [],
    dayReached: [null, Validators.required],
    llinsVillageReport: [null, Validators.required],
  });

  constructor(
    protected lLINSVillageReportHistoryService: LLINSVillageReportHistoryService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected lLINSVillageReportService: LLINSVillageReportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lLINSVillageReportHistory }) => {
      if (lLINSVillageReportHistory.id === undefined) {
        const today = dayjs().startOf('day');
        lLINSVillageReportHistory.created = today;
        lLINSVillageReportHistory.lastUpdated = today;
      }

      this.updateForm(lLINSVillageReportHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lLINSVillageReportHistory = this.createFromForm();
    if (lLINSVillageReportHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.lLINSVillageReportHistoryService.update(lLINSVillageReportHistory));
    } else {
      this.subscribeToSaveResponse(this.lLINSVillageReportHistoryService.create(lLINSVillageReportHistory));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLLINSVillageReportById(index: number, item: ILLINSVillageReport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILLINSVillageReportHistory>>): void {
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

  protected updateForm(lLINSVillageReportHistory: ILLINSVillageReportHistory): void {
    this.editForm.patchValue({
      id: lLINSVillageReportHistory.id,
      uid: lLINSVillageReportHistory.uid,
      created: lLINSVillageReportHistory.created ? lLINSVillageReportHistory.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: lLINSVillageReportHistory.lastUpdated ? lLINSVillageReportHistory.lastUpdated.format(DATE_TIME_FORMAT) : null,
      houses: lLINSVillageReportHistory.houses,
      residentHousehold: lLINSVillageReportHistory.residentHousehold,
      idpsHousehold: lLINSVillageReportHistory.idpsHousehold,
      maleIndividuals: lLINSVillageReportHistory.maleIndividuals,
      femaleIndividuals: lLINSVillageReportHistory.femaleIndividuals,
      lessThan5Males: lLINSVillageReportHistory.lessThan5Males,
      lessThan5Females: lLINSVillageReportHistory.lessThan5Females,
      pregnantWomen: lLINSVillageReportHistory.pregnantWomen,
      quantityReceived: lLINSVillageReportHistory.quantityReceived,
      user: lLINSVillageReportHistory.user,
      lastUpdatedBy: lLINSVillageReportHistory.lastUpdatedBy,
      dayReached: lLINSVillageReportHistory.dayReached,
      llinsVillageReport: lLINSVillageReportHistory.llinsVillageReport,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      lLINSVillageReportHistory.user,
      lLINSVillageReportHistory.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      lLINSVillageReportHistory.dayReached
    );
    this.lLINSVillageReportsSharedCollection = this.lLINSVillageReportService.addLLINSVillageReportToCollectionIfMissing(
      this.lLINSVillageReportsSharedCollection,
      lLINSVillageReportHistory.llinsVillageReport
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

    this.workingDayService
      .query()
      .pipe(map((res: HttpResponse<IWorkingDay[]>) => res.body ?? []))
      .pipe(
        map((workingDays: IWorkingDay[]) =>
          this.workingDayService.addWorkingDayToCollectionIfMissing(workingDays, this.editForm.get('dayReached')!.value)
        )
      )
      .subscribe((workingDays: IWorkingDay[]) => (this.workingDaysSharedCollection = workingDays));

    this.lLINSVillageReportService
      .query()
      .pipe(map((res: HttpResponse<ILLINSVillageReport[]>) => res.body ?? []))
      .pipe(
        map((lLINSVillageReports: ILLINSVillageReport[]) =>
          this.lLINSVillageReportService.addLLINSVillageReportToCollectionIfMissing(
            lLINSVillageReports,
            this.editForm.get('llinsVillageReport')!.value
          )
        )
      )
      .subscribe((lLINSVillageReports: ILLINSVillageReport[]) => (this.lLINSVillageReportsSharedCollection = lLINSVillageReports));
  }

  protected createFromForm(): ILLINSVillageReportHistory {
    return {
      ...new LLINSVillageReportHistory(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      houses: this.editForm.get(['houses'])!.value,
      residentHousehold: this.editForm.get(['residentHousehold'])!.value,
      idpsHousehold: this.editForm.get(['idpsHousehold'])!.value,
      maleIndividuals: this.editForm.get(['maleIndividuals'])!.value,
      femaleIndividuals: this.editForm.get(['femaleIndividuals'])!.value,
      lessThan5Males: this.editForm.get(['lessThan5Males'])!.value,
      lessThan5Females: this.editForm.get(['lessThan5Females'])!.value,
      pregnantWomen: this.editForm.get(['pregnantWomen'])!.value,
      quantityReceived: this.editForm.get(['quantityReceived'])!.value,
      user: this.editForm.get(['user'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayReached: this.editForm.get(['dayReached'])!.value,
      llinsVillageReport: this.editForm.get(['llinsVillageReport'])!.value,
    };
  }
}
