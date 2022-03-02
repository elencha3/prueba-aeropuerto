import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pilot',
        data: { pageTitle: 'Pilots' },
        loadChildren: () => import('./pilot/pilot.module').then(m => m.PilotModule),
      },
      {
        path: 'crew',
        data: { pageTitle: 'Crews' },
        loadChildren: () => import('./crew/crew.module').then(m => m.CrewModule),
      },
      {
        path: 'plane',
        data: { pageTitle: 'Planes' },
        loadChildren: () => import('./plane/plane.module').then(m => m.PlaneModule),
      },
      {
        path: 'flight',
        data: { pageTitle: 'Flights' },
        loadChildren: () => import('./flight/flight.module').then(m => m.FlightModule),
      },
      {
        path: 'airport',
        data: { pageTitle: 'Airports' },
        loadChildren: () => import('./airport/airport.module').then(m => m.AirportModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
