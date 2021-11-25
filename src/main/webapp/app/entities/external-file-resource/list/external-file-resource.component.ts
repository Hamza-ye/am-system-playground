import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExternalFileResource } from '../external-file-resource.model';
import { ExternalFileResourceService } from '../service/external-file-resource.service';
import { ExternalFileResourceDeleteDialogComponent } from '../delete/external-file-resource-delete-dialog.component';

@Component({
  selector: 'app-external-file-resource',
  templateUrl: './external-file-resource.component.html',
})
export class ExternalFileResourceComponent implements OnInit {
  externalFileResources?: IExternalFileResource[];
  isLoading = false;

  constructor(protected externalFileResourceService: ExternalFileResourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.externalFileResourceService.query().subscribe(
      (res: HttpResponse<IExternalFileResource[]>) => {
        this.isLoading = false;
        this.externalFileResources = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IExternalFileResource): number {
    return item.id!;
  }

  delete(externalFileResource: IExternalFileResource): void {
    const modalRef = this.modalService.open(ExternalFileResourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.externalFileResource = externalFileResource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
