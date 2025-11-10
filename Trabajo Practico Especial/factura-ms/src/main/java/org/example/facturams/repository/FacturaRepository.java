package org.example.facturams.repository;

import org.example.facturams.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT SUM(f.montoTotal) FROM Factura f WHERE YEAR(f.fechaEmision) = :anio AND MONTH(f.fechaEmision) BETWEEN :mesInicio AND :mesFin")
    Double totalFacturadoEnRango(@Param("anio") int anio,
                                 @Param("mesInicio") int mesInicio,
                                 @Param("mesFin") int mesFin);

}

