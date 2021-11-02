import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSVillageReportHistory } from '../llins-village-report-history.model';
import { LLINSVillageReportHistoryService } from '../service/llins-village-report-history.service';
import { LLINSVillageReportHistoryDeleteDialogComponent } from '../delete/llins-village-report-history-delete-dialog.component';

@Component({
  selector: 'app-llins-village-report-history',
  templateUrl: './llins-village-report-history.component.html',
})
export class LLINSVillageReportHistoryComponent implements OnInit {
  lLINSVillageReportHistories?: ILLINSVillageReportHistory[];
  isLoading = false;

  constructor(protected lLINSVillageReportHistoryService: LLINSVillageReportHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lLINSVillageReportHistoryService.query().subscribe(
      (res: HttpResponse<ILLINSVillageReportHistory[]>) => {
        this.isLoading = false;
        this.lLINSVillageReportHistories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILLINSVillageReportHistory): number {
    return item.id!;
  }

  delete(lLINSVillageReportHistory: ILLINSVillageReportHistory): void {
    const modalRef = this.modalService.open(LLINSVillageReportHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lLINSVillageReportHistory = lLINSVillageReportHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
