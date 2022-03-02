import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPilot, Pilot } from '../pilot.model';

import { PilotService } from './pilot.service';

describe('Pilot Service', () => {
  let service: PilotService;
  let httpMock: HttpTestingController;
  let elemDefault: IPilot;
  let expectedResult: IPilot | IPilot[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PilotService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      dni: 'AAAAAAA',
      adress: 'AAAAAAA',
      email: 'AAAAAAA',
      horasVuelo: 0,
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

    it('should create a Pilot', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Pilot()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pilot', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          dni: 'BBBBBB',
          adress: 'BBBBBB',
          email: 'BBBBBB',
          horasVuelo: 1,
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

    it('should partial update a Pilot', () => {
      const patchObject = Object.assign({}, new Pilot());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pilot', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          dni: 'BBBBBB',
          adress: 'BBBBBB',
          email: 'BBBBBB',
          horasVuelo: 1,
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

    it('should delete a Pilot', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPilotToCollectionIfMissing', () => {
      it('should add a Pilot to an empty array', () => {
        const pilot: IPilot = { id: 123 };
        expectedResult = service.addPilotToCollectionIfMissing([], pilot);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pilot);
      });

      it('should not add a Pilot to an array that contains it', () => {
        const pilot: IPilot = { id: 123 };
        const pilotCollection: IPilot[] = [
          {
            ...pilot,
          },
          { id: 456 },
        ];
        expectedResult = service.addPilotToCollectionIfMissing(pilotCollection, pilot);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pilot to an array that doesn't contain it", () => {
        const pilot: IPilot = { id: 123 };
        const pilotCollection: IPilot[] = [{ id: 456 }];
        expectedResult = service.addPilotToCollectionIfMissing(pilotCollection, pilot);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pilot);
      });

      it('should add only unique Pilot to an array', () => {
        const pilotArray: IPilot[] = [{ id: 123 }, { id: 456 }, { id: 40670 }];
        const pilotCollection: IPilot[] = [{ id: 123 }];
        expectedResult = service.addPilotToCollectionIfMissing(pilotCollection, ...pilotArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pilot: IPilot = { id: 123 };
        const pilot2: IPilot = { id: 456 };
        expectedResult = service.addPilotToCollectionIfMissing([], pilot, pilot2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pilot);
        expect(expectedResult).toContain(pilot2);
      });

      it('should accept null and undefined values', () => {
        const pilot: IPilot = { id: 123 };
        expectedResult = service.addPilotToCollectionIfMissing([], null, pilot, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pilot);
      });

      it('should return initial array if no Pilot is added', () => {
        const pilotCollection: IPilot[] = [{ id: 123 }];
        expectedResult = service.addPilotToCollectionIfMissing(pilotCollection, undefined, null);
        expect(expectedResult).toEqual(pilotCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
