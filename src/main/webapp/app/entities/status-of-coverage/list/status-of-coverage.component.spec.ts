import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StatusOfCoverageService } from '../service/status-of-coverage.service';

import { StatusOfCoverageComponent } from './status-of-coverage.component';

describe('Component Tests', () => {
  describe('StatusOfCoverage Management Component', () => {
    let comp: StatusOfCoverageComponent;
    let fixture: ComponentFixture<StatusOfCoverageComponent>;
    let service: StatusOfCoverageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StatusOfCoverageComponent],
      })
        .overrideTemplate(StatusOfCoverageComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StatusOfCoverageComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StatusOfCoverageService);

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
      expect(comp.statusOfCoverages?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
