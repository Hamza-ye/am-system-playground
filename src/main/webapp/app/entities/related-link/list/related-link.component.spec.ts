import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RelatedLinkService } from '../service/related-link.service';

import { RelatedLinkComponent } from './related-link.component';

describe('Component Tests', () => {
  describe('RelatedLink Management Component', () => {
    let comp: RelatedLinkComponent;
    let fixture: ComponentFixture<RelatedLinkComponent>;
    let service: RelatedLinkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RelatedLinkComponent],
      })
        .overrideTemplate(RelatedLinkComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RelatedLinkComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RelatedLinkService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.relatedLinks?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
