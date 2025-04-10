import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { RegisterComponent } from './app/register/register.component';
import { LoginComponent } from './app/login/login.component';
import { appConfig } from './app/app.config';

bootstrapApplication(LoginComponent, appConfig)
  .catch((err) => console.error(err));
