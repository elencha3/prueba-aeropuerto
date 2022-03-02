import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlane } from '../plane.model';
import { PlaneService } from '../service/plane.service';

@Component({
  templateUrl: './plane-delete-dialog.component.html',
})
export class PlaneDeleteDialogComponent {
  plane?: IPlane;

  constructor(protected planeService: PlaneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
