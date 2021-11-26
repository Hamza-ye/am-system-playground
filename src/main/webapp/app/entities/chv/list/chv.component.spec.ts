import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ChvService } from '../service/chv.service';

import { ChvComponent } from './chv.component';

describe('Component Tests', () => {
  describe('Chv Management Component', () => {
    let comp: ChvComponent;
    let fixture: ComponentFixture<ChvComponent>;
    let service: ChvService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ChvComponent],
      })
        .overrideTemplate(ChvComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChvComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ChvService);

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
      expect(comp.chvs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
