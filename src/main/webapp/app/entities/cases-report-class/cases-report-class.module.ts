import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CasesReportClassComponent } from './list/cases-report-class.component';
import { CasesReportClassDetailComponent } from './detail/cases-report-class-detail.component';
import { CasesReportClassUpdateComponent } from './update/cases-report-class-update.component';
import { CasesReportClassDeleteDialogComponent } from './delete/cases-report-class-delete-dialog.component';
import { CasesReportClassRoutingModule } from './route/cases-report-class-routing.module';

@NgModule({
  imports: [SharedModule, CasesReportClassRoutingModule],
  declarations: [
    CasesReportClassComponent,
    CasesReportClassDetailComponent,
    CasesReportClassUpdateComponent,
    CasesReportClassDeleteDialogComponent,
  ],
  entryComponents: [CasesReportClassDeleteDialogComponent],
})
export class CasesReportClassModule {}
