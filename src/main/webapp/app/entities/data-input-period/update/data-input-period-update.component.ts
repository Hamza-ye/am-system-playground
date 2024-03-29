import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDataInputPeriod, DataInputPeriod } from '../data-input-period.model';
import { DataInputPeriodService } from '../service/data-input-period.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IDataSet } from 'app/entities/data-set/data-set.model';
import { DataSetService } from 'app/entities/data-set/service/data-set.service';

@Component({
  selector: 'app-data-input-period-update',
  templateUrl: './data-input-period-update.component.html',
})
export class DataInputPeriodUpdateComponent implements OnInit {
  isSaving = false;

  periodsSharedCollection: IPeriod[] = [];
  dataSetsSharedCollection: IDataSet[] = [];

  editForm = this.fb.group({
    id: [],
    openingDate: [],
    closingDate: [],
    period: [],
    dataSet: [],
  });

  constructor(
    protected dataInputPeriodService: DataInputPeriodService,
    protected periodService: PeriodService,
    protected dataSetService: DataSetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dataInputPeriod }) => {
      this.updateForm(dataInputPeriod);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dataInputPeriod = this.createFromForm();
    if (dataInputPeriod.id !== undefined) {
      this.subscribeToSaveResponse(this.dataInputPeriodService.update(dataInputPeriod));
    } else {
      this.subscribeToSaveResponse(this.dataInputPeriodService.create(dataInputPeriod));
    }
  }

  trackPeriodById(index: number, item: IPeriod): number {
    return item.id!;
  }

  trackDataSetById(index: number, item: IDataSet): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDataInputPeriod>>): void {
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

  protected updateForm(dataInputPeriod: IDataInputPeriod): void {
    this.editForm.patchValue({
      id: dataInputPeriod.id,
      openingDate: dataInputPeriod.openingDate,
      closingDate: dataInputPeriod.closingDate,
      period: dataInputPeriod.period,
      dataSet: dataInputPeriod.dataSet,
    });

    this.periodsSharedCollection = this.periodService.addPeriodToCollectionIfMissing(this.periodsSharedCollection, dataInputPeriod.period);
    this.dataSetsSharedCollection = this.dataSetService.addDataSetToCollectionIfMissing(
      this.dataSetsSharedCollection,
      dataInputPeriod.dataSet
    );
  }

  protected loadRelationshipsOptions(): void {
    this.periodService
      .query()
      .pipe(map((res: HttpResponse<IPeriod[]>) => res.body ?? []))
      .pipe(map((periods: IPeriod[]) => this.periodService.addPeriodToCollectionIfMissing(periods, this.editForm.get('period')!.value)))
      .subscribe((periods: IPeriod[]) => (this.periodsSharedCollection = periods));

    this.dataSetService
      .query()
      .pipe(map((res: HttpResponse<IDataSet[]>) => res.body ?? []))
      .pipe(
        map((dataSets: IDataSet[]) => this.dataSetService.addDataSetToCollectionIfMissing(dataSets, this.editForm.get('dataSet')!.value))
      )
      .subscribe((dataSets: IDataSet[]) => (this.dataSetsSharedCollection = dataSets));
  }

  protected createFromForm(): IDataInputPeriod {
    return {
      ...new DataInputPeriod(),
      id: this.editForm.get(['id'])!.value,
      openingDate: this.editForm.get(['openingDate'])!.value,
      closingDate: this.editForm.get(['closingDate'])!.value,
      period: this.editForm.get(['period'])!.value,
      dataSet: this.editForm.get(['dataSet'])!.value,
    };
  }
}
