import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAirport } from '../airport.model';

@Component({
  selector: 'jhi-airport-detail',
  templateUrl: './airport-detail.component.html',
})
export class AirportDetailComponent implements OnInit {
  airport: IAirport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ airport }) => {
      this.airport = airport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
