import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICasesReportClass } from '../cases-report-class.model';
import { CasesReportClassService } from '../service/cases-report-class.service';
import { CasesReportClassDeleteDialogComponent } from '../delete/cases-report-class-delete-dialog.component';

@Component({
  selector: 'app-cases-report-class',
  templateUrl: './cases-report-class.component.html',
})
export class CasesReportClassComponent implements OnInit {
  casesReportClasses?: ICasesReportClass[];
  isLoading = false;

  constructor(protected casesReportClassService: CasesReportClassService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.casesReportClassService.query().subscribe(
      (res: HttpResponse<ICasesReportClass[]>) => {
        this.isLoading = false;
        this.casesReportClasses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICasesReportClass): number {
    return item.id!;
  }

  delete(casesReportClass: ICasesReportClass): void {
    const modalRef = this.modalService.open(CasesReportClassDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.casesReportClass = casesReportClass;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
