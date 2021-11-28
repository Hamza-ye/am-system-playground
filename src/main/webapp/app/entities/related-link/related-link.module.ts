import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RelatedLinkComponent } from './list/related-link.component';
import { RelatedLinkDetailComponent } from './detail/related-link-detail.component';
import { RelatedLinkUpdateComponent } from './update/related-link-update.component';
import { RelatedLinkDeleteDialogComponent } from './delete/related-link-delete-dialog.component';
import { RelatedLinkRoutingModule } from './route/related-link-routing.module';

@NgModule({
  imports: [SharedModule, RelatedLinkRoutingModule],
  declarations: [RelatedLinkComponent, RelatedLinkDetailComponent, RelatedLinkUpdateComponent, RelatedLinkDeleteDialogComponent],
  entryComponents: [RelatedLinkDeleteDialogComponent],
})
export class RelatedLinkModule {}
