import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAirport, Airport } from '../airport.model';
import { AirportService } from '../service/airport.service';

@Component({
  selector: 'jhi-airport-update',
  templateUrl: './airport-update.component.html',
})
export class AirportUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected airportService: AirportService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ airport }) => {
      this.updateForm(airport);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const airport = this.createFromForm();
    if (airport.id !== undefined) {
      this.subscribeToSaveResponse(this.airportService.update(airport));
    } else {
      this.subscribeToSaveResponse(this.airportService.create(airport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAirport>>): void {
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

  protected updateForm(airport: IAirport): void {
    this.editForm.patchValue({
      id: airport.id,
      name: airport.name,
    });
  }

  protected createFromForm(): IAirport {
    return {
      ...new Airport(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
