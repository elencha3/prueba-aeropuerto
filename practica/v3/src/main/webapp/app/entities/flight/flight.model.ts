import { IPilot } from 'app/entities/pilot/pilot.model';
import { IPlane } from 'app/entities/plane/plane.model';
import { ICrew } from 'app/entities/crew/crew.model';
import { IAirport } from 'app/entities/airport/airport.model';

export interface IFlight {
  id?: number;
  numFlight?: string | null;
  pilot?: IPilot | null;
  plane?: IPlane | null;
  crews?: ICrew[] | null;
  origin?: IAirport | null;
  destination?: IAirport | null;
}

export class Flight implements IFlight {
  constructor(
    public id?: number,
    public numFlight?: string | null,
    public pilot?: IPilot | null,
    public plane?: IPlane | null,
    public crews?: ICrew[] | null,
    public origin?: IAirport | null,
    public destination?: IAirport | null
  ) {}
}

export function getFlightIdentifier(flight: IFlight): number | undefined {
  return flight.id;
}
