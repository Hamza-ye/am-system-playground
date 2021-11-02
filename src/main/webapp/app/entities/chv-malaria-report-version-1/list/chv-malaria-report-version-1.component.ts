import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHVMalariaReportVersion1 } from '../chv-malaria-report-version-1.model';
import { CHVMalariaReportVersion1Service } from '../service/chv-malaria-report-version-1.service';
import { CHVMalariaReportVersion1DeleteDialogComponent } from '../delete/chv-malaria-report-version-1-delete-dialog.component';

@Component({
  selector: 'app-chv-malaria-report-version-1',
  templateUrl: './chv-malaria-report-version-1.component.html',
})
export class CHVMalariaReportVersion1Component implements OnInit {
  cHVMalariaReportVersion1s?: ICHVMalariaReportVersion1[];
  isLoading = false;

  constructor(protected cHVMalariaReportVersion1Service: CHVMalariaReportVersion1Service, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cHVMalariaReportVersion1Service.query().subscribe(
      (res: HttpResponse<ICHVMalariaReportVersion1[]>) => {
        this.isLoading = false;
        this.cHVMalariaReportVersion1s = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICHVMalariaReportVersion1): number {
    return item.id!;
  }

  delete(cHVMalariaReportVersion1: ICHVMalariaReportVersion1): void {
    const modalRef = this.modalService.open(CHVMalariaReportVersion1DeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cHVMalariaReportVersion1 = cHVMalariaReportVersion1;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
