import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component'; 
import { FooterComponent } from '../footer/footer.component'; 
import { Product } from '../../models/product';
import { ProductImage } from '../../models/product.image';
import { environment } from '../../environments/environment';
import { CartService } from '../../service/cart.service';
import { ProductService } from '../../service/product.service';
import { CategoryService } from '../../service/category.service';
import { Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-detail-product',
  imports:  [HeaderComponent,FooterComponent,CommonModule],
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.scss'
})

export class DetailProductComponent implements OnInit {
  product?: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1;
  constructor(
    private productService: ProductService,
    private cartService: CartService,
    // private categoryService: CategoryService,
    // private router: Router,
    // private activatedRoute: ActivatedRoute,
    ) {
      
    }
    ngOnInit() {
      // Lấy productId từ URL      
      //const idParam = this.activatedRoute.snapshot.paramMap.get('id');
      debugger
      // this.cartService.clearCart();
      const idParam = 9 //fake tạm 1 giá trị
      if (idParam !== null) {
        this.productId = +idParam;
      }
      if (!isNaN(this.productId)) {
        this.productService.getDetailProduct(this.productId).subscribe({
          next: (response: any) => {            
            // Lấy danh sách ảnh sản phẩm và thay đổi URL
            debugger
            if (response.product_images && response.product_images.length > 0) {
              console.log("Response images:", response.product_images);
              response.product_images.forEach((product_image:ProductImage) => {
                product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
              });
            }            
            debugger
            this.product = response 
            // Bắt đầu với ảnh đầu tiên
            this.showImage(0);
          },
          complete: () => {
            debugger;
          },
          error: (error: any) => {
            debugger;
            console.error('Error fetching detail:', error);
          }
        });    
      } else {
        console.error('Invalid productId:', idParam);
      }      
    }
    showImage(index: number): void {
      debugger
      if (this.product && this.product.product_images && 
          this.product.product_images.length > 0) {
        // Đảm bảo index nằm trong khoảng hợp lệ        
        if (index < 0) {
          index = 0;
        } else if (index >= this.product.product_images.length) {
          index = this.product.product_images.length - 1;
        }        
        // Gán index hiện tại và cập nhật ảnh hiển thị
        this.currentImageIndex = index;
      }
    }
    thumbnailClick(index: number) {
      debugger
      // Gọi khi một thumbnail được bấm
      this.currentImageIndex = index; // Cập nhật currentImageIndex
    }  
    nextImage(): void {
      debugger
      this.showImage(this.currentImageIndex + 1);
    }
  
    previousImage(): void {
      debugger
      this.showImage(this.currentImageIndex - 1);
    }      
    addToCart(): void {
      debugger
      if (this.product) {
        this.cartService.addToCart(this.product.id, this.quantity);
      } else {
        // Xử lý khi product là null
        console.error('Không thể thêm sản phẩm vào giỏ hàng vì product là null.');
      }
    }    
        
    increaseQuantity(): void {
      this.quantity++;
    }
    
    decreaseQuantity(): void {
      if (this.quantity > 1) {
        this.quantity--;
      }
    }
    
    buyNow(): void {
      // Thực hiện xử lý khi người dùng muốn mua ngay
    }    
}

