import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DengueCasesReportComponent } from './list/dengue-cases-report.component';
import { DengueCasesReportDetailComponent } from './detail/dengue-cases-report-detail.component';
import { DengueCasesReportUpdateComponent } from './update/dengue-cases-report-update.component';
import { DengueCasesReportDeleteDialogComponent } from './delete/dengue-cases-report-delete-dialog.component';
import { DengueCasesReportRoutingModule } from './route/dengue-cases-report-routing.module';

@NgModule({
  imports: [SharedModule, DengueCasesReportRoutingModule],
  declarations: [
    DengueCasesReportComponent,
    DengueCasesReportDetailComponent,
    DengueCasesReportUpdateComponent,
    DengueCasesReportDeleteDialogComponent,
  ],
  entryComponents: [DengueCasesReportDeleteDialogComponent],
})
export class DengueCasesReportModule {}
