import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFingerprint } from '../fingerprint.model';
import { FingerprintService } from '../service/fingerprint.service';
import { FingerprintDeleteDialogComponent } from '../delete/fingerprint-delete-dialog.component';

@Component({
  selector: 'app-fingerprint',
  templateUrl: './fingerprint.component.html',
})
export class FingerprintComponent implements OnInit {
  fingerprints?: IFingerprint[];
  isLoading = false;

  constructor(protected fingerprintService: FingerprintService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fingerprintService.query().subscribe(
      (res: HttpResponse<IFingerprint[]>) => {
        this.isLoading = false;
        this.fingerprints = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFingerprint): number {
    return item.id!;
  }

  delete(fingerprint: IFingerprint): void {
    const modalRef = this.modalService.open(FingerprintDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fingerprint = fingerprint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
