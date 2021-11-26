import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChvMalariaReportVersion1Component } from './list/chv-malaria-report-version-1.component';
import { ChvMalariaReportVersion1DetailComponent } from './detail/chv-malaria-report-version-1-detail.component';
import { ChvMalariaReportVersion1UpdateComponent } from './update/chv-malaria-report-version-1-update.component';
import { ChvMalariaReportVersion1DeleteDialogComponent } from './delete/chv-malaria-report-version-1-delete-dialog.component';
import { ChvMalariaReportVersion1RoutingModule } from './route/chv-malaria-report-version-1-routing.module';

@NgModule({
  imports: [SharedModule, ChvMalariaReportVersion1RoutingModule],
  declarations: [
    ChvMalariaReportVersion1Component,
    ChvMalariaReportVersion1DetailComponent,
    ChvMalariaReportVersion1UpdateComponent,
    ChvMalariaReportVersion1DeleteDialogComponent,
  ],
  entryComponents: [ChvMalariaReportVersion1DeleteDialogComponent],
})
export class ChvMalariaReportVersion1Module {}
