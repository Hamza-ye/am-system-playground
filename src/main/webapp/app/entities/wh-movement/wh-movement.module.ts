import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WHMovementComponent } from './list/wh-movement.component';
import { WHMovementDetailComponent } from './detail/wh-movement-detail.component';
import { WHMovementUpdateComponent } from './update/wh-movement-update.component';
import { WHMovementDeleteDialogComponent } from './delete/wh-movement-delete-dialog.component';
import { WHMovementRoutingModule } from './route/wh-movement-routing.module';

@NgModule({
  imports: [SharedModule, WHMovementRoutingModule],
  declarations: [WHMovementComponent, WHMovementDetailComponent, WHMovementUpdateComponent, WHMovementDeleteDialogComponent],
  entryComponents: [WHMovementDeleteDialogComponent],
})
export class WHMovementModule {}
