import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MalariaUnitStaffMemberService } from '../service/malaria-unit-staff-member.service';

import { MalariaUnitStaffMemberComponent } from './malaria-unit-staff-member.component';

describe('Component Tests', () => {
  describe('MalariaUnitStaffMember Management Component', () => {
    let comp: MalariaUnitStaffMemberComponent;
    let fixture: ComponentFixture<MalariaUnitStaffMemberComponent>;
    let service: MalariaUnitStaffMemberService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MalariaUnitStaffMemberComponent],
      })
        .overrideTemplate(MalariaUnitStaffMemberComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MalariaUnitStaffMemberComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MalariaUnitStaffMemberService);

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
      expect(comp.malariaUnitStaffMembers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
