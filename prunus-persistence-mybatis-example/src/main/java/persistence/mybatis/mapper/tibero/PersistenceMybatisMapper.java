package persistence.mybatis.mapper.tibero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import persistence.mybatis.dto.LaptopDto;
import persistence.mybatis.entity.Laptop;

import java.util.List;

@Repository("tibero.persistenceMybatisMapper")
public interface PersistenceMybatisMapper {

    List<Laptop> selectAll(LaptopDto laptopDto);

    List<Laptop> selectPageList(LaptopDto laptopDto, Pageable pageable);

    Page<Laptop> selectPage(LaptopDto laptopDto, Pageable pageable);

    void insert(Laptop laptop);

    void update(Laptop laptop);

    void delete(String id);
}
