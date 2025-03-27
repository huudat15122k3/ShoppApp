import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { HomeComponent } from './app/home/home.component';
import { OrderComponent } from './app/order/order.component';
import { OrderConfirmComponent } from './app/order-confirm/order-confirm.component';
import { LoginComponent } from './app/login/login.component';
import { RegisterComponent } from './app/register/register.component';
import { DetailProductComponent } from './app/detail-product/detail-product.component';

// bootstrapApplication(HomeComponent, appConfig)
//   .catch((err) => console.error(err));

  bootstrapApplication(HomeComponent, appConfig)
  .catch((err) => console.error(err));
  bootstrapApplication(OrderComponent, appConfig)
  .catch((err) => console.error(err));
  bootstrapApplication(OrderConfirmComponent, appConfig)
  .catch((err) => console.error(err));
  bootstrapApplication(LoginComponent, appConfig)
  .catch((err) => console.error(err));
  bootstrapApplication(RegisterComponent, appConfig)
  .catch((err) => console.error(err));
  bootstrapApplication(DetailProductComponent, appConfig)
  .catch((err) => console.error(err));
  

