import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LLINSFamilyReportComponent } from './list/llins-family-report.component';
import { LLINSFamilyReportDetailComponent } from './detail/llins-family-report-detail.component';
import { LLINSFamilyReportUpdateComponent } from './update/llins-family-report-update.component';
import { LLINSFamilyReportDeleteDialogComponent } from './delete/llins-family-report-delete-dialog.component';
import { LLINSFamilyReportRoutingModule } from './route/llins-family-report-routing.module';

@NgModule({
  imports: [SharedModule, LLINSFamilyReportRoutingModule],
  declarations: [
    LLINSFamilyReportComponent,
    LLINSFamilyReportDetailComponent,
    LLINSFamilyReportUpdateComponent,
    LLINSFamilyReportDeleteDialogComponent,
  ],
  entryComponents: [LLINSFamilyReportDeleteDialogComponent],
})
export class LLINSFamilyReportModule {}
