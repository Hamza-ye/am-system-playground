import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilyHeadDetailComponent } from './family-head-detail.component';

describe('Component Tests', () => {
  describe('FamilyHead Management Detail Component', () => {
    let comp: FamilyHeadDetailComponent;
    let fixture: ComponentFixture<FamilyHeadDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FamilyHeadDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ familyHead: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FamilyHeadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FamilyHeadDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load familyHead on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.familyHead).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
