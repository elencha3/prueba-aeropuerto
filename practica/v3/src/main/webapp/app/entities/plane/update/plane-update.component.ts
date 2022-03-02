import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlane, Plane } from '../plane.model';
import { PlaneService } from '../service/plane.service';

@Component({
  selector: 'jhi-plane-update',
  templateUrl: './plane-update.component.html',
})
export class PlaneUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    plate: [null, [Validators.required, Validators.pattern('^.\\\\-.$')]],
    type: [null, [Validators.minLength(10), Validators.maxLength(255)]],
    age: [null, [Validators.min(0)]],
    serialNumber: [null, [Validators.minLength(10), Validators.maxLength(255)]],
  });

  constructor(protected planeService: PlaneService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plane }) => {
      this.updateForm(plane);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plane = this.createFromForm();
    if (plane.id !== undefined) {
      this.subscribeToSaveResponse(this.planeService.update(plane));
    } else {
      this.subscribeToSaveResponse(this.planeService.create(plane));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlane>>): void {
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

  protected updateForm(plane: IPlane): void {
    this.editForm.patchValue({
      id: plane.id,
      plate: plane.plate,
      type: plane.type,
      age: plane.age,
      serialNumber: plane.serialNumber,
    });
  }

  protected createFromForm(): IPlane {
    return {
      ...new Plane(),
      id: this.editForm.get(['id'])!.value,
      plate: this.editForm.get(['plate'])!.value,
      type: this.editForm.get(['type'])!.value,
      age: this.editForm.get(['age'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
    };
  }
}
