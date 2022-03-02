import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPilot } from '../pilot.model';
import { PilotService } from '../service/pilot.service';

@Component({
  templateUrl: './pilot-delete-dialog.component.html',
})
export class PilotDeleteDialogComponent {
  pilot?: IPilot;

  constructor(protected pilotService: PilotService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pilotService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
