import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CHVService } from '../service/chv.service';

import { CHVComponent } from './chv.component';

describe('Component Tests', () => {
  describe('CHV Management Component', () => {
    let comp: CHVComponent;
    let fixture: ComponentFixture<CHVComponent>;
    let service: CHVService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CHVComponent],
      })
        .overrideTemplate(CHVComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CHVComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CHVService);

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
      expect(comp.cHVS?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
