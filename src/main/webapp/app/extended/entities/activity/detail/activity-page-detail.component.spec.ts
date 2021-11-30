import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ActivityPageDetailComponent } from './activity-page-detail.component';

describe('Component Tests', () => {
  describe('Activity Management Detail Component', () => {
    let comp: ActivityPageDetailComponent;
    let fixture: ComponentFixture<ActivityPageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ActivityPageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ activity: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ActivityPageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ActivityPageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load activity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.activity).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
