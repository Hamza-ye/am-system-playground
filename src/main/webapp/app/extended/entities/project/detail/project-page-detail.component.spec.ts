import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProjectPageDetailComponent } from './project-page-detail.component';

describe('Component Tests', () => {
  describe('Project Management Detail Component', () => {
    let comp: ProjectPageDetailComponent;
    let fixture: ComponentFixture<ProjectPageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProjectPageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ project: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProjectPageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProjectPageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load project on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.project).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
