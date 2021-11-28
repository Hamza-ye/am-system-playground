import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContentPageComponent } from './list/content-page.component';
import { ContentPageDetailComponent } from './detail/content-page-detail.component';
import { ContentPageUpdateComponent } from './update/content-page-update.component';
import { ContentPageDeleteDialogComponent } from './delete/content-page-delete-dialog.component';
import { ContentPageRoutingModule } from './route/content-page-routing.module';

@NgModule({
  imports: [SharedModule, ContentPageRoutingModule],
  declarations: [ContentPageComponent, ContentPageDetailComponent, ContentPageUpdateComponent, ContentPageDeleteDialogComponent],
  entryComponents: [ContentPageDeleteDialogComponent],
})
export class ContentPageModule {}
