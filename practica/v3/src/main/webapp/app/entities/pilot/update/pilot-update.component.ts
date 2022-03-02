import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPilot, Pilot } from '../pilot.model';
import { PilotService } from '../service/pilot.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-pilot-update',
  templateUrl: './pilot-update.component.html',
})
export class PilotUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(255)]],
    dni: [null, [Validators.required, Validators.pattern('[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]')]],
    adress: [null, [Validators.required]],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    horasVuelo: [null, [Validators.required]],
    picture: [],
    pictureContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected pilotService: PilotService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pilot }) => {
      this.updateForm(pilot);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) => this.eventManager.broadcast(new EventWithContent<AlertError>('v3App.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pilot = this.createFromForm();
    if (pilot.id !== undefined) {
      this.subscribeToSaveResponse(this.pilotService.update(pilot));
    } else {
      this.subscribeToSaveResponse(this.pilotService.create(pilot));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPilot>>): void {
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

  protected updateForm(pilot: IPilot): void {
    this.editForm.patchValue({
      id: pilot.id,
      name: pilot.name,
      dni: pilot.dni,
      adress: pilot.adress,
      email: pilot.email,
      horasVuelo: pilot.horasVuelo,
      picture: pilot.picture,
      pictureContentType: pilot.pictureContentType,
    });
  }

  protected createFromForm(): IPilot {
    return {
      ...new Pilot(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      adress: this.editForm.get(['adress'])!.value,
      email: this.editForm.get(['email'])!.value,
      horasVuelo: this.editForm.get(['horasVuelo'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
    };
  }
}
