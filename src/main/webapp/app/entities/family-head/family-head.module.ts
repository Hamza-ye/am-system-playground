import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FamilyHeadComponent } from './list/family-head.component';
import { FamilyHeadDetailComponent } from './detail/family-head-detail.component';
import { FamilyHeadUpdateComponent } from './update/family-head-update.component';
import { FamilyHeadDeleteDialogComponent } from './delete/family-head-delete-dialog.component';
import { FamilyHeadRoutingModule } from './route/family-head-routing.module';

@NgModule({
  imports: [SharedModule, FamilyHeadRoutingModule],
  declarations: [FamilyHeadComponent, FamilyHeadDetailComponent, FamilyHeadUpdateComponent, FamilyHeadDeleteDialogComponent],
  entryComponents: [FamilyHeadDeleteDialogComponent],
})
export class FamilyHeadModule {}
