import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ContentPageService } from '../service/content-page.service';

import { ContentPageComponent } from './content-page.component';

describe('Component Tests', () => {
  describe('ContentPage Management Component', () => {
    let comp: ContentPageComponent;
    let fixture: ComponentFixture<ContentPageComponent>;
    let service: ContentPageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContentPageComponent],
      })
        .overrideTemplate(ContentPageComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContentPageComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ContentPageService);

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
      expect(comp.contentPages?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
