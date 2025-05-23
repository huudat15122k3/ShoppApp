import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component'; 
import { FooterComponent } from '../footer/footer.component'; 
import { CommonModule } from '@angular/common';
import { ProductService } from '../../service/product.service';
import { CartService } from '../../service/cart.service';
import { Product } from '../../models/product';
import { environment } from '../../environments/environment';


@Component({
  selector: 'app-order-confirm',
  imports:  [HeaderComponent,FooterComponent,CommonModule],
  templateUrl: './order-confirm.component.html',
  styleUrl: './order-confirm.component.scss'
})
export class OrderConfirmComponent implements OnInit {
  cartItems: {product:Product,quantity:number}[]=[] // Danh sách sản phẩm trong giỏ hàng
  couponCode: string = ''; // Mã giảm giá
  totalAmount: number = 0; // Tổng tiền
  constructor(
    private productService: ProductService,
    private cartService: CartService,    
  ){}
  ngOnInit(): void {  
    // Lấy danh sách sản phẩm từ giỏ hàng
    debugger
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys()); // Chuyển danh sách ID từ Map giỏ hàng    
  
    // Gọi service để lấy thông tin sản phẩm dựa trên danh sách ID
    debugger    
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {            
        debugger
        // Lấy thông tin sản phẩm và số lượng từ danh sách sản phẩm và giỏ hàng
        this.cartItems = productIds.map((productId) => {
          debugger
          const product = products.find((p) => p.id === productId);
          if (product) {
            product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }          
          return {
            product: product!,
            quantity: cart.get(productId)!
          };
        });
        console.log('haha');
      },
      complete: () => {
        debugger;
        this.calculateTotal();
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching detail:', error);
      }
    });        
  }
  // Hàm tính tổng tiền
  calculateTotal(): void {
    this.totalAmount = this.cartItems.reduce(
        (total, item) => total + item.product.price * item.quantity,
        0
    );
}

// Hàm xử lý việc áp dụng mã giảm giá
applyCoupon(): void {
    // Viết mã xử lý áp dụng mã giảm giá ở đây
    // Cập nhật giá trị totalAmount dựa trên mã giảm giá nếu áp dụng
}
}


