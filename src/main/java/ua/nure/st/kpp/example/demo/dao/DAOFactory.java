package ua.nure.st.kpp.example.demo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.nure.st.kpp.example.demo.dao.collectiondao.CollectionCustomerDAO;
import ua.nure.st.kpp.example.demo.dao.collectiondao.CollectionProductDAO;
import ua.nure.st.kpp.example.demo.dao.collectiondao.CollectionProductInstanceDAO;
import ua.nure.st.kpp.example.demo.dao.collectiondao.CollectionRentalDAO;
import ua.nure.st.kpp.example.demo.dao.daointerface.CustomerDAO;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductDAO;
import ua.nure.st.kpp.example.demo.dao.daointerface.ProductInstanceDAO;
import ua.nure.st.kpp.example.demo.dao.daointerface.RentalDAO;
import ua.nure.st.kpp.example.demo.dao.mysqldao.*;

@Configuration
public class DAOFactory {
    Logger log = LoggerFactory.getLogger(DAOFactory.class);

    @Bean(name = "CustomerDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "COLLECTION")
    public CustomerDAO getCollectionCustomerDAOInstance() {
        log.debug("MyCollectionCustomerDAO");
        return new CollectionCustomerDAO();
    }

    @Bean(name = "ProductDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "COLLECTION")
    public ProductDAO getCollectionProductDAOInstance() {
        log.debug("MyCollectionProductDAO");
        return new CollectionProductDAO();
    }

    @Bean(name = "ProductInstanceDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "COLLECTION")
    public ProductInstanceDAO getCollectionProductInstanceDAOInstance() {
        log.debug("MyCollectionProductInstanceDAO");
        return new CollectionProductInstanceDAO();
    }

    @Bean(name = "RentalDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "COLLECTION")
    public RentalDAO getCollectionRentalDAOInstance() {
        log.debug("CollectionRentalDAO");
        return new CollectionRentalDAO();
    }


    @Bean(name = "CustomerDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "MySQL")
    public CustomerDAO getMySqlCustomerDAOInstance(ConnectionManager cm) {
        log.debug("MySqlCustomerDAO");
        return new MySqlCustomerDAO(cm);
    }

    @Bean(name = "ProductDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "MySQL")
    public ProductDAO getMySqlProductDAOInstance(ConnectionManager cm) {
        log.debug("MySqlProductDAO");
        return new MySqlProductDAO(cm);
    }

    @Bean(name = "ProductInstanceDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "MySQL")
    public ProductInstanceDAO getMySqlProductInstanceDAOInstance(ConnectionManager cm) {
        log.debug("MySqlProductInstanceDAO");
        return new MySqlProductInstanceDAO(cm);
    }

    @Bean(name = "RentalDAO")
    @ConditionalOnProperty(name = "type", prefix = "database", havingValue = "MySQL")
    public RentalDAO getMySqlRentalDAOInstance(ConnectionManager cm) {
        log.debug("MySqlRentalDAO");
        return new MySqlRentalDAO(cm);
    }


    @Bean
    public ConnectionManager getConnectionManager(DAOConfig config) {
        return new MySqlConnectionManager(config);
    }
}
