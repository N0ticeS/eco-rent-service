package ua.nure.st.kpp.example.demo.dao.collectiondao;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IdGeneratorService {
    final AtomicInteger id;

    public IdGeneratorService() {
        super();
        this.id = new AtomicInteger();
    }

    public Integer nextId() {
        return id.incrementAndGet();
    }
}
