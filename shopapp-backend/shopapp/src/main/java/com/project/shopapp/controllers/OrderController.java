package com.project.shopapp.controllers;

import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.services.IOrderService;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/user/{user_id}")// http://localhost:8088/api/v1/orders/user/1
    public ResponseEntity<?> getOrders(
            @Valid @PathVariable("user_id") Long userId
    ){
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")// http://localhost:8088/api/v1/orders/user/1
    public ResponseEntity<?> getOrder(
            @Valid @PathVariable("id") Long id
    ){
        try {
            Order existingOrder = orderService.getOrder(id);
            return ResponseEntity.ok(existingOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("")
    public  ResponseEntity<?> createOrder(
                @RequestBody @Valid OrderDTO orderDTO,
                BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO ){

        try {
            Order orderUpdated = orderService.updateOrder(id,orderDTO);
            return ResponseEntity.ok(orderUpdated);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable long id){
        //Xóa mềm => active = false
        orderService.deleteOrder(id);
        return ResponseEntity.ok(localizationUtils
                .getLocalizedMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY));
    }
}
