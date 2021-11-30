import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProjectPageComponent } from './list/project-page.component';
import { ProjectPageDetailComponent } from './detail/project-page-detail.component';
import { ProjectPageRoutingModule } from './route/project-page-routing.module';

@NgModule({
  imports: [SharedModule, ProjectPageRoutingModule],
  declarations: [ProjectPageComponent, ProjectPageDetailComponent],
  // entryComponents: [ProjectDeleteDialogComponent],
})
export class ProjectPageModule {}
