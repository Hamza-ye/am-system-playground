import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkingDayDetailComponent } from './working-day-detail.component';

describe('Component Tests', () => {
  describe('WorkingDay Management Detail Component', () => {
    let comp: WorkingDayDetailComponent;
    let fixture: ComponentFixture<WorkingDayDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WorkingDayDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ workingDay: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WorkingDayDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkingDayDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load workingDay on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workingDay).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
