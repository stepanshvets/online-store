package com.dev.server.dto;

import com.dev.server.model.CartProduct;
import com.dev.server.model.Category;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartProductDto {
    private Long id;

    private Long productId;

    @Min(value = 1)
    private Long count;

    private ProductDto productDto;

    public static CartProductDto toDto(CartProduct cartProduct) {
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setId(cartProduct.getId());
        cartProductDto.setCount(cartProduct.getCount());
        cartProductDto.setProductDto(ProductDto.toDto(cartProduct.getProduct()));
        return cartProductDto;
    }

    public static List<CartProductDto> toListDto(List<CartProduct> list) {
        return list.stream().map(cartProduct -> toDto(cartProduct))
                .collect(Collectors.toList());
    }
}
