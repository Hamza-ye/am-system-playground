import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MalariaUnitStaffMemberComponent } from './list/malaria-unit-staff-member.component';
import { MalariaUnitStaffMemberDetailComponent } from './detail/malaria-unit-staff-member-detail.component';
import { MalariaUnitStaffMemberUpdateComponent } from './update/malaria-unit-staff-member-update.component';
import { MalariaUnitStaffMemberDeleteDialogComponent } from './delete/malaria-unit-staff-member-delete-dialog.component';
import { MalariaUnitStaffMemberRoutingModule } from './route/malaria-unit-staff-member-routing.module';

@NgModule({
  imports: [SharedModule, MalariaUnitStaffMemberRoutingModule],
  declarations: [
    MalariaUnitStaffMemberComponent,
    MalariaUnitStaffMemberDetailComponent,
    MalariaUnitStaffMemberUpdateComponent,
    MalariaUnitStaffMemberDeleteDialogComponent,
  ],
  entryComponents: [MalariaUnitStaffMemberDeleteDialogComponent],
})
export class MalariaUnitStaffMemberModule {}
