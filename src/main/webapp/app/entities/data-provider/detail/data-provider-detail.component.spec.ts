import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataProviderDetailComponent } from './data-provider-detail.component';

describe('Component Tests', () => {
  describe('DataProvider Management Detail Component', () => {
    let comp: DataProviderDetailComponent;
    let fixture: ComponentFixture<DataProviderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DataProviderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dataProvider: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DataProviderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DataProviderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dataProvider on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dataProvider).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
