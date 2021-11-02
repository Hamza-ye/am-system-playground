import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICHV } from '../chv.model';
import { CHVService } from '../service/chv.service';
import { CHVDeleteDialogComponent } from '../delete/chv-delete-dialog.component';

@Component({
  selector: 'app-chv',
  templateUrl: './chv.component.html',
})
export class CHVComponent implements OnInit {
  cHVS?: ICHV[];
  isLoading = false;

  constructor(protected cHVService: CHVService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cHVService.query().subscribe(
      (res: HttpResponse<ICHV[]>) => {
        this.isLoading = false;
        this.cHVS = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICHV): number {
    return item.id!;
  }

  delete(cHV: ICHV): void {
    const modalRef = this.modalService.open(CHVDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cHV = cHV;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
