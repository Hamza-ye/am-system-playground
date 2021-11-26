import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LlinsVillageTargetComponent } from './list/llins-village-target.component';
import { LlinsVillageTargetDetailComponent } from './detail/llins-village-target-detail.component';
import { LlinsVillageTargetUpdateComponent } from './update/llins-village-target-update.component';
import { LlinsVillageTargetDeleteDialogComponent } from './delete/llins-village-target-delete-dialog.component';
import { LlinsVillageTargetRoutingModule } from './route/llins-village-target-routing.module';

@NgModule({
  imports: [SharedModule, LlinsVillageTargetRoutingModule],
  declarations: [
    LlinsVillageTargetComponent,
    LlinsVillageTargetDetailComponent,
    LlinsVillageTargetUpdateComponent,
    LlinsVillageTargetDeleteDialogComponent,
  ],
  entryComponents: [LlinsVillageTargetDeleteDialogComponent],
})
export class LlinsVillageTargetModule {}
