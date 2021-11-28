import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContentPage } from '../content-page.model';
import { ContentPageService } from '../service/content-page.service';

@Component({
  templateUrl: './content-page-delete-dialog.component.html',
})
export class ContentPageDeleteDialogComponent {
  contentPage?: IContentPage;

  constructor(protected contentPageService: ContentPageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contentPageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
