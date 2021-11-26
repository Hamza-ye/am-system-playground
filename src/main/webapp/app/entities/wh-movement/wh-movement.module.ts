import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WhMovementComponent } from './list/wh-movement.component';
import { WhMovementDetailComponent } from './detail/wh-movement-detail.component';
import { WhMovementUpdateComponent } from './update/wh-movement-update.component';
import { WhMovementDeleteDialogComponent } from './delete/wh-movement-delete-dialog.component';
import { WhMovementRoutingModule } from './route/wh-movement-routing.module';

@NgModule({
  imports: [SharedModule, WhMovementRoutingModule],
  declarations: [WhMovementComponent, WhMovementDetailComponent, WhMovementUpdateComponent, WhMovementDeleteDialogComponent],
  entryComponents: [WhMovementDeleteDialogComponent],
})
export class WhMovementModule {}
