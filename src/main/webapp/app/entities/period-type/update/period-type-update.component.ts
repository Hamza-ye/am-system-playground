import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPeriodType, PeriodType } from '../period-type.model';
import { PeriodTypeService } from '../service/period-type.service';

@Component({
  selector: 'app-period-type-update',
  templateUrl: './period-type-update.component.html',
})
export class PeriodTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected periodTypeService: PeriodTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ periodType }) => {
      this.updateForm(periodType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const periodType = this.createFromForm();
    if (periodType.id !== undefined) {
      this.subscribeToSaveResponse(this.periodTypeService.update(periodType));
    } else {
      this.subscribeToSaveResponse(this.periodTypeService.create(periodType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodType>>): void {
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

  protected updateForm(periodType: IPeriodType): void {
    this.editForm.patchValue({
      id: periodType.id,
      name: periodType.name,
    });
  }

  protected createFromForm(): IPeriodType {
    return {
      ...new PeriodType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
