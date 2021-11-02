import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LLINSVillageTargetComponent } from './list/llins-village-target.component';
import { LLINSVillageTargetDetailComponent } from './detail/llins-village-target-detail.component';
import { LLINSVillageTargetUpdateComponent } from './update/llins-village-target-update.component';
import { LLINSVillageTargetDeleteDialogComponent } from './delete/llins-village-target-delete-dialog.component';
import { LLINSVillageTargetRoutingModule } from './route/llins-village-target-routing.module';

@NgModule({
  imports: [SharedModule, LLINSVillageTargetRoutingModule],
  declarations: [
    LLINSVillageTargetComponent,
    LLINSVillageTargetDetailComponent,
    LLINSVillageTargetUpdateComponent,
    LLINSVillageTargetDeleteDialogComponent,
  ],
  entryComponents: [LLINSVillageTargetDeleteDialogComponent],
})
export class LLINSVillageTargetModule {}
