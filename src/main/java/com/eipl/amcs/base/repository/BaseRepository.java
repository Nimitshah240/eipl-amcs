package com.eipl.amcs.base.repository;

import com.eipl.amcs.base.JsonAndTableBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends JsonAndTableBuilder, ID> extends JpaRepository<T, ID> {

    <S extends T> S customSave(S entity, String identityInfo);

    <S extends T> S customSaveForSync(S entity, String identityInfo);

    <S extends T> S customUpdate(S entity, String identityInfo);

    <S extends T> void customDelete(S entity, String identityInfo);

    void customDelete(ID id, String identityInfo);
}
