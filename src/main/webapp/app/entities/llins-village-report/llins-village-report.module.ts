import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LlinsVillageReportComponent } from './list/llins-village-report.component';
import { LlinsVillageReportDetailComponent } from './detail/llins-village-report-detail.component';
import { LlinsVillageReportUpdateComponent } from './update/llins-village-report-update.component';
import { LlinsVillageReportDeleteDialogComponent } from './delete/llins-village-report-delete-dialog.component';
import { LlinsVillageReportRoutingModule } from './route/llins-village-report-routing.module';

@NgModule({
  imports: [SharedModule, LlinsVillageReportRoutingModule],
  declarations: [
    LlinsVillageReportComponent,
    LlinsVillageReportDetailComponent,
    LlinsVillageReportUpdateComponent,
    LlinsVillageReportDeleteDialogComponent,
  ],
  entryComponents: [LlinsVillageReportDeleteDialogComponent],
})
export class LlinsVillageReportModule {}
