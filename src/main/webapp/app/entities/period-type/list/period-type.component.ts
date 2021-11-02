import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeriodType } from '../period-type.model';
import { PeriodTypeService } from '../service/period-type.service';
import { PeriodTypeDeleteDialogComponent } from '../delete/period-type-delete-dialog.component';

@Component({
  selector: 'app-period-type',
  templateUrl: './period-type.component.html',
})
export class PeriodTypeComponent implements OnInit {
  periodTypes?: IPeriodType[];
  isLoading = false;

  constructor(protected periodTypeService: PeriodTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.periodTypeService.query().subscribe(
      (res: HttpResponse<IPeriodType[]>) => {
        this.isLoading = false;
        this.periodTypes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPeriodType): number {
    return item.id!;
  }

  delete(periodType: IPeriodType): void {
    const modalRef = this.modalService.open(PeriodTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.periodType = periodType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
