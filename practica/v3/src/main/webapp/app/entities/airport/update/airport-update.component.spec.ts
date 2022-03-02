import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AirportService } from '../service/airport.service';
import { IAirport, Airport } from '../airport.model';

import { AirportUpdateComponent } from './airport-update.component';

describe('Airport Management Update Component', () => {
  let comp: AirportUpdateComponent;
  let fixture: ComponentFixture<AirportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let airportService: AirportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AirportUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AirportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AirportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    airportService = TestBed.inject(AirportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const airport: IAirport = { id: 456 };

      activatedRoute.data = of({ airport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(airport));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Airport>>();
      const airport = { id: 123 };
      jest.spyOn(airportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ airport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: airport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(airportService.update).toHaveBeenCalledWith(airport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Airport>>();
      const airport = new Airport();
      jest.spyOn(airportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ airport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: airport }));
      saveSubject.complete();

      // THEN
      expect(airportService.create).toHaveBeenCalledWith(airport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Airport>>();
      const airport = { id: 123 };
      jest.spyOn(airportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ airport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(airportService.update).toHaveBeenCalledWith(airport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
