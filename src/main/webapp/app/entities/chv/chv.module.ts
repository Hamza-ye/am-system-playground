import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CHVComponent } from './list/chv.component';
import { CHVDetailComponent } from './detail/chv-detail.component';
import { CHVUpdateComponent } from './update/chv-update.component';
import { CHVDeleteDialogComponent } from './delete/chv-delete-dialog.component';
import { CHVRoutingModule } from './route/chv-routing.module';

@NgModule({
  imports: [SharedModule, CHVRoutingModule],
  declarations: [CHVComponent, CHVDetailComponent, CHVUpdateComponent, CHVDeleteDialogComponent],
  entryComponents: [CHVDeleteDialogComponent],
})
export class CHVModule {}
