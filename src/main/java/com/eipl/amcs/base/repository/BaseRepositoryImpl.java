package com.eipl.amcs.base.repository;

import com.eipl.amcs.EiplAmcsAppRunner;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.sync.model.Broadcasted;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Base64;
import java.util.Optional;

public class BaseRepositoryImpl<T extends JsonAndTableBuilder, ID> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    private final EntityManager entityManager;
    private final ObjectMapper objMapper;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        objMapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @Override
    @Transactional
    public <S extends T> S customSave(S entity, String identityInfo) {
        S entityNew = super.save(entity);
        if (EiplAmcsAppRunner.books.containsKey(entityNew.getTableName())) {
            String json;
            try {
                json = objMapper.writeValueAsString(entity);
                LOGGER.info("Json of entity {} --- {}", entity.getClass(), json);
                Broadcasted broadcast;
                if (identityInfo == null || identityInfo.isEmpty()) {
                    broadcast = Broadcasted.prepareBroadcaste(json, "SOCIETY", null, entity.getTableName(), "INSERT",
                            null, null, null);
                } else {
                    String[] val = new String(Base64.getDecoder().decode(identityInfo)).split("#");
                    broadcast = Broadcasted.prepareBroadcaste(json, val[0], val[1], entity.getTableName(), "INSERT",
                            val[2], val[3], val[4]);
                }
                entityManager.persist(broadcast);
            } catch (JsonProcessingException e) {
                LOGGER.error("Error in json", e);
            }
        }
        return entityNew;
    }

    @Override
    @Transactional
    public <S extends T> S customSaveForSync(S entity, String identityInfo) {
//		S entityNew = super.save(entity);
        if (EiplAmcsAppRunner.books.containsKey(entity.getTableName())) {
            String json;
            try {
                json = objMapper.writeValueAsString(entity);
                LOGGER.info("Json of entity {} --- {}", entity.getClass(), json);
                Broadcasted broadcast;
                if (identityInfo == null || identityInfo.isEmpty()) {
                    broadcast = Broadcasted.prepareBroadcaste(json, "SOCIETY", null, entity.getTableName(), "INSERT",
                            null, null, null);
                } else {
                    String[] val = new String(Base64.getDecoder().decode(identityInfo)).split("#");
                    broadcast = Broadcasted.prepareBroadcaste(json, val[0], val[1], entity.getTableName(), "INSERT",
                            val[2], val[3], val[4]);
                }
                entityManager.persist(broadcast);
            } catch (JsonProcessingException e) {
                LOGGER.error("Error in json", e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public <S extends T> S customUpdate(S entity, String identityInfo) {
        Optional<T> _audit = findById((ID) entity.getId());
        if (_audit.isPresent()) {
            entityManager.persist(_audit.get().getAuditModel("UPDATE", entity.getUserInfo()));
        }
        S entityNew = super.save(entity);
        if (EiplAmcsAppRunner.books.containsKey(entityNew.getTableName())) {
            String json;
            try {
                json = objMapper.writeValueAsString(entity);
                LOGGER.info("Json of entity {} --- {}", entity.getClass(), json);
                Broadcasted broadcast;
                if (identityInfo == null || identityInfo.isEmpty()) {
                    broadcast = Broadcasted.prepareBroadcaste(json, "SOCIETY", null, entity.getTableName(), "UPDATE",
                            null, null, null);
                } else {
                    String[] val = new String(Base64.getDecoder().decode(identityInfo)).split("#");
                    broadcast = Broadcasted.prepareBroadcaste(json, val[0], val[1], entity.getTableName(), "UPDATE",
                            val[2], val[3], val[4]);
                }
                entityManager.persist(broadcast);
            } catch (JsonProcessingException e) {
                LOGGER.error("Error in json", e);
            }
        }
        return entityNew;
    }

    @Override
    @Transactional
    public <S extends T> void customDelete(S entity, String identityInfo) {
        entityManager.persist(entity.getAuditModel("DELETE", entity.getUserInfo()));
        super.delete(entity);
        if (EiplAmcsAppRunner.books.containsKey(entity.getTableName())) {
            String json;
            try {
                json = objMapper.writeValueAsString(entity);
                LOGGER.info("Json of entity {} --- {}", entity.getClass(), json);
                Broadcasted broadcast;
                if (identityInfo == null || identityInfo.isEmpty()) {
                    broadcast = Broadcasted.prepareBroadcaste(json, "SOCIETY", null, entity.getTableName(), "DELETE",
                            null, null, null);
                } else {
                    String[] val = new String(Base64.getDecoder().decode(identityInfo)).split("#");
                    broadcast = Broadcasted.prepareBroadcaste(json, val[0], val[1], entity.getTableName(), "DELETE",
                            val[2], val[3], val[4]);
                }
                entityManager.persist(broadcast);
            } catch (JsonProcessingException e) {
                LOGGER.warn("Error in json {}", entity.getClass());
                LOGGER.error("Error in json", e);
            }
        }
    }

    @Override
    @Transactional
    public void customDelete(ID id, String identityInfo) {
        customDelete(findById(id).get(), identityInfo);
    }

}
