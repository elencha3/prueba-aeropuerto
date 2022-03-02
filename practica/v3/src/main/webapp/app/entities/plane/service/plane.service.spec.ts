import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlane, Plane } from '../plane.model';

import { PlaneService } from './plane.service';

describe('Plane Service', () => {
  let service: PlaneService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlane;
  let expectedResult: IPlane | IPlane[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlaneService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      plate: 'AAAAAAA',
      type: 'AAAAAAA',
      age: 0,
      serialNumber: 'AAAAAAA',
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

    it('should create a Plane', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Plane()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Plane', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          plate: 'BBBBBB',
          type: 'BBBBBB',
          age: 1,
          serialNumber: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Plane', () => {
      const patchObject = Object.assign(
        {
          plate: 'BBBBBB',
          age: 1,
        },
        new Plane()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Plane', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          plate: 'BBBBBB',
          type: 'BBBBBB',
          age: 1,
          serialNumber: 'BBBBBB',
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

    it('should delete a Plane', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlaneToCollectionIfMissing', () => {
      it('should add a Plane to an empty array', () => {
        const plane: IPlane = { id: 123 };
        expectedResult = service.addPlaneToCollectionIfMissing([], plane);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plane);
      });

      it('should not add a Plane to an array that contains it', () => {
        const plane: IPlane = { id: 123 };
        const planeCollection: IPlane[] = [
          {
            ...plane,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlaneToCollectionIfMissing(planeCollection, plane);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Plane to an array that doesn't contain it", () => {
        const plane: IPlane = { id: 123 };
        const planeCollection: IPlane[] = [{ id: 456 }];
        expectedResult = service.addPlaneToCollectionIfMissing(planeCollection, plane);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plane);
      });

      it('should add only unique Plane to an array', () => {
        const planeArray: IPlane[] = [{ id: 123 }, { id: 456 }, { id: 80778 }];
        const planeCollection: IPlane[] = [{ id: 123 }];
        expectedResult = service.addPlaneToCollectionIfMissing(planeCollection, ...planeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const plane: IPlane = { id: 123 };
        const plane2: IPlane = { id: 456 };
        expectedResult = service.addPlaneToCollectionIfMissing([], plane, plane2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plane);
        expect(expectedResult).toContain(plane2);
      });

      it('should accept null and undefined values', () => {
        const plane: IPlane = { id: 123 };
        expectedResult = service.addPlaneToCollectionIfMissing([], null, plane, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plane);
      });

      it('should return initial array if no Plane is added', () => {
        const planeCollection: IPlane[] = [{ id: 123 }];
        expectedResult = service.addPlaneToCollectionIfMissing(planeCollection, undefined, null);
        expect(expectedResult).toEqual(planeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
