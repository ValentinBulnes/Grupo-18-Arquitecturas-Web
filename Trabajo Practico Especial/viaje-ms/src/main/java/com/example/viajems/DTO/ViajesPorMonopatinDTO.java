package com.example.viajems.DTO;

public class ViajesPorMonopatinDTO {
    private String idMonopatin;
    private Long totalViajes;

    public ViajesPorMonopatinDTO() {}

    public ViajesPorMonopatinDTO(String idMonopatin, Long totalViajes) {
        this.idMonopatin = idMonopatin;
        this.totalViajes = totalViajes;
    }

    // Getters y setters
    public String getIdMonopatin() {
        return idMonopatin;
    }

    public void setIdMonopatin(String idMonopatin) {
        this.idMonopatin = idMonopatin;
    }

    public Long getTotalViajes() {
        return totalViajes;
    }

    public void setTotalViajes(Long totalViajes) {
        this.totalViajes = totalViajes;
    }

}
