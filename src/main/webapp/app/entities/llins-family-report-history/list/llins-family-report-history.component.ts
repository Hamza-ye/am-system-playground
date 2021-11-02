import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSFamilyReportHistory } from '../llins-family-report-history.model';
import { LLINSFamilyReportHistoryService } from '../service/llins-family-report-history.service';
import { LLINSFamilyReportHistoryDeleteDialogComponent } from '../delete/llins-family-report-history-delete-dialog.component';

@Component({
  selector: 'app-llins-family-report-history',
  templateUrl: './llins-family-report-history.component.html',
})
export class LLINSFamilyReportHistoryComponent implements OnInit {
  lLINSFamilyReportHistories?: ILLINSFamilyReportHistory[];
  isLoading = false;

  constructor(protected lLINSFamilyReportHistoryService: LLINSFamilyReportHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lLINSFamilyReportHistoryService.query().subscribe(
      (res: HttpResponse<ILLINSFamilyReportHistory[]>) => {
        this.isLoading = false;
        this.lLINSFamilyReportHistories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILLINSFamilyReportHistory): number {
    return item.id!;
  }

  delete(lLINSFamilyReportHistory: ILLINSFamilyReportHistory): void {
    const modalRef = this.modalService.open(LLINSFamilyReportHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lLINSFamilyReportHistory = lLINSFamilyReportHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
