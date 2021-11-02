import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMalariaCasesReport } from '../malaria-cases-report.model';
import { MalariaCasesReportService } from '../service/malaria-cases-report.service';
import { MalariaCasesReportDeleteDialogComponent } from '../delete/malaria-cases-report-delete-dialog.component';

@Component({
  selector: 'app-malaria-cases-report',
  templateUrl: './malaria-cases-report.component.html',
})
export class MalariaCasesReportComponent implements OnInit {
  malariaCasesReports?: IMalariaCasesReport[];
  isLoading = false;

  constructor(protected malariaCasesReportService: MalariaCasesReportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.malariaCasesReportService.query().subscribe(
      (res: HttpResponse<IMalariaCasesReport[]>) => {
        this.isLoading = false;
        this.malariaCasesReports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMalariaCasesReport): number {
    return item.id!;
  }

  delete(malariaCasesReport: IMalariaCasesReport): void {
    const modalRef = this.modalService.open(MalariaCasesReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.malariaCasesReport = malariaCasesReport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
