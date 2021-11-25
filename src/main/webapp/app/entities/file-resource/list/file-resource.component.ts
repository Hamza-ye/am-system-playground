import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileResource } from '../file-resource.model';
import { FileResourceService } from '../service/file-resource.service';
import { FileResourceDeleteDialogComponent } from '../delete/file-resource-delete-dialog.component';

@Component({
  selector: 'app-file-resource',
  templateUrl: './file-resource.component.html',
})
export class FileResourceComponent implements OnInit {
  fileResources?: IFileResource[];
  isLoading = false;

  constructor(protected fileResourceService: FileResourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fileResourceService.query().subscribe(
      (res: HttpResponse<IFileResource[]>) => {
        this.isLoading = false;
        this.fileResources = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFileResource): number {
    return item.id!;
  }

  delete(fileResource: IFileResource): void {
    const modalRef = this.modalService.open(FileResourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fileResource = fileResource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
