import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFlight } from '../flight.model';

@Component({
  selector: 'jhi-flight-detail',
  templateUrl: './flight-detail.component.html',
})
export class FlightDetailComponent implements OnInit {
  flight: IFlight | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flight }) => {
      this.flight = flight;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
