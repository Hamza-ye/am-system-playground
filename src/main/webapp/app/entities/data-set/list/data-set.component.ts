import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDataSet } from '../data-set.model';
import { DataSetService } from '../service/data-set.service';
import { DataSetDeleteDialogComponent } from '../delete/data-set-delete-dialog.component';

@Component({
  selector: 'app-data-set',
  templateUrl: './data-set.component.html',
})
export class DataSetComponent implements OnInit {
  dataSets?: IDataSet[];
  isLoading = false;

  constructor(protected dataSetService: DataSetService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dataSetService.query().subscribe(
      (res: HttpResponse<IDataSet[]>) => {
        this.isLoading = false;
        this.dataSets = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDataSet): number {
    return item.id!;
  }

  delete(dataSet: IDataSet): void {
    const modalRef = this.modalService.open(DataSetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dataSet = dataSet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
