package com.zalo.Spring_Zalo.Controller;

import com.zalo.Spring_Zalo.Entities.Cart;
import com.zalo.Spring_Zalo.Repo.CartMongoRepo;
import com.zalo.Spring_Zalo.Service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private CartMongoRepo cartRepo;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @PostMapping("/")
    public ResponseEntity<?> getAllCarts(@RequestBody Cart cart) {
        cart.setId(sequenceGeneratorService.generateSequence(Cart.SEQUENCE_NAME));
        Cart c = cartRepo.save(cart);
        return new ResponseEntity<>(cartRepo.findAll(), HttpStatus.OK);
    }
}
