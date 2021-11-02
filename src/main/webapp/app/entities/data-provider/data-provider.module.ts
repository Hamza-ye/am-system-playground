import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DataProviderComponent } from './list/data-provider.component';
import { DataProviderDetailComponent } from './detail/data-provider-detail.component';
import { DataProviderUpdateComponent } from './update/data-provider-update.component';
import { DataProviderDeleteDialogComponent } from './delete/data-provider-delete-dialog.component';
import { DataProviderRoutingModule } from './route/data-provider-routing.module';

@NgModule({
  imports: [SharedModule, DataProviderRoutingModule],
  declarations: [DataProviderComponent, DataProviderDetailComponent, DataProviderUpdateComponent, DataProviderDeleteDialogComponent],
  entryComponents: [DataProviderDeleteDialogComponent],
})
export class DataProviderModule {}
