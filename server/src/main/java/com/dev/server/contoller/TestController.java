package com.dev.server.contoller;

import com.dev.server.dto.TestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/free")
    public ResponseEntity<?> free() {
        Map<Object, Object> response = new HashMap<>();
        response.put("free", "ok");
        response.put("time", new Date());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/foo")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> foo() {
        Map<Object, Object> response = new HashMap<>();
        response.put("foo", "ok");
        response.put("time", new Date());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bar")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> bar() {
        Map<Object, Object> response = new HashMap<>();
        response.put("bar", "ok");
        response.put("time", new Date());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test")
    public void test(@RequestBody @Valid TestDto testDTO) {
        System.out.println(testDTO);
//        return testDTO;
    }
}
