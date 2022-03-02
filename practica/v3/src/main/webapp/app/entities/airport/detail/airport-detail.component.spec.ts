import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AirportDetailComponent } from './airport-detail.component';

describe('Airport Management Detail Component', () => {
  let comp: AirportDetailComponent;
  let fixture: ComponentFixture<AirportDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AirportDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ airport: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AirportDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AirportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load airport on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.airport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
