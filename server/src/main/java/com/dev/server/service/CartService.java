package com.dev.server.service;

import com.dev.server.dto.CartProductDto;
import com.dev.server.exception.GeneralException;
import com.dev.server.model.Cart;
import com.dev.server.model.CartProduct;
import com.dev.server.model.Product;
import com.dev.server.model.User;
import com.dev.server.repository.CartProductRepository;
import com.dev.server.repository.CartRepository;
import com.dev.server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;

    private final UserService userService;

    @Transactional(readOnly = true)
    public Cart get(User user) {
        return cartRepository.findByUser(user).orElse(null);
    }

    @Transactional(readOnly = true)
    public Cart get() {
        return cartRepository.findByUser(userService.getAuthUser()).orElse(null);
    }

    @Transactional
    public Cart create() {
        Cart cart = new Cart();
        cart.setUser(userService.getAuthUser());
        return cartRepository.save(cart);
    }

    @Transactional
    public CartProduct addCartProduct(@Valid CartProductDto cartProductDto) {
        Cart cart = get(userService.getAuthUser());
        if (cart == null) {
            cart = create();
        }

        Product product = productRepository.findById(cartProductDto.getProductId())
                .orElseThrow(() -> new GeneralException("Product with id " + cartProductDto.getProductId() + "not found",
                        HttpStatus.CONFLICT));

//        if (cartProductDto.getCount() < 1) {
//            throw new GeneralException("Count of product should be more than 0", HttpStatus.CONFLICT);
//        }

        if (product.getCount() < cartProductDto.getCount()) {
            throw new GeneralException("Available only " + product.getCount() + " products", HttpStatus.CONFLICT);
        }

        product.setCount(
                product.getCount() - cartProductDto.getCount()
        );

        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setCount(cartProductDto.getCount());

        return cartProductRepository.save(cartProduct);
    }

    @Transactional
    public CartProduct updateCountOfProductInCart(Long id, @Valid CartProductDto cartProductDto) {
        CartProduct cartProduct = cartProductRepository.findById(id)
                .orElseThrow(() -> new GeneralException("CartProduct with id " + cartProductDto.getProductId() + "not found",
                        HttpStatus.CONFLICT));

        Product product = cartProduct.getProduct();

//        if (cartProductDto.getCount() > 0) {
//            throw new GeneralException("Count of products is more than 0", HttpStatus.CONFLICT);
//        }

        if (product.getCount() + cartProduct.getCount() < cartProductDto.getCount()) {
            throw new GeneralException("Available only " + product.getCount() + " products", HttpStatus.CONFLICT);
        }

        product.setCount(
                product.getCount() - cartProductDto.getCount()
        );

        cartProduct.setCount(cartProductDto.getCount());

        return cartProductRepository.save(cartProduct);
    }

    @Transactional
    public void delete(Long id) {
        cartProductRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CartProduct> getCartProducts() {
        Optional<Cart> optionalCart = cartRepository.findByUser(userService.getAuthUser());

        if (optionalCart.isPresent()) {
            return optionalCart.get().getCartProducts();
        }

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public void ifAllProductsAvailableInCart() {
        Optional<Cart> optionalCart = cartRepository.findByUser(userService.getAuthUser());

        if (optionalCart.isEmpty()) {
            return;
        }

        List<CartProduct> cartProducts = optionalCart.get().getCartProducts();

        StringBuilder message = new StringBuilder();

        for (CartProduct cartProduct: optionalCart.get().getCartProducts()) {
            Product product = cartProduct.getProduct();
            if (cartProduct.getCount() > product.getCount()) {
                message.append("Available only ").append(product.getCount()).append(product.getName()).append(";");
            }
        }

        if (!cartProducts.isEmpty()) {
            throw new GeneralException(message.toString(), HttpStatus.CONFLICT);
        }
    }
}
