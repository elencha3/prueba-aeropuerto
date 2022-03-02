import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CrewService } from '../service/crew.service';
import { ICrew, Crew } from '../crew.model';

import { CrewUpdateComponent } from './crew-update.component';

describe('Crew Management Update Component', () => {
  let comp: CrewUpdateComponent;
  let fixture: ComponentFixture<CrewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crewService: CrewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CrewUpdateComponent],
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
      .overrideTemplate(CrewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crewService = TestBed.inject(CrewService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crew: ICrew = { id: 456 };

      activatedRoute.data = of({ crew });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crew));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Crew>>();
      const crew = { id: 123 };
      jest.spyOn(crewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crew });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crew }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crewService.update).toHaveBeenCalledWith(crew);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Crew>>();
      const crew = new Crew();
      jest.spyOn(crewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crew });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crew }));
      saveSubject.complete();

      // THEN
      expect(crewService.create).toHaveBeenCalledWith(crew);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Crew>>();
      const crew = { id: 123 };
      jest.spyOn(crewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crew });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crewService.update).toHaveBeenCalledWith(crew);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
