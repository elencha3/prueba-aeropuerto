export interface IAirport {
  id?: number;
  name?: string;
}

export class Airport implements IAirport {
  constructor(public id?: number, public name?: string) {}
}

export function getAirportIdentifier(airport: IAirport): number | undefined {
  return airport.id;
}
