import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileResource } from '../file-resource.model';
import { FileResourceService } from '../service/file-resource.service';

@Component({
  templateUrl: './file-resource-delete-dialog.component.html',
})
export class FileResourceDeleteDialogComponent {
  fileResource?: IFileResource;

  constructor(protected fileResourceService: FileResourceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileResourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
