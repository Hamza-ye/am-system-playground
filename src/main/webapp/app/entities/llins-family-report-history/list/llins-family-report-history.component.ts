import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsFamilyReportHistory } from '../llins-family-report-history.model';
import { LlinsFamilyReportHistoryService } from '../service/llins-family-report-history.service';
import { LlinsFamilyReportHistoryDeleteDialogComponent } from '../delete/llins-family-report-history-delete-dialog.component';

@Component({
  selector: 'app-llins-family-report-history',
  templateUrl: './llins-family-report-history.component.html',
})
export class LlinsFamilyReportHistoryComponent implements OnInit {
  llinsFamilyReportHistories?: ILlinsFamilyReportHistory[];
  isLoading = false;

  constructor(protected llinsFamilyReportHistoryService: LlinsFamilyReportHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.llinsFamilyReportHistoryService.query().subscribe(
      (res: HttpResponse<ILlinsFamilyReportHistory[]>) => {
        this.isLoading = false;
        this.llinsFamilyReportHistories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILlinsFamilyReportHistory): number {
    return item.id!;
  }

  delete(llinsFamilyReportHistory: ILlinsFamilyReportHistory): void {
    const modalRef = this.modalService.open(LlinsFamilyReportHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.llinsFamilyReportHistory = llinsFamilyReportHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
