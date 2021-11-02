import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DataSetComponent } from './list/data-set.component';
import { DataSetDetailComponent } from './detail/data-set-detail.component';
import { DataSetUpdateComponent } from './update/data-set-update.component';
import { DataSetDeleteDialogComponent } from './delete/data-set-delete-dialog.component';
import { DataSetRoutingModule } from './route/data-set-routing.module';

@NgModule({
  imports: [SharedModule, DataSetRoutingModule],
  declarations: [DataSetComponent, DataSetDetailComponent, DataSetUpdateComponent, DataSetDeleteDialogComponent],
  entryComponents: [DataSetDeleteDialogComponent],
})
export class DataSetModule {}
