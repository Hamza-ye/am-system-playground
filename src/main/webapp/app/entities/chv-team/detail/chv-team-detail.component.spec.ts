import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChvTeamDetailComponent } from './chv-team-detail.component';

describe('Component Tests', () => {
  describe('ChvTeam Management Detail Component', () => {
    let comp: ChvTeamDetailComponent;
    let fixture: ComponentFixture<ChvTeamDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ChvTeamDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ chvTeam: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ChvTeamDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChvTeamDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chvTeam on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chvTeam).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
