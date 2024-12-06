package io.yody.yodemy.elearning.repository.impl;

import io.yody.yodemy.elearning.domain.constant.NextIdConst;
import io.yody.yodemy.elearning.repository.IDGeneratorRepository;
import org.nentangso.core.web.rest.errors.BadRequestAlertException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class IDGeneratorRepositoryImpl implements IDGeneratorRepository {

    public static final String ENTITY_NAME = "{entity}";
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final EntityManager entityManager;

    public IDGeneratorRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long getLastId(String key) {
        if (isRunning.get()) {
            throw new BadRequestAlertException("Lỗi kết nối khi lấy ID", ENTITY_NAME, "idgenerator");
        }
        String queryString = getQuery(key);
        try {
            isRunning.set(true);
            Query query = entityManager.createNativeQuery(queryString);
            BigInteger lastId = (BigInteger) query.getSingleResult();
            return lastId.longValue();
        } catch (NoResultException noResultException) {
            return 0L;
        } catch (Exception e) {
            throw new BadRequestAlertException("Lỗi kết nối khi lấy ID", ENTITY_NAME, "idgenerator");
        } finally {
            isRunning.set(false);
        }
    }

    private String getQuery(String key) {
        String query = "SELECT e.id FROM {entity} e ORDER BY e.id DESC LIMIT 1";
        switch (key) {
            case NextIdConst.NODE:
                return query.replace(ENTITY_NAME, "node");
            case NextIdConst.RULE:
                return query.replace(ENTITY_NAME, "rule");
            default:
                return query;
        }
    }
}
