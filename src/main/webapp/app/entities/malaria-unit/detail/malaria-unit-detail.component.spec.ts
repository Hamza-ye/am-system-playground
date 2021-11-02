import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MalariaUnitDetailComponent } from './malaria-unit-detail.component';

describe('Component Tests', () => {
  describe('MalariaUnit Management Detail Component', () => {
    let comp: MalariaUnitDetailComponent;
    let fixture: ComponentFixture<MalariaUnitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MalariaUnitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ malariaUnit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MalariaUnitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MalariaUnitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load malariaUnit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.malariaUnit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
