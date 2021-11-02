import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MalariaUnitComponent } from './list/malaria-unit.component';
import { MalariaUnitDetailComponent } from './detail/malaria-unit-detail.component';
import { MalariaUnitUpdateComponent } from './update/malaria-unit-update.component';
import { MalariaUnitDeleteDialogComponent } from './delete/malaria-unit-delete-dialog.component';
import { MalariaUnitRoutingModule } from './route/malaria-unit-routing.module';

@NgModule({
  imports: [SharedModule, MalariaUnitRoutingModule],
  declarations: [MalariaUnitComponent, MalariaUnitDetailComponent, MalariaUnitUpdateComponent, MalariaUnitDeleteDialogComponent],
  entryComponents: [MalariaUnitDeleteDialogComponent],
})
export class MalariaUnitModule {}
