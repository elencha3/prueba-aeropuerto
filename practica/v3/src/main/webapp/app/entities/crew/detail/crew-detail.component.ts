import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICrew } from '../crew.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-crew-detail',
  templateUrl: './crew-detail.component.html',
})
export class CrewDetailComponent implements OnInit {
  crew: ICrew | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ crew }) => {
      this.crew = crew;
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
