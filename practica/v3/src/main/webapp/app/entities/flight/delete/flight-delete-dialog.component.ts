import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFlight } from '../flight.model';
import { FlightService } from '../service/flight.service';

@Component({
  templateUrl: './flight-delete-dialog.component.html',
})
export class FlightDeleteDialogComponent {
  flight?: IFlight;

  constructor(protected flightService: FlightService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.flightService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
