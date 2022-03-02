import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFlight, Flight } from '../flight.model';
import { FlightService } from '../service/flight.service';
import { IPilot } from 'app/entities/pilot/pilot.model';
import { PilotService } from 'app/entities/pilot/service/pilot.service';
import { IPlane } from 'app/entities/plane/plane.model';
import { PlaneService } from 'app/entities/plane/service/plane.service';
import { ICrew } from 'app/entities/crew/crew.model';
import { CrewService } from 'app/entities/crew/service/crew.service';
import { IAirport } from 'app/entities/airport/airport.model';
import { AirportService } from 'app/entities/airport/service/airport.service';

@Component({
  selector: 'jhi-flight-update',
  templateUrl: './flight-update.component.html',
})
export class FlightUpdateComponent implements OnInit {
  isSaving = false;

  pilotsSharedCollection: IPilot[] = [];
  planesSharedCollection: IPlane[] = [];
  crewsSharedCollection: ICrew[] = [];
  airportsSharedCollection: IAirport[] = [];

  editForm = this.fb.group({
    id: [],
    numFlight: [null, [Validators.minLength(0), Validators.maxLength(10)]],
    pilot: [],
    plane: [],
    crews: [],
    origin: [],
    destination: [],
  });

  constructor(
    protected flightService: FlightService,
    protected pilotService: PilotService,
    protected planeService: PlaneService,
    protected crewService: CrewService,
    protected airportService: AirportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flight }) => {
      this.updateForm(flight);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flight = this.createFromForm();
    if (flight.id !== undefined) {
      this.subscribeToSaveResponse(this.flightService.update(flight));
    } else {
      this.subscribeToSaveResponse(this.flightService.create(flight));
    }
  }

  trackPilotById(index: number, item: IPilot): number {
    return item.id!;
  }

  trackPlaneById(index: number, item: IPlane): number {
    return item.id!;
  }

  trackCrewById(index: number, item: ICrew): number {
    return item.id!;
  }

  trackAirportById(index: number, item: IAirport): number {
    return item.id!;
  }

  getSelectedCrew(option: ICrew, selectedVals?: ICrew[]): ICrew {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlight>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(flight: IFlight): void {
    this.editForm.patchValue({
      id: flight.id,
      numFlight: flight.numFlight,
      pilot: flight.pilot,
      plane: flight.plane,
      crews: flight.crews,
      origin: flight.origin,
      destination: flight.destination,
    });

    this.pilotsSharedCollection = this.pilotService.addPilotToCollectionIfMissing(this.pilotsSharedCollection, flight.pilot);
    this.planesSharedCollection = this.planeService.addPlaneToCollectionIfMissing(this.planesSharedCollection, flight.plane);
    this.crewsSharedCollection = this.crewService.addCrewToCollectionIfMissing(this.crewsSharedCollection, ...(flight.crews ?? []));
    this.airportsSharedCollection = this.airportService.addAirportToCollectionIfMissing(
      this.airportsSharedCollection,
      flight.origin,
      flight.destination
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pilotService
      .query()
      .pipe(map((res: HttpResponse<IPilot[]>) => res.body ?? []))
      .pipe(map((pilots: IPilot[]) => this.pilotService.addPilotToCollectionIfMissing(pilots, this.editForm.get('pilot')!.value)))
      .subscribe((pilots: IPilot[]) => (this.pilotsSharedCollection = pilots));

    this.planeService
      .query()
      .pipe(map((res: HttpResponse<IPlane[]>) => res.body ?? []))
      .pipe(map((planes: IPlane[]) => this.planeService.addPlaneToCollectionIfMissing(planes, this.editForm.get('plane')!.value)))
      .subscribe((planes: IPlane[]) => (this.planesSharedCollection = planes));

    this.crewService
      .query()
      .pipe(map((res: HttpResponse<ICrew[]>) => res.body ?? []))
      .pipe(map((crews: ICrew[]) => this.crewService.addCrewToCollectionIfMissing(crews, ...(this.editForm.get('crews')!.value ?? []))))
      .subscribe((crews: ICrew[]) => (this.crewsSharedCollection = crews));

    this.airportService
      .query()
      .pipe(map((res: HttpResponse<IAirport[]>) => res.body ?? []))
      .pipe(
        map((airports: IAirport[]) =>
          this.airportService.addAirportToCollectionIfMissing(
            airports,
            this.editForm.get('origin')!.value,
            this.editForm.get('destination')!.value
          )
        )
      )
      .subscribe((airports: IAirport[]) => (this.airportsSharedCollection = airports));
  }

  protected createFromForm(): IFlight {
    return {
      ...new Flight(),
      id: this.editForm.get(['id'])!.value,
      numFlight: this.editForm.get(['numFlight'])!.value,
      pilot: this.editForm.get(['pilot'])!.value,
      plane: this.editForm.get(['plane'])!.value,
      crews: this.editForm.get(['crews'])!.value,
      origin: this.editForm.get(['origin'])!.value,
      destination: this.editForm.get(['destination'])!.value,
    };
  }
}
