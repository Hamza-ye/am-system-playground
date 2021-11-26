import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WhMovementDetailComponent } from './wh-movement-detail.component';

describe('Component Tests', () => {
  describe('WhMovement Management Detail Component', () => {
    let comp: WhMovementDetailComponent;
    let fixture: ComponentFixture<WhMovementDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WhMovementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ whMovement: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WhMovementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WhMovementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load whMovement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.whMovement).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
