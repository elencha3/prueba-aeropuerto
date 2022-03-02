import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PilotService } from '../service/pilot.service';
import { IPilot, Pilot } from '../pilot.model';

import { PilotUpdateComponent } from './pilot-update.component';

describe('Pilot Management Update Component', () => {
  let comp: PilotUpdateComponent;
  let fixture: ComponentFixture<PilotUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pilotService: PilotService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PilotUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PilotUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PilotUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pilotService = TestBed.inject(PilotService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pilot: IPilot = { id: 456 };

      activatedRoute.data = of({ pilot });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pilot));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pilot>>();
      const pilot = { id: 123 };
      jest.spyOn(pilotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pilot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pilot }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pilotService.update).toHaveBeenCalledWith(pilot);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pilot>>();
      const pilot = new Pilot();
      jest.spyOn(pilotService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pilot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pilot }));
      saveSubject.complete();

      // THEN
      expect(pilotService.create).toHaveBeenCalledWith(pilot);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pilot>>();
      const pilot = { id: 123 };
      jest.spyOn(pilotService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pilot });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pilotService.update).toHaveBeenCalledWith(pilot);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
