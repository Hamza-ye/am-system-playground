import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrganisationUnitService } from '../service/organisation-unit.service';

import { OrganisationUnitComponent } from './organisation-unit.component';

describe('Component Tests', () => {
  describe('OrganisationUnit Management Component', () => {
    let comp: OrganisationUnitComponent;
    let fixture: ComponentFixture<OrganisationUnitComponent>;
    let service: OrganisationUnitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitComponent],
      })
        .overrideTemplate(OrganisationUnitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrganisationUnitService);

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
      expect(comp.organisationUnits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
