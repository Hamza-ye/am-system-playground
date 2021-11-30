import { NgModule } from '@angular/core';
import { ActivityPageComponent } from './list/activity-page.component';
import { ActivityPageDetailComponent } from './detail/activity-page-detail.component';
import { ActivityPageRoutingModule } from './route/activity-page-routing.module';
import {SharedModule} from "../../../shared/shared.module";

@NgModule({
  imports: [SharedModule, ActivityPageRoutingModule],
  declarations: [ActivityPageComponent, ActivityPageDetailComponent],
  // entryComponents: [ActivityDeleteDialogComponent],
})
export class ActivityPageModule {}
