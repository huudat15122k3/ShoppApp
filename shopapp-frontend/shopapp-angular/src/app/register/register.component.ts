import { Component, ViewChild } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgFor } from '@angular/common';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [FooterComponent, FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  //Khai báo biến tương ứng với các trường dữ liệu của form đăng ký
  phone: string;
  password: string;
  retypePassword: string;
  address: string;
  fullName: string
  isAccepted: boolean;
  dateOfBirth: Date;
  //Hàm khởi tạo của component
  constructor() {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.phone = '';
    this.address = '';
    this.fullName = '';
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(1990);
  }

  register() {
    const message = `Phone: ${this.phone}, 
    Password: ${this.password},
     Address: ${this.address},
      Full name: ${this.fullName},
       Is accepted: ${this.isAccepted},
        Date of birth: ${this.dateOfBirth}`;
    alert(message);
  }

  checkPasswordsMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': true });
    } else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }

  checkAge() {
    if (this.dateOfBirth) {
      const birthDate = new Date(this.dateOfBirth); // Convert from string to Date
      const today = new Date();
      const age = today.getFullYear() - birthDate.getFullYear();
      if (age < 18) {
        this.registerForm.form.controls['dateOfBirth'].setErrors({ 'underAge': true });
      } else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}
