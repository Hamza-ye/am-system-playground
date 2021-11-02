import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMalariaUnit } from '../malaria-unit.model';
import { MalariaUnitService } from '../service/malaria-unit.service';
import { MalariaUnitDeleteDialogComponent } from '../delete/malaria-unit-delete-dialog.component';

@Component({
  selector: 'app-malaria-unit',
  templateUrl: './malaria-unit.component.html',
})
export class MalariaUnitComponent implements OnInit {
  malariaUnits?: IMalariaUnit[];
  isLoading = false;

  constructor(protected malariaUnitService: MalariaUnitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.malariaUnitService.query().subscribe(
      (res: HttpResponse<IMalariaUnit[]>) => {
        this.isLoading = false;
        this.malariaUnits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMalariaUnit): number {
    return item.id!;
  }

  delete(malariaUnit: IMalariaUnit): void {
    const modalRef = this.modalService.open(MalariaUnitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.malariaUnit = malariaUnit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
