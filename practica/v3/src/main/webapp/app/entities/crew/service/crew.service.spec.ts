import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICrew, Crew } from '../crew.model';

import { CrewService } from './crew.service';

describe('Crew Service', () => {
  let service: CrewService;
  let httpMock: HttpTestingController;
  let elemDefault: ICrew;
  let expectedResult: ICrew | ICrew[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CrewService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      dni: 'AAAAAAA',
      adress: 'AAAAAAA',
      email: 'AAAAAAA',
      pictureContentType: 'image/png',
      picture: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Crew', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Crew()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Crew', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          dni: 'BBBBBB',
          adress: 'BBBBBB',
          email: 'BBBBBB',
          picture: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Crew', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          dni: 'BBBBBB',
          email: 'BBBBBB',
          picture: 'BBBBBB',
        },
        new Crew()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Crew', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          dni: 'BBBBBB',
          adress: 'BBBBBB',
          email: 'BBBBBB',
          picture: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Crew', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCrewToCollectionIfMissing', () => {
      it('should add a Crew to an empty array', () => {
        const crew: ICrew = { id: 123 };
        expectedResult = service.addCrewToCollectionIfMissing([], crew);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crew);
      });

      it('should not add a Crew to an array that contains it', () => {
        const crew: ICrew = { id: 123 };
        const crewCollection: ICrew[] = [
          {
            ...crew,
          },
          { id: 456 },
        ];
        expectedResult = service.addCrewToCollectionIfMissing(crewCollection, crew);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Crew to an array that doesn't contain it", () => {
        const crew: ICrew = { id: 123 };
        const crewCollection: ICrew[] = [{ id: 456 }];
        expectedResult = service.addCrewToCollectionIfMissing(crewCollection, crew);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crew);
      });

      it('should add only unique Crew to an array', () => {
        const crewArray: ICrew[] = [{ id: 123 }, { id: 456 }, { id: 9248 }];
        const crewCollection: ICrew[] = [{ id: 123 }];
        expectedResult = service.addCrewToCollectionIfMissing(crewCollection, ...crewArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const crew: ICrew = { id: 123 };
        const crew2: ICrew = { id: 456 };
        expectedResult = service.addCrewToCollectionIfMissing([], crew, crew2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(crew);
        expect(expectedResult).toContain(crew2);
      });

      it('should accept null and undefined values', () => {
        const crew: ICrew = { id: 123 };
        expectedResult = service.addCrewToCollectionIfMissing([], null, crew, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(crew);
      });

      it('should return initial array if no Crew is added', () => {
        const crewCollection: ICrew[] = [{ id: 123 }];
        expectedResult = service.addCrewToCollectionIfMissing(crewCollection, undefined, null);
        expect(expectedResult).toEqual(crewCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
