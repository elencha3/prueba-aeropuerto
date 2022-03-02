import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FlightService } from '../service/flight.service';
import { IFlight, Flight } from '../flight.model';
import { IPilot } from 'app/entities/pilot/pilot.model';
import { PilotService } from 'app/entities/pilot/service/pilot.service';
import { IPlane } from 'app/entities/plane/plane.model';
import { PlaneService } from 'app/entities/plane/service/plane.service';
import { ICrew } from 'app/entities/crew/crew.model';
import { CrewService } from 'app/entities/crew/service/crew.service';
import { IAirport } from 'app/entities/airport/airport.model';
import { AirportService } from 'app/entities/airport/service/airport.service';

import { FlightUpdateComponent } from './flight-update.component';

describe('Flight Management Update Component', () => {
  let comp: FlightUpdateComponent;
  let fixture: ComponentFixture<FlightUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let flightService: FlightService;
  let pilotService: PilotService;
  let planeService: PlaneService;
  let crewService: CrewService;
  let airportService: AirportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FlightUpdateComponent],
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
      .overrideTemplate(FlightUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FlightUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    flightService = TestBed.inject(FlightService);
    pilotService = TestBed.inject(PilotService);
    planeService = TestBed.inject(PlaneService);
    crewService = TestBed.inject(CrewService);
    airportService = TestBed.inject(AirportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pilot query and add missing value', () => {
      const flight: IFlight = { id: 456 };
      const pilot: IPilot = { id: 93276 };
      flight.pilot = pilot;

      const pilotCollection: IPilot[] = [{ id: 96352 }];
      jest.spyOn(pilotService, 'query').mockReturnValue(of(new HttpResponse({ body: pilotCollection })));
      const additionalPilots = [pilot];
      const expectedCollection: IPilot[] = [...additionalPilots, ...pilotCollection];
      jest.spyOn(pilotService, 'addPilotToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      expect(pilotService.query).toHaveBeenCalled();
      expect(pilotService.addPilotToCollectionIfMissing).toHaveBeenCalledWith(pilotCollection, ...additionalPilots);
      expect(comp.pilotsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Plane query and add missing value', () => {
      const flight: IFlight = { id: 456 };
      const plane: IPlane = { id: 1352 };
      flight.plane = plane;

      const planeCollection: IPlane[] = [{ id: 45930 }];
      jest.spyOn(planeService, 'query').mockReturnValue(of(new HttpResponse({ body: planeCollection })));
      const additionalPlanes = [plane];
      const expectedCollection: IPlane[] = [...additionalPlanes, ...planeCollection];
      jest.spyOn(planeService, 'addPlaneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      expect(planeService.query).toHaveBeenCalled();
      expect(planeService.addPlaneToCollectionIfMissing).toHaveBeenCalledWith(planeCollection, ...additionalPlanes);
      expect(comp.planesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Crew query and add missing value', () => {
      const flight: IFlight = { id: 456 };
      const crews: ICrew[] = [{ id: 61884 }];
      flight.crews = crews;

      const crewCollection: ICrew[] = [{ id: 81088 }];
      jest.spyOn(crewService, 'query').mockReturnValue(of(new HttpResponse({ body: crewCollection })));
      const additionalCrews = [...crews];
      const expectedCollection: ICrew[] = [...additionalCrews, ...crewCollection];
      jest.spyOn(crewService, 'addCrewToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      expect(crewService.query).toHaveBeenCalled();
      expect(crewService.addCrewToCollectionIfMissing).toHaveBeenCalledWith(crewCollection, ...additionalCrews);
      expect(comp.crewsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Airport query and add missing value', () => {
      const flight: IFlight = { id: 456 };
      const origin: IAirport = { id: 45857 };
      flight.origin = origin;
      const destination: IAirport = { id: 4360 };
      flight.destination = destination;

      const airportCollection: IAirport[] = [{ id: 3848 }];
      jest.spyOn(airportService, 'query').mockReturnValue(of(new HttpResponse({ body: airportCollection })));
      const additionalAirports = [origin, destination];
      const expectedCollection: IAirport[] = [...additionalAirports, ...airportCollection];
      jest.spyOn(airportService, 'addAirportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      expect(airportService.query).toHaveBeenCalled();
      expect(airportService.addAirportToCollectionIfMissing).toHaveBeenCalledWith(airportCollection, ...additionalAirports);
      expect(comp.airportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const flight: IFlight = { id: 456 };
      const pilot: IPilot = { id: 46976 };
      flight.pilot = pilot;
      const plane: IPlane = { id: 43066 };
      flight.plane = plane;
      const crews: ICrew = { id: 31888 };
      flight.crews = [crews];
      const origin: IAirport = { id: 75198 };
      flight.origin = origin;
      const destination: IAirport = { id: 74492 };
      flight.destination = destination;

      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(flight));
      expect(comp.pilotsSharedCollection).toContain(pilot);
      expect(comp.planesSharedCollection).toContain(plane);
      expect(comp.crewsSharedCollection).toContain(crews);
      expect(comp.airportsSharedCollection).toContain(origin);
      expect(comp.airportsSharedCollection).toContain(destination);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Flight>>();
      const flight = { id: 123 };
      jest.spyOn(flightService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flight }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(flightService.update).toHaveBeenCalledWith(flight);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Flight>>();
      const flight = new Flight();
      jest.spyOn(flightService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: flight }));
      saveSubject.complete();

      // THEN
      expect(flightService.create).toHaveBeenCalledWith(flight);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Flight>>();
      const flight = { id: 123 };
      jest.spyOn(flightService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ flight });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(flightService.update).toHaveBeenCalledWith(flight);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPilotById', () => {
      it('Should return tracked Pilot primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPilotById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlaneById', () => {
      it('Should return tracked Plane primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCrewById', () => {
      it('Should return tracked Crew primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCrewById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAirportById', () => {
      it('Should return tracked Airport primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAirportById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCrew', () => {
      it('Should return option if no Crew is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCrew(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Crew for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCrew(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Crew is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCrew(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
