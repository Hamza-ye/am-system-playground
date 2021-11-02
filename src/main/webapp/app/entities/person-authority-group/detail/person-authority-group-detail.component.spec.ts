import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonAuthorityGroupDetailComponent } from './person-authority-group-detail.component';

describe('Component Tests', () => {
  describe('PersonAuthorityGroup Management Detail Component', () => {
    let comp: PersonAuthorityGroupDetailComponent;
    let fixture: ComponentFixture<PersonAuthorityGroupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PersonAuthorityGroupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ personAuthorityGroup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PersonAuthorityGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PersonAuthorityGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load personAuthorityGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.personAuthorityGroup).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
