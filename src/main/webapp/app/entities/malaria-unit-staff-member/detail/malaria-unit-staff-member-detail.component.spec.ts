import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MalariaUnitStaffMemberDetailComponent } from './malaria-unit-staff-member-detail.component';

describe('Component Tests', () => {
  describe('MalariaUnitStaffMember Management Detail Component', () => {
    let comp: MalariaUnitStaffMemberDetailComponent;
    let fixture: ComponentFixture<MalariaUnitStaffMemberDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MalariaUnitStaffMemberDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ malariaUnitStaffMember: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MalariaUnitStaffMemberDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MalariaUnitStaffMemberDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load malariaUnitStaffMember on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.malariaUnitStaffMember).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
