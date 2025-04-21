import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { LoginComponent } from './app/components/login/login.component';

import { OrderComponent } from './app/components/order/order.component';
import { appConfig } from './app/app.config';

bootstrapApplication(OrderComponent, appConfig)
  .catch((err) => console.error(err));
