package mx.cenidet.projects.covid.generators;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.MappingException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class CovidIdGenerator implements IdentifierGenerator, Configurable {

    private String prefix;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String query = String.format("select %s from %s", 
            session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
            obj.getClass().getSimpleName());

		@SuppressWarnings("unchecked")
		Stream<String> ids = session.createQuery(query).stream();
		
        Long max = ids.map(o -> o.replace(prefix + "_", ""))
          .mapToLong(Long::parseLong)
          .max()
          .orElse(0L);

        return prefix + "_" + (max + 1);
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
    }
}