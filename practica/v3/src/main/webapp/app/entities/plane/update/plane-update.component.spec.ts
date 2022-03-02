import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlaneService } from '../service/plane.service';
import { IPlane, Plane } from '../plane.model';

import { PlaneUpdateComponent } from './plane-update.component';

describe('Plane Management Update Component', () => {
  let comp: PlaneUpdateComponent;
  let fixture: ComponentFixture<PlaneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let planeService: PlaneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlaneUpdateComponent],
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
      .overrideTemplate(PlaneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    planeService = TestBed.inject(PlaneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const plane: IPlane = { id: 456 };

      activatedRoute.data = of({ plane });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(plane));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Plane>>();
      const plane = { id: 123 };
      jest.spyOn(planeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plane });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plane }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(planeService.update).toHaveBeenCalledWith(plane);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Plane>>();
      const plane = new Plane();
      jest.spyOn(planeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plane });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plane }));
      saveSubject.complete();

      // THEN
      expect(planeService.create).toHaveBeenCalledWith(plane);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Plane>>();
      const plane = { id: 123 };
      jest.spyOn(planeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plane });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(planeService.update).toHaveBeenCalledWith(plane);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
