import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CHVTeamDetailComponent } from './chv-team-detail.component';

describe('Component Tests', () => {
  describe('CHVTeam Management Detail Component', () => {
    let comp: CHVTeamDetailComponent;
    let fixture: ComponentFixture<CHVTeamDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CHVTeamDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cHVTeam: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CHVTeamDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CHVTeamDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cHVTeam on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cHVTeam).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
