import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StatusOfCoverageDetailComponent } from './status-of-coverage-detail.component';

describe('Component Tests', () => {
  describe('StatusOfCoverage Management Detail Component', () => {
    let comp: StatusOfCoverageDetailComponent;
    let fixture: ComponentFixture<StatusOfCoverageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StatusOfCoverageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ statusOfCoverage: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StatusOfCoverageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StatusOfCoverageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load statusOfCoverage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.statusOfCoverage).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
