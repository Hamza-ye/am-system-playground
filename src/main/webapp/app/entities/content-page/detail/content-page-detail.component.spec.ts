import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContentPageDetailComponent } from './content-page-detail.component';

describe('Component Tests', () => {
  describe('ContentPage Management Detail Component', () => {
    let comp: ContentPageDetailComponent;
    let fixture: ComponentFixture<ContentPageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContentPageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contentPage: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContentPageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContentPageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contentPage on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contentPage).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
