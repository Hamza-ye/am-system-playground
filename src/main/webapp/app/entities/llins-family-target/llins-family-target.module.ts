import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LLINSFamilyTargetComponent } from './list/llins-family-target.component';
import { LLINSFamilyTargetDetailComponent } from './detail/llins-family-target-detail.component';
import { LLINSFamilyTargetUpdateComponent } from './update/llins-family-target-update.component';
import { LLINSFamilyTargetDeleteDialogComponent } from './delete/llins-family-target-delete-dialog.component';
import { LLINSFamilyTargetRoutingModule } from './route/llins-family-target-routing.module';

@NgModule({
  imports: [SharedModule, LLINSFamilyTargetRoutingModule],
  declarations: [
    LLINSFamilyTargetComponent,
    LLINSFamilyTargetDetailComponent,
    LLINSFamilyTargetUpdateComponent,
    LLINSFamilyTargetDeleteDialogComponent,
  ],
  entryComponents: [LLINSFamilyTargetDeleteDialogComponent],
})
export class LLINSFamilyTargetModule {}
