package ua.nure.st.kpp.example.demo.dao.daointerface;


import org.springframework.stereotype.Repository;
import ua.nure.st.kpp.example.demo.entity.Rental;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface RentalDAO {

    void createRental(Rental rental);

    void updateRental(Rental rental);

    void deleteRental(int id);

    Rental getRentalById(int id);

    List<Rental> getRentalsByCustomerId(int customerId);

    List<Rental> getRentalsByInstanceId(int instanceId);

    List<Rental> getAllRentals();
}
