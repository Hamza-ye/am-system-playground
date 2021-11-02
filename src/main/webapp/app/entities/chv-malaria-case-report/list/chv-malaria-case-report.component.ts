import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHVMalariaCaseReport } from '../chv-malaria-case-report.model';
import { CHVMalariaCaseReportService } from '../service/chv-malaria-case-report.service';
import { CHVMalariaCaseReportDeleteDialogComponent } from '../delete/chv-malaria-case-report-delete-dialog.component';

@Component({
  selector: 'app-chv-malaria-case-report',
  templateUrl: './chv-malaria-case-report.component.html',
})
export class CHVMalariaCaseReportComponent implements OnInit {
  cHVMalariaCaseReports?: ICHVMalariaCaseReport[];
  isLoading = false;

  constructor(protected cHVMalariaCaseReportService: CHVMalariaCaseReportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cHVMalariaCaseReportService.query().subscribe(
      (res: HttpResponse<ICHVMalariaCaseReport[]>) => {
        this.isLoading = false;
        this.cHVMalariaCaseReports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICHVMalariaCaseReport): number {
    return item.id!;
  }

  delete(cHVMalariaCaseReport: ICHVMalariaCaseReport): void {
    const modalRef = this.modalService.open(CHVMalariaCaseReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cHVMalariaCaseReport = cHVMalariaCaseReport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
