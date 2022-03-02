import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAirport } from '../airport.model';
import { AirportService } from '../service/airport.service';

@Component({
  templateUrl: './airport-delete-dialog.component.html',
})
export class AirportDeleteDialogComponent {
  airport?: IAirport;

  constructor(protected airportService: AirportService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.airportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
