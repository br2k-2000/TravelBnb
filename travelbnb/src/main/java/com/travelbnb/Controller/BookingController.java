package com.travelbnb.Controller;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Room;
import com.travelbnb.Service.BookingService;
import com.travelbnb.payload.BookingDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.Files.readAllBytes;


@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @RequestParam long propertyId,
            @AuthenticationPrincipal AppUser user,
            @RequestBody BookingDto bookingDto,
            @RequestParam long roomId
    ) {
        BookingDto createdBooking = bookingService.createBooking(propertyId, user, bookingDto,roomId);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }



    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            File uploadedFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
            file.transferTo(uploadedFile);

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    //for converting filePath into MultiPartFile
    public static MultipartFile convert(String filePath) throws IOException {
        // Load the file from the specified path
        File file = new File(filePath);

        // Read the file content into array
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // Convert byte array to a ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(fileContent);

        // Create MultipartFile from Resource
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                // You can determine and set the content type here
                try {
                    return Files.probeContentType(file.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean isEmpty() {
                return file.length() == 0;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(fileContent);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try (InputStream inputStream = new ByteArrayInputStream(fileContent);
                     OutputStream outputStream = new FileOutputStream(dest)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        };

        return multipartFile;
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<Room>> getAvailableRooms(@RequestParam long propertyId) {
        List<Room> availableRooms = bookingService.getAvailableRooms(propertyId);
        return new ResponseEntity<>(availableRooms,HttpStatus.OK);
    }
}

