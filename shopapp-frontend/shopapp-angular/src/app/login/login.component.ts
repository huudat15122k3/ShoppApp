import { Component, ViewChild } from '@angular/core';
import { HeaderComponent } from '../header/header.component'; 
import { FooterComponent } from '../footer/footer.component'; 
import { LoginDTO } from '../dtos/user/login.dto';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { CommonModule } from '@angular/common';
import { NgForm } from '@angular/forms';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-login',
  imports:  [HeaderComponent,FooterComponent, CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
    @ViewChild('loginForm') loginForm!: NgForm;
  
  phoneNumber: string ;
  password: string ;

    constructor(private router: Router, private UserService: UserService) {
      this.phoneNumber = '15122003';
      this.password = '123456';
    }

  login() {
      const message = `Phone: ${this.phoneNumber}, 
      Password: ${this.password}`;
      debugger
  
      
  
      const loginDTO:LoginDTO = {
        "phone_number": this.phoneNumber,
        "password": this.password
      }
  
      this.UserService.login(loginDTO).subscribe({
        next: (response: any) => {
          debugger
          //this.router.navigate(['/login']);
  
        },
        complete: () => { debugger },
        error: (error: any) => {
          alert(`Register failed! Error:  + ${error.error}`);
        }});
    }
}
