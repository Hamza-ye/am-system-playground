import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PeopleGroupDetailComponent } from './people-group-detail.component';

describe('Component Tests', () => {
  describe('PeopleGroup Management Detail Component', () => {
    let comp: PeopleGroupDetailComponent;
    let fixture: ComponentFixture<PeopleGroupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PeopleGroupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ peopleGroup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PeopleGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PeopleGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load peopleGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.peopleGroup).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
