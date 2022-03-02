import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICrew } from '../crew.model';
import { CrewService } from '../service/crew.service';

@Component({
  templateUrl: './crew-delete-dialog.component.html',
})
export class CrewDeleteDialogComponent {
  crew?: ICrew;

  constructor(protected crewService: CrewService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.crewService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
