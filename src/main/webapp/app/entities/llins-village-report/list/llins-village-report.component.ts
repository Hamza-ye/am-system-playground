import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILlinsVillageReport } from '../llins-village-report.model';
import { LlinsVillageReportService } from '../service/llins-village-report.service';
import { LlinsVillageReportDeleteDialogComponent } from '../delete/llins-village-report-delete-dialog.component';

@Component({
  selector: 'app-llins-village-report',
  templateUrl: './llins-village-report.component.html',
})
export class LlinsVillageReportComponent implements OnInit {
  llinsVillageReports?: ILlinsVillageReport[];
  isLoading = false;

  constructor(protected llinsVillageReportService: LlinsVillageReportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.llinsVillageReportService.query().subscribe(
      (res: HttpResponse<ILlinsVillageReport[]>) => {
        this.isLoading = false;
        this.llinsVillageReports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILlinsVillageReport): number {
    return item.id!;
  }

  delete(llinsVillageReport: ILlinsVillageReport): void {
    const modalRef = this.modalService.open(LlinsVillageReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.llinsVillageReport = llinsVillageReport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
