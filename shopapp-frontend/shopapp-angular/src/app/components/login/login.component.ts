import { Component, ViewChild } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { LoginDTO } from '../../dtos/user/login.dto';
import { Router } from '@angular/router';
import { UserService } from '../../service/user.service';
import { CommonModule } from '@angular/common';
import { NgForm } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { LoginRespone } from '../../responses/user/login.response';
import { TokenService } from '../../service/token.service';
import { RoleService } from '../../service/role.service'; 
import { Role } from '../../models/role';

@Component({
  selector: 'app-login',
  imports: [HeaderComponent, FooterComponent, CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;

  phoneNumber: string;
  password: string;

  roles: Role[] = []; // Mảng roles
  rememberMe: boolean = true;
  selectedRole: Role | undefined;// Biến để lưu giá trị được chọn từ dropdown
  constructor(
    private router: Router,
    private UserService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
  ) {
    this.phoneNumber = '15122003';
    this.password = '123456';
  }
  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`);
    //how to validate ? phone must be at least 6 characters
  }

  ngOnInit() {
    //Gọi API lấy danh sách roles và lưu vào biến roles
    debugger
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        debugger
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined; // Chọn role đầu tiên trong danh sách
      },
      error: (error: any) => {
        console.error('Error fetching roles:', error);
      }
    })
  }

  login() {
    const message = `Phone: ${this.phoneNumber}, 
      Password: ${this.password}`;
    debugger



    const loginDTO: LoginDTO = {
      phone_number: this.phoneNumber,
      password: this.password,
      role_id: this.selectedRole?.id ?? 1  // Thêm role_id vào DTO nếu có
    }

    this.UserService.login(loginDTO).subscribe({
      next: (response: LoginRespone) => {
        debugger
        const { token } = response;
        if (this.rememberMe) {
          this.tokenService.setToken(token);
        }   
        //this.router.navigate(['/login']);

      },
      complete: () => { debugger },
      error: (error: any) => {
        alert(error?.error?.message);
      }
    });
  }
}
