package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.DockMilkType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DockMilkTypeRepository extends BaseRepository<DockMilkType, String> {

    @EntityGraph(attributePaths = {"dock", "milkType"})
    List<DockMilkType> findAllByDock(Dock dock);

    void deleteByDock(Dock dock);

    @Override
    @EntityGraph(attributePaths = {"dock", "milkType"})
    Optional<DockMilkType> findById(String id);
}
