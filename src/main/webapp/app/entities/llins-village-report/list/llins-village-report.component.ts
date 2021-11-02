import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILLINSVillageReport } from '../llins-village-report.model';
import { LLINSVillageReportService } from '../service/llins-village-report.service';
import { LLINSVillageReportDeleteDialogComponent } from '../delete/llins-village-report-delete-dialog.component';

@Component({
  selector: 'app-llins-village-report',
  templateUrl: './llins-village-report.component.html',
})
export class LLINSVillageReportComponent implements OnInit {
  lLINSVillageReports?: ILLINSVillageReport[];
  isLoading = false;

  constructor(protected lLINSVillageReportService: LLINSVillageReportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.lLINSVillageReportService.query().subscribe(
      (res: HttpResponse<ILLINSVillageReport[]>) => {
        this.isLoading = false;
        this.lLINSVillageReports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILLINSVillageReport): number {
    return item.id!;
  }

  delete(lLINSVillageReport: ILLINSVillageReport): void {
    const modalRef = this.modalService.open(LLINSVillageReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lLINSVillageReport = lLINSVillageReport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
