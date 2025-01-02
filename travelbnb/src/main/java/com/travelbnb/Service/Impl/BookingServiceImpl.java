package com.travelbnb.Service.Impl;

import com.travelbnb.Controller.BookingController;
import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Booking;
import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Room;
import com.travelbnb.Exception.ResourceNotFoundException;
import com.travelbnb.Service.BookingService;
import com.travelbnb.Service.BucketService;
import com.travelbnb.Service.PDFService;
import com.travelbnb.payload.BookingDto;
import com.travelbnb.repository.BookingRepository;
import com.travelbnb.repository.PropertyRepository;
import com.travelbnb.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private RoomRepository roomRepository;
    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private PDFService pdfService;
    private BucketService bucketService;



    public BookingServiceImpl(RoomRepository roomRepository, BookingRepository bookingRepository, PropertyRepository propertyRepository, PDFService pdfService, BucketService bucketService) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.pdfService = pdfService;
        this.bucketService = bucketService;
    }

    @Transactional
    @Override
    public BookingDto createBooking(long propertyId, AppUser user, BookingDto bookingDto, long roomId) {

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));
            // Check if the room is already booked
            if (room.isStatus()) {
                throw new ResourceNotFoundException("Room is already booked.");
            } else {
                // Set the room status to booked
                room.setStatus(false);
                roomRepository.save(room);
            }
            // Get the property details
            Property property = propertyRepository.findById(propertyId).orElseThrow(
                    () -> new ResourceNotFoundException("Property not found with id: " + propertyId)
            );
            bookingDto.setRoom(room);
            bookingDto.setProperty(property);
            bookingDto.setAppUser(user);

            // Calculate the final price
            int nightlyPrice = property.getNightlyPrice();
            int totalPrice = nightlyPrice * bookingDto.getTotalNights();
            int gstAmount = (totalPrice * 18) / 100;
            int finalPrice = totalPrice + gstAmount;
            bookingDto.setPrice(finalPrice);

            // Save the booking

            Booking booking = dtoToEntity(bookingDto);
            Booking savedBooking = bookingRepository.save(booking);

//            after complete of booking a room use versioning to avoid concurrency booking
            // Generate and upload the booking confirmation PDF
            boolean b = pdfService.generatePDF("E://pdf_example//" + "booking-confirmation-id" + savedBooking.getId() + ".pdf", bookingDto);
            if (b) {
                try {
                    MultipartFile file = BookingController.convert("E://pdf_example//iTextHelloWorld.pdf");
                    //upload the booking confirmation PDF to S3
                    String uploadedFileUrl = bucketService.uploadFile(file, "biswa2k");
                    System.out.println(uploadedFileUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                BookingDto dto = entityToDto(savedBooking);
                return dto;
            }catch(ObjectOptimisticLockingFailureException e){
                throw new ResourceNotFoundException("Failed to book room due to concurrent updates. Please try again.");
            }
        }

    private BookingDto entityToDto(Booking booking) {
    BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setName(booking.getName());
        dto.setEmail(booking.getEmail());
        dto.setMobile(booking.getMobile());
        dto.setTotalNights(booking.getTotalNights());
        dto.setPrice(booking.getPrice());
        dto.setProperty(booking.getProperty());
        dto.setAppUser(booking.getAppUser());
        dto.setRoom(booking.getRoom());
        return dto;
    }

    private Booking dtoToEntity(BookingDto bookingDto) {
    Booking booking = new Booking();
    booking.setName(bookingDto.getName());
    booking.setEmail(bookingDto.getEmail());
    booking.setMobile(bookingDto.getMobile());
    booking.setPrice(bookingDto.getPrice());
    booking.setTotalNights(bookingDto.getTotalNights());
    booking.setProperty(bookingDto.getProperty());
    booking.setAppUser(bookingDto.getAppUser());
    booking.setRoom(bookingDto.getRoom());
        return booking;
    }

    @Override
    public List<Room> getAvailableRooms(long propertyId) {
        return roomRepository.findByPropertyIdAndStatus(propertyId,false);
    }
}








