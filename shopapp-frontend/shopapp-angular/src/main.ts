import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { DetailProductComponent } from './app/components/detail-product/detail-product.component';
import { OrderConfirmComponent } from './app/components/order-confirm/order-confirm.component';
import { appConfig } from './app/app.config';

bootstrapApplication(OrderConfirmComponent, appConfig)
  .catch((err) => console.error(err));
