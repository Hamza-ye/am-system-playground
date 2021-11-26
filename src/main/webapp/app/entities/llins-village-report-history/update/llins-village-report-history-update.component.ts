import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILlinsVillageReportHistory, LlinsVillageReportHistory } from '../llins-village-report-history.model';
import { LlinsVillageReportHistoryService } from '../service/llins-village-report-history.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IWorkingDay } from 'app/entities/working-day/working-day.model';
import { WorkingDayService } from 'app/entities/working-day/service/working-day.service';
import { ILlinsVillageReport } from 'app/entities/llins-village-report/llins-village-report.model';
import { LlinsVillageReportService } from 'app/entities/llins-village-report/service/llins-village-report.service';

@Component({
  selector: 'app-llins-village-report-history-update',
  templateUrl: './llins-village-report-history-update.component.html',
})
export class LlinsVillageReportHistoryUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  workingDaysSharedCollection: IWorkingDay[] = [];
  llinsVillageReportsSharedCollection: ILlinsVillageReport[] = [];

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
    createdBy: [],
    lastUpdatedBy: [],
    dayReached: [null, Validators.required],
    llinsVillageReport: [null, Validators.required],
  });

  constructor(
    protected llinsVillageReportHistoryService: LlinsVillageReportHistoryService,
    protected userService: UserService,
    protected workingDayService: WorkingDayService,
    protected llinsVillageReportService: LlinsVillageReportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ llinsVillageReportHistory }) => {
      if (llinsVillageReportHistory.id === undefined) {
        const today = dayjs().startOf('day');
        llinsVillageReportHistory.created = today;
        llinsVillageReportHistory.lastUpdated = today;
      }

      this.updateForm(llinsVillageReportHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const llinsVillageReportHistory = this.createFromForm();
    if (llinsVillageReportHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.llinsVillageReportHistoryService.update(llinsVillageReportHistory));
    } else {
      this.subscribeToSaveResponse(this.llinsVillageReportHistoryService.create(llinsVillageReportHistory));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackWorkingDayById(index: number, item: IWorkingDay): number {
    return item.id!;
  }

  trackLlinsVillageReportById(index: number, item: ILlinsVillageReport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILlinsVillageReportHistory>>): void {
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

  protected updateForm(llinsVillageReportHistory: ILlinsVillageReportHistory): void {
    this.editForm.patchValue({
      id: llinsVillageReportHistory.id,
      uid: llinsVillageReportHistory.uid,
      created: llinsVillageReportHistory.created ? llinsVillageReportHistory.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: llinsVillageReportHistory.lastUpdated ? llinsVillageReportHistory.lastUpdated.format(DATE_TIME_FORMAT) : null,
      houses: llinsVillageReportHistory.houses,
      residentHousehold: llinsVillageReportHistory.residentHousehold,
      idpsHousehold: llinsVillageReportHistory.idpsHousehold,
      maleIndividuals: llinsVillageReportHistory.maleIndividuals,
      femaleIndividuals: llinsVillageReportHistory.femaleIndividuals,
      lessThan5Males: llinsVillageReportHistory.lessThan5Males,
      lessThan5Females: llinsVillageReportHistory.lessThan5Females,
      pregnantWomen: llinsVillageReportHistory.pregnantWomen,
      quantityReceived: llinsVillageReportHistory.quantityReceived,
      createdBy: llinsVillageReportHistory.createdBy,
      lastUpdatedBy: llinsVillageReportHistory.lastUpdatedBy,
      dayReached: llinsVillageReportHistory.dayReached,
      llinsVillageReport: llinsVillageReportHistory.llinsVillageReport,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(
      this.usersSharedCollection,
      llinsVillageReportHistory.createdBy,
      llinsVillageReportHistory.lastUpdatedBy
    );
    this.workingDaysSharedCollection = this.workingDayService.addWorkingDayToCollectionIfMissing(
      this.workingDaysSharedCollection,
      llinsVillageReportHistory.dayReached
    );
    this.llinsVillageReportsSharedCollection = this.llinsVillageReportService.addLlinsVillageReportToCollectionIfMissing(
      this.llinsVillageReportsSharedCollection,
      llinsVillageReportHistory.llinsVillageReport
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

    this.llinsVillageReportService
      .query()
      .pipe(map((res: HttpResponse<ILlinsVillageReport[]>) => res.body ?? []))
      .pipe(
        map((llinsVillageReports: ILlinsVillageReport[]) =>
          this.llinsVillageReportService.addLlinsVillageReportToCollectionIfMissing(
            llinsVillageReports,
            this.editForm.get('llinsVillageReport')!.value
          )
        )
      )
      .subscribe((llinsVillageReports: ILlinsVillageReport[]) => (this.llinsVillageReportsSharedCollection = llinsVillageReports));
  }

  protected createFromForm(): ILlinsVillageReportHistory {
    return {
      ...new LlinsVillageReportHistory(),
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
      createdBy: this.editForm.get(['createdBy'])!.value,
      lastUpdatedBy: this.editForm.get(['lastUpdatedBy'])!.value,
      dayReached: this.editForm.get(['dayReached'])!.value,
      llinsVillageReport: this.editForm.get(['llinsVillageReport'])!.value,
    };
  }
}
