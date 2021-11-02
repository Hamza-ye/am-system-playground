import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDengueCasesReport } from '../dengue-cases-report.model';
import { DengueCasesReportService } from '../service/dengue-cases-report.service';
import { DengueCasesReportDeleteDialogComponent } from '../delete/dengue-cases-report-delete-dialog.component';

@Component({
  selector: 'app-dengue-cases-report',
  templateUrl: './dengue-cases-report.component.html',
})
export class DengueCasesReportComponent implements OnInit {
  dengueCasesReports?: IDengueCasesReport[];
  isLoading = false;

  constructor(protected dengueCasesReportService: DengueCasesReportService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dengueCasesReportService.query().subscribe(
      (res: HttpResponse<IDengueCasesReport[]>) => {
        this.isLoading = false;
        this.dengueCasesReports = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDengueCasesReport): number {
    return item.id!;
  }

  delete(dengueCasesReport: IDengueCasesReport): void {
    const modalRef = this.modalService.open(DengueCasesReportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dengueCasesReport = dengueCasesReport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
