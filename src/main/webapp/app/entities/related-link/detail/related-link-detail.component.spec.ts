import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RelatedLinkDetailComponent } from './related-link-detail.component';

describe('Component Tests', () => {
  describe('RelatedLink Management Detail Component', () => {
    let comp: RelatedLinkDetailComponent;
    let fixture: ComponentFixture<RelatedLinkDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RelatedLinkDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ relatedLink: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RelatedLinkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RelatedLinkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load relatedLink on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.relatedLink).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
