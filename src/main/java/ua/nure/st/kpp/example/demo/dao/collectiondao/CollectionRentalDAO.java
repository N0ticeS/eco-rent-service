package ua.nure.st.kpp.example.demo.dao.collectiondao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.nure.st.kpp.example.demo.dao.daointerface.RentalDAO;
import ua.nure.st.kpp.example.demo.entity.Customer;
import ua.nure.st.kpp.example.demo.entity.ProductInstance;
import ua.nure.st.kpp.example.demo.entity.Rental;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class CollectionRentalDAO implements RentalDAO {

    private static final Logger log = LoggerFactory.getLogger(CollectionRentalDAO.class);

    private static final List<Rental> rentals = new ArrayList<>();
    private static final IdGeneratorService idGenerator = new IdGeneratorService();
    private static final CollectionCustomerDAO customerDAO = new CollectionCustomerDAO();
    private static final CollectionProductInstanceDAO productInstanceDAO = new CollectionProductInstanceDAO();

    static {
        Customer customer1 = customerDAO.getCustomerById(1);
        Customer customer2 = customerDAO.getCustomerById(2);

        ProductInstance instance1 = productInstanceDAO.getProductInstanceById(1);
        ProductInstance instance2 = productInstanceDAO.getProductInstanceById(2);

        rentals.add(createRental(instance1, customer1, Date.valueOf("2025-01-01"), Date.valueOf("2025-01-05")));
        rentals.add(createRental(instance2, customer2, Date.valueOf("2025-01-03"), Date.valueOf("2025-01-07")));
    }

    private static Rental createRental(ProductInstance productInstance, Customer customer, Date rentalDate, Date returnDate) {
        Rental rental = new Rental(productInstance, customer, rentalDate, returnDate);
        rental.setId(idGenerator.nextId());
        rental.setRentalPrice(productInstance.getRentalPrice() * calculateDays(rentalDate, returnDate));
        return rental;
    }

    private static int calculateDays(Date start, Date end) {
        long difference = end.getTime() - start.getTime();
        return (int) (difference / (1000 * 60 * 60 * 24));
    }

    @Override
    public synchronized void createRental(Rental rental) {
        rental.setId(idGenerator.nextId());
        rental.setRentalPrice(rental.getProductInstance().getRentalPrice() * calculateDays(rental.getRentalDate(), rental.getReturnDate()));
        rentals.add(rental);
        log.debug("Rental added: {}", rental);
    }

    @Override
    public synchronized void updateRental(Rental rental) {
        for (Rental existingRental : rentals) {
            if (existingRental.getId() == rental.getId()) {
                existingRental.setProductInstance(rental.getProductInstance());
                existingRental.setCustomer(rental.getCustomer());
                existingRental.setRentalDate(rental.getRentalDate());
                existingRental.setReturnDate(rental.getReturnDate());
                existingRental.setRentalPrice(rental.getProductInstance().getRentalPrice() * calculateDays(rental.getRentalDate(), rental.getReturnDate()));
                log.debug("Rental updated: {}", existingRental);
                return;
            }
        }
        log.warn("Rental with ID {} not found for update", rental.getId());
    }

    @Override
    public synchronized void deleteRental(int id) {
        rentals.removeIf(rental -> rental.getId() == id);
        log.debug("Rental with ID {} deleted", id);
    }

    @Override
    public synchronized Rental getRentalById(int id) {
        for (Rental rental : rentals) {
            if (rental.getId() == id) {
                return rental;
            }
        }
        log.warn("Rental with ID {} not found", id);
        return null;
    }

    @Override
    public synchronized List<Rental> getRentalsByCustomerId(int customerId) {
        List<Rental> result = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getCustomer().getId() == customerId) {
                result.add(rental);
            }
        }
        return result;
    }

    @Override
    public synchronized List<Rental> getRentalsByInstanceId(int instanceId) {
        List<Rental> result = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getProductInstance().getId() == instanceId) {
                result.add(rental);
            }
        }
        return result;
    }

    @Override
    public synchronized List<Rental> getAllRentals() {
        log.debug("Found: {} rentals", rentals.size());
        return new ArrayList<>(rentals);
    }
}
