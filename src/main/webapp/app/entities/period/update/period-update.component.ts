import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPeriod, Period } from '../period.model';
import { PeriodService } from '../service/period.service';
import { IPeriodType } from 'app/entities/period-type/period-type.model';
import { PeriodTypeService } from 'app/entities/period-type/service/period-type.service';

@Component({
  selector: 'app-period-update',
  templateUrl: './period-update.component.html',
})
export class PeriodUpdateComponent implements OnInit {
  isSaving = false;

  periodTypesSharedCollection: IPeriodType[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    periodType: [null, Validators.required],
  });

  constructor(
    protected periodService: PeriodService,
    protected periodTypeService: PeriodTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ period }) => {
      this.updateForm(period);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const period = this.createFromForm();
    if (period.id !== undefined) {
      this.subscribeToSaveResponse(this.periodService.update(period));
    } else {
      this.subscribeToSaveResponse(this.periodService.create(period));
    }
  }

  trackPeriodTypeById(index: number, item: IPeriodType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriod>>): void {
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

  protected updateForm(period: IPeriod): void {
    this.editForm.patchValue({
      id: period.id,
      name: period.name,
      startDate: period.startDate,
      endDate: period.endDate,
      periodType: period.periodType,
    });

    this.periodTypesSharedCollection = this.periodTypeService.addPeriodTypeToCollectionIfMissing(
      this.periodTypesSharedCollection,
      period.periodType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.periodTypeService
      .query()
      .pipe(map((res: HttpResponse<IPeriodType[]>) => res.body ?? []))
      .pipe(
        map((periodTypes: IPeriodType[]) =>
          this.periodTypeService.addPeriodTypeToCollectionIfMissing(periodTypes, this.editForm.get('periodType')!.value)
        )
      )
      .subscribe((periodTypes: IPeriodType[]) => (this.periodTypesSharedCollection = periodTypes));
  }

  protected createFromForm(): IPeriod {
    return {
      ...new Period(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      periodType: this.editForm.get(['periodType'])!.value,
    };
  }
}
