import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LLINSVillageReportComponent } from './list/llins-village-report.component';
import { LLINSVillageReportDetailComponent } from './detail/llins-village-report-detail.component';
import { LLINSVillageReportUpdateComponent } from './update/llins-village-report-update.component';
import { LLINSVillageReportDeleteDialogComponent } from './delete/llins-village-report-delete-dialog.component';
import { LLINSVillageReportRoutingModule } from './route/llins-village-report-routing.module';

@NgModule({
  imports: [SharedModule, LLINSVillageReportRoutingModule],
  declarations: [
    LLINSVillageReportComponent,
    LLINSVillageReportDetailComponent,
    LLINSVillageReportUpdateComponent,
    LLINSVillageReportDeleteDialogComponent,
  ],
  entryComponents: [LLINSVillageReportDeleteDialogComponent],
})
export class LLINSVillageReportModule {}
