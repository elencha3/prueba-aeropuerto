import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICrew, Crew } from '../crew.model';
import { CrewService } from '../service/crew.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crew-update',
  templateUrl: './crew-update.component.html',
})
export class CrewUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(255)]],
    dni: [null, [Validators.required, Validators.pattern('[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]')]],
    adress: [null, [Validators.required]],
    email: [null, [Validators.required, Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
    picture: [],
    pictureContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected crewService: CrewService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crew }) => {
      this.updateForm(crew);
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
    const crew = this.createFromForm();
    if (crew.id !== undefined) {
      this.subscribeToSaveResponse(this.crewService.update(crew));
    } else {
      this.subscribeToSaveResponse(this.crewService.create(crew));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICrew>>): void {
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

  protected updateForm(crew: ICrew): void {
    this.editForm.patchValue({
      id: crew.id,
      name: crew.name,
      dni: crew.dni,
      adress: crew.adress,
      email: crew.email,
      picture: crew.picture,
      pictureContentType: crew.pictureContentType,
    });
  }

  protected createFromForm(): ICrew {
    return {
      ...new Crew(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      adress: this.editForm.get(['adress'])!.value,
      email: this.editForm.get(['email'])!.value,
      pictureContentType: this.editForm.get(['pictureContentType'])!.value,
      picture: this.editForm.get(['picture'])!.value,
    };
  }
}
