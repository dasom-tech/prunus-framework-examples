package persistence.mybatis.mapper.h2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import persistence.mybatis.dto.LaptopReq;
import persistence.mybatis.entity.Laptop;

import java.util.List;

@Repository("h2.persistenceMybatisMapper")
public interface PersistenceMybatisMapper {

    List<Laptop> selectAll(LaptopReq laptopReq);

    List<Laptop> selectPageList(LaptopReq laptopReq, Pageable pageable);

    Page<Laptop> selectPage(LaptopReq laptopReq, Pageable pageable);

    void insert(Laptop laptop);

    void update(Laptop laptop);

    void delete(String id);
}
