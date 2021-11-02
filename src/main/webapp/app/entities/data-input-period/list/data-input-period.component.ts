import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataInputPeriod } from '../data-input-period.model';
import { DataInputPeriodService } from '../service/data-input-period.service';
import { DataInputPeriodDeleteDialogComponent } from '../delete/data-input-period-delete-dialog.component';

@Component({
  selector: 'app-data-input-period',
  templateUrl: './data-input-period.component.html',
})
export class DataInputPeriodComponent implements OnInit {
  dataInputPeriods?: IDataInputPeriod[];
  isLoading = false;

  constructor(protected dataInputPeriodService: DataInputPeriodService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dataInputPeriodService.query().subscribe(
      (res: HttpResponse<IDataInputPeriod[]>) => {
        this.isLoading = false;
        this.dataInputPeriods = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDataInputPeriod): number {
    return item.id!;
  }

  delete(dataInputPeriod: IDataInputPeriod): void {
    const modalRef = this.modalService.open(DataInputPeriodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dataInputPeriod = dataInputPeriod;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
