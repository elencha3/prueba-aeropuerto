import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FlightDetailComponent } from './flight-detail.component';

describe('Flight Management Detail Component', () => {
  let comp: FlightDetailComponent;
  let fixture: ComponentFixture<FlightDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FlightDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ flight: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FlightDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FlightDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load flight on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.flight).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
