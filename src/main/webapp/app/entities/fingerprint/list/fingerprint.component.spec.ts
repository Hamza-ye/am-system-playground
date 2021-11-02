import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FingerprintService } from '../service/fingerprint.service';

import { FingerprintComponent } from './fingerprint.component';

describe('Component Tests', () => {
  describe('Fingerprint Management Component', () => {
    let comp: FingerprintComponent;
    let fixture: ComponentFixture<FingerprintComponent>;
    let service: FingerprintService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FingerprintComponent],
      })
        .overrideTemplate(FingerprintComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FingerprintComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FingerprintService);

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
      expect(comp.fingerprints?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
