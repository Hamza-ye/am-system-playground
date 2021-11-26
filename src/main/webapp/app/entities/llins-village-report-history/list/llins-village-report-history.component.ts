import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsVillageReportHistory } from '../llins-village-report-history.model';
import { LlinsVillageReportHistoryService } from '../service/llins-village-report-history.service';
import { LlinsVillageReportHistoryDeleteDialogComponent } from '../delete/llins-village-report-history-delete-dialog.component';

@Component({
  selector: 'app-llins-village-report-history',
  templateUrl: './llins-village-report-history.component.html',
})
export class LlinsVillageReportHistoryComponent implements OnInit {
  llinsVillageReportHistories?: ILlinsVillageReportHistory[];
  isLoading = false;

  constructor(protected llinsVillageReportHistoryService: LlinsVillageReportHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.llinsVillageReportHistoryService.query().subscribe(
      (res: HttpResponse<ILlinsVillageReportHistory[]>) => {
        this.isLoading = false;
        this.llinsVillageReportHistories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILlinsVillageReportHistory): number {
    return item.id!;
  }

  delete(llinsVillageReportHistory: ILlinsVillageReportHistory): void {
    const modalRef = this.modalService.open(LlinsVillageReportHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.llinsVillageReportHistory = llinsVillageReportHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
