package com.dev.server.contoller;

import com.dev.server.service.TestFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileTestController {
    private final TestFileService testFileService;

    @PostMapping("/upload")
    public void save(@RequestParam("file") MultipartFile file) {
        testFileService.save(file);
    }

    @GetMapping()
    public ResponseEntity<Resource> get() throws IOException {
        Resource resource = testFileService.get();

        return ResponseEntity.ok()
                //.contentType(MediaType.parseMediaType(Files.probeContentType(resource.getFile().toPath())))
                .body(resource);
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        testFileService.delete();
    }
}
