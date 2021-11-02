import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WHMovementDetailComponent } from './wh-movement-detail.component';

describe('Component Tests', () => {
  describe('WHMovement Management Detail Component', () => {
    let comp: WHMovementDetailComponent;
    let fixture: ComponentFixture<WHMovementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WHMovementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ wHMovement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WHMovementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WHMovementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load wHMovement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.wHMovement).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
