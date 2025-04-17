import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet], // ✅ Cho phép điều hướng giữa các trang
  template: `<router-outlet></router-outlet>` // ✅ Nơi hiển thị các component dựa trên đường dẫn
})
export class AppComponent {}
