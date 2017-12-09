package pl.wilanowskiartur.chat.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.wilanowskiartur.chat.models.LogModel;

@Repository
public interface LogRepository extends CrudRepository<LogModel, Integer>{



}
