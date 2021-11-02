import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrganisationUnitGroupSetService } from '../service/organisation-unit-group-set.service';

import { OrganisationUnitGroupSetComponent } from './organisation-unit-group-set.component';

describe('Component Tests', () => {
  describe('OrganisationUnitGroupSet Management Component', () => {
    let comp: OrganisationUnitGroupSetComponent;
    let fixture: ComponentFixture<OrganisationUnitGroupSetComponent>;
    let service: OrganisationUnitGroupSetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrganisationUnitGroupSetComponent],
      })
        .overrideTemplate(OrganisationUnitGroupSetComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrganisationUnitGroupSetComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrganisationUnitGroupSetService);

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
      expect(comp.organisationUnitGroupSets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
