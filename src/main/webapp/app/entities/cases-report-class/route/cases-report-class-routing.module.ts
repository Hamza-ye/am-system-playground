import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CasesReportClassComponent } from '../list/cases-report-class.component';
import { CasesReportClassDetailComponent } from '../detail/cases-report-class-detail.component';
import { CasesReportClassUpdateComponent } from '../update/cases-report-class-update.component';
import { CasesReportClassRoutingResolveService } from './cases-report-class-routing-resolve.service';

const casesReportClassRoute: Routes = [
  {
    path: '',
    component: CasesReportClassComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CasesReportClassDetailComponent,
    resolve: {
      casesReportClass: CasesReportClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CasesReportClassUpdateComponent,
    resolve: {
      casesReportClass: CasesReportClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CasesReportClassUpdateComponent,
    resolve: {
      casesReportClass: CasesReportClassRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(casesReportClassRoute)],
  exports: [RouterModule],
})
export class CasesReportClassRoutingModule {}
