import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LlinsFamilyTargetComponent } from './list/llins-family-target.component';
import { LlinsFamilyTargetDetailComponent } from './detail/llins-family-target-detail.component';
import { LlinsFamilyTargetUpdateComponent } from './update/llins-family-target-update.component';
import { LlinsFamilyTargetDeleteDialogComponent } from './delete/llins-family-target-delete-dialog.component';
import { LlinsFamilyTargetRoutingModule } from './route/llins-family-target-routing.module';

@NgModule({
  imports: [SharedModule, LlinsFamilyTargetRoutingModule],
  declarations: [
    LlinsFamilyTargetComponent,
    LlinsFamilyTargetDetailComponent,
    LlinsFamilyTargetUpdateComponent,
    LlinsFamilyTargetDeleteDialogComponent,
  ],
  entryComponents: [LlinsFamilyTargetDeleteDialogComponent],
})
export class LlinsFamilyTargetModule {}
