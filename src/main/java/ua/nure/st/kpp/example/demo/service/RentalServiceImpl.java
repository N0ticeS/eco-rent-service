package ua.nure.st.kpp.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.st.kpp.example.demo.dao.DataAccessException;
import ua.nure.st.kpp.example.demo.dao.daointerface.RentalDAO;
import ua.nure.st.kpp.example.demo.entity.Rental;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalDAO dao;
    private static final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    @Autowired
    public RentalServiceImpl(RentalDAO dao) {
        this.dao = dao;
    }

    @Override
    public void createRental(Rental rental) {
        try {
            dao.createRental(rental);
        } catch (Exception e) {
            log.error("Error creating rental: {}", e.getMessage(), e);
            throw new DataAccessException("Error creating rental. Please try again" + e.getMessage());
        }
    }

    @Override
    public void updateRental(Rental rental) {
        try {
            dao.updateRental(rental);
        } catch (Exception e) {
            log.error("Error updating rental: {}", e.getMessage(), e);
            throw new DataAccessException("Error updating rental. Please try again" + e.getMessage());
        }
    }

    @Override
    public void deleteRental(int id) {
        try {
            dao.deleteRental(id);
        } catch (Exception e) {
            log.error("Error deleting rental: {}", e.getMessage(), e);
            throw new DataAccessException("Error deleting rental: " + e.getMessage());
        }
    }

    @Override
    public Rental getRentalById(int id) {
        try {
            return dao.getRentalById(id);
        } catch (Exception e) {
            log.error("Error getting rental: {}", e.getMessage(), e);
            throw new DataAccessException("Error getting rental: " + e.getMessage());
        }
    }

    @Override
    public List<Rental> getRentalsByCustomerId(int customerId) {
        try {
            return dao.getRentalsByCustomerId(customerId);
        } catch (Exception e) {
            log.error("Error getting rentals: {}", e.getMessage(), e);
            throw new DataAccessException("Error getting rentals: " + e.getMessage());
        }
    }

    @Override
    public List<Rental> getRentalsByInstanceId(int instanceId) {
        try {
            return dao.getRentalsByInstanceId(instanceId);
        } catch (Exception e) {
            log.error("Error getting rentals: {}", e.getMessage(), e);
            throw new DataAccessException("Error getting rentals: " + e.getMessage());
        }
    }

    @Override
    public List<Rental> getAllRentals() {
        return dao.getAllRentals();
    }
}
