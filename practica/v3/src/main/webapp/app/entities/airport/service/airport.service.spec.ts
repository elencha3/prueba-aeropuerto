import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAirport, Airport } from '../airport.model';

import { AirportService } from './airport.service';

describe('Airport Service', () => {
  let service: AirportService;
  let httpMock: HttpTestingController;
  let elemDefault: IAirport;
  let expectedResult: IAirport | IAirport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AirportService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Airport', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Airport()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Airport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Airport', () => {
      const patchObject = Object.assign({}, new Airport());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Airport', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Airport', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAirportToCollectionIfMissing', () => {
      it('should add a Airport to an empty array', () => {
        const airport: IAirport = { id: 123 };
        expectedResult = service.addAirportToCollectionIfMissing([], airport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(airport);
      });

      it('should not add a Airport to an array that contains it', () => {
        const airport: IAirport = { id: 123 };
        const airportCollection: IAirport[] = [
          {
            ...airport,
          },
          { id: 456 },
        ];
        expectedResult = service.addAirportToCollectionIfMissing(airportCollection, airport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Airport to an array that doesn't contain it", () => {
        const airport: IAirport = { id: 123 };
        const airportCollection: IAirport[] = [{ id: 456 }];
        expectedResult = service.addAirportToCollectionIfMissing(airportCollection, airport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(airport);
      });

      it('should add only unique Airport to an array', () => {
        const airportArray: IAirport[] = [{ id: 123 }, { id: 456 }, { id: 82122 }];
        const airportCollection: IAirport[] = [{ id: 123 }];
        expectedResult = service.addAirportToCollectionIfMissing(airportCollection, ...airportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const airport: IAirport = { id: 123 };
        const airport2: IAirport = { id: 456 };
        expectedResult = service.addAirportToCollectionIfMissing([], airport, airport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(airport);
        expect(expectedResult).toContain(airport2);
      });

      it('should accept null and undefined values', () => {
        const airport: IAirport = { id: 123 };
        expectedResult = service.addAirportToCollectionIfMissing([], null, airport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(airport);
      });

      it('should return initial array if no Airport is added', () => {
        const airportCollection: IAirport[] = [{ id: 123 }];
        expectedResult = service.addAirportToCollectionIfMissing(airportCollection, undefined, null);
        expect(expectedResult).toEqual(airportCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
