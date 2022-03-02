import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPilot } from '../pilot.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-pilot-detail',
  templateUrl: './pilot-detail.component.html',
})
export class PilotDetailComponent implements OnInit {
  pilot: IPilot | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pilot }) => {
      this.pilot = pilot;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
