import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChv } from '../chv.model';
import { ChvService } from '../service/chv.service';
import { ChvDeleteDialogComponent } from '../delete/chv-delete-dialog.component';

@Component({
  selector: 'app-chv',
  templateUrl: './chv.component.html',
})
export class ChvComponent implements OnInit {
  chvs?: IChv[];
  isLoading = false;

  constructor(protected chvService: ChvService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.chvService.query().subscribe(
      (res: HttpResponse<IChv[]>) => {
        this.isLoading = false;
        this.chvs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IChv): number {
    return item.id!;
  }

  delete(chv: IChv): void {
    const modalRef = this.modalService.open(ChvDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chv = chv;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
