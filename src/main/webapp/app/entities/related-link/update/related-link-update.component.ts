import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRelatedLink, RelatedLink } from '../related-link.model';
import { RelatedLinkService } from '../service/related-link.service';

@Component({
  selector: 'app-related-link-update',
  templateUrl: './related-link-update.component.html',
})
export class RelatedLinkUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uid: [null, [Validators.required, Validators.maxLength(11)]],
    name: [],
    created: [],
    lastUpdated: [],
    url: [],
  });

  constructor(protected relatedLinkService: RelatedLinkService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ relatedLink }) => {
      if (relatedLink.id === undefined) {
        const today = dayjs().startOf('day');
        relatedLink.created = today;
        relatedLink.lastUpdated = today;
      }

      this.updateForm(relatedLink);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const relatedLink = this.createFromForm();
    if (relatedLink.id !== undefined) {
      this.subscribeToSaveResponse(this.relatedLinkService.update(relatedLink));
    } else {
      this.subscribeToSaveResponse(this.relatedLinkService.create(relatedLink));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRelatedLink>>): void {
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

  protected updateForm(relatedLink: IRelatedLink): void {
    this.editForm.patchValue({
      id: relatedLink.id,
      uid: relatedLink.uid,
      name: relatedLink.name,
      created: relatedLink.created ? relatedLink.created.format(DATE_TIME_FORMAT) : null,
      lastUpdated: relatedLink.lastUpdated ? relatedLink.lastUpdated.format(DATE_TIME_FORMAT) : null,
      url: relatedLink.url,
    });
  }

  protected createFromForm(): IRelatedLink {
    return {
      ...new RelatedLink(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      name: this.editForm.get(['name'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value
        ? dayjs(this.editForm.get(['lastUpdated'])!.value, DATE_TIME_FORMAT)
        : undefined,
      url: this.editForm.get(['url'])!.value,
    };
  }
}
