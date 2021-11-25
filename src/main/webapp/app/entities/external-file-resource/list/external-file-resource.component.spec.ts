import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ExternalFileResourceService } from '../service/external-file-resource.service';

import { ExternalFileResourceComponent } from './external-file-resource.component';

describe('Component Tests', () => {
  describe('ExternalFileResource Management Component', () => {
    let comp: ExternalFileResourceComponent;
    let fixture: ComponentFixture<ExternalFileResourceComponent>;
    let service: ExternalFileResourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExternalFileResourceComponent],
      })
        .overrideTemplate(ExternalFileResourceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExternalFileResourceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExternalFileResourceService);

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
      expect(comp.externalFileResources?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
