import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CHVDetailComponent } from './chv-detail.component';

describe('Component Tests', () => {
  describe('CHV Management Detail Component', () => {
    let comp: CHVDetailComponent;
    let fixture: ComponentFixture<CHVDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CHVDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cHV: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CHVDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CHVDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cHV on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cHV).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
