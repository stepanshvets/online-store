package com.dev.server.contoller;

import com.dev.server.dto.CartProductDto;
import com.dev.server.dto.CategoryDto;
import com.dev.server.dto.ProductDto;
import com.dev.server.model.Cart;
import com.dev.server.model.CartProduct;
import com.dev.server.model.Category;
import com.dev.server.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/products")
    public List<CartProductDto> get() {
        return CartProductDto.toListDto(cartService.getCartProducts());
    }

    @PostMapping("/products")
    public CartProductDto add(@RequestBody CartProductDto cartProductDto) {
         return CartProductDto.toDto(cartService.addCartProduct(cartProductDto));
    }

    @PatchMapping("/products/{id}")
    public CartProductDto updateCountOfProductInCart(@PathVariable("id") Long id, @RequestBody CartProductDto cartProductDto) {
        return CartProductDto.toDto(cartService.updateCountOfProductInCart(id, cartProductDto));
    }

    @DeleteMapping("/products/{id}")
    public void delete(@PathVariable("id") Long id) {
        cartService.delete(id);
    }
}
