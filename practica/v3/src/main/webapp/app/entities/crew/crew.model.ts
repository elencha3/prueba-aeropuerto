import { IFlight } from 'app/entities/flight/flight.model';

export interface ICrew {
  id?: number;
  name?: string;
  dni?: string;
  adress?: string;
  email?: string;
  pictureContentType?: string | null;
  picture?: string | null;
  flights?: IFlight[] | null;
}

export class Crew implements ICrew {
  constructor(
    public id?: number,
    public name?: string,
    public dni?: string,
    public adress?: string,
    public email?: string,
    public pictureContentType?: string | null,
    public picture?: string | null,
    public flights?: IFlight[] | null
  ) {}
}

export function getCrewIdentifier(crew: ICrew): number | undefined {
  return crew.id;
}
