import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlaneDetailComponent } from './plane-detail.component';

describe('Plane Management Detail Component', () => {
  let comp: PlaneDetailComponent;
  let fixture: ComponentFixture<PlaneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlaneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ plane: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlaneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlaneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load plane on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.plane).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
