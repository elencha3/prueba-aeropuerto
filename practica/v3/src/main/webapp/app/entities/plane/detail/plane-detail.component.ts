import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlane } from '../plane.model';

@Component({
  selector: 'jhi-plane-detail',
  templateUrl: './plane-detail.component.html',
})
export class PlaneDetailComponent implements OnInit {
  plane: IPlane | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plane }) => {
      this.plane = plane;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
