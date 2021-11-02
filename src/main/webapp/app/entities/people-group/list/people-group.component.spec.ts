import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PeopleGroupService } from '../service/people-group.service';

import { PeopleGroupComponent } from './people-group.component';

describe('Component Tests', () => {
  describe('PeopleGroup Management Component', () => {
    let comp: PeopleGroupComponent;
    let fixture: ComponentFixture<PeopleGroupComponent>;
    let service: PeopleGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PeopleGroupComponent],
      })
        .overrideTemplate(PeopleGroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PeopleGroupComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PeopleGroupService);

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
      expect(comp.peopleGroups?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
