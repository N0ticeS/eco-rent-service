package ua.nure.st.kpp.example.demo.service;

import ua.nure.st.kpp.example.demo.entity.Rental;

import java.util.List;

public interface RentalService {
    void createRental(Rental rental);

    void updateRental(Rental rental);

    void deleteRental(int id);

    Rental getRentalById(int id);

    List<Rental> getRentalsByCustomerId(int customerId);

    List<Rental> getRentalsByInstanceId(int instanceId);

    List<Rental> getAllRentals();
}
