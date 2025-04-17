import { Component } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component'; 
import { FooterComponent } from '../../components/footer/footer.component'; 

@Component({
  selector: 'app-order',
  imports:  [HeaderComponent,FooterComponent],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent {

}
