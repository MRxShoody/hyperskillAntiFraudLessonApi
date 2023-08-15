package antifraud.business.security.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public abstract class securityService<T,R extends CrudRepository<T,Long>> {

    protected R repo;

    protected securityService(R repo){
        this.repo = repo;
    }

    public abstract T add(T t);

    public abstract boolean delete(String t);

    public abstract List<T> getAll();

    public abstract boolean isBlocked(String t);

}
