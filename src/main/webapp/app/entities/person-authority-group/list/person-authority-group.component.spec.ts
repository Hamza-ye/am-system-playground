import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PersonAuthorityGroupService } from '../service/person-authority-group.service';

import { PersonAuthorityGroupComponent } from './person-authority-group.component';

describe('Component Tests', () => {
  describe('PersonAuthorityGroup Management Component', () => {
    let comp: PersonAuthorityGroupComponent;
    let fixture: ComponentFixture<PersonAuthorityGroupComponent>;
    let service: PersonAuthorityGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonAuthorityGroupComponent],
      })
        .overrideTemplate(PersonAuthorityGroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonAuthorityGroupComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PersonAuthorityGroupService);

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
      expect(comp.personAuthorityGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
