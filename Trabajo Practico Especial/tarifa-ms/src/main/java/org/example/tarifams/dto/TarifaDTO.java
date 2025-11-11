package org.example.tarifams.dto;

public class TarifaDTO {
    private Double precioPorKm;
    private Double precioPorMinuto;
    private Double tarifaExtraPorPausa;

    public TarifaDTO() {
    }

    public TarifaDTO(Double precioPorKm, Double precioPorMinuto, Double tarifaExtraPorPausa) {
        this.precioPorKm = precioPorKm;
        this.precioPorMinuto = precioPorMinuto;
        this.tarifaExtraPorPausa = tarifaExtraPorPausa;
    }

    public Double getPrecioPorKm() {
        return precioPorKm;
    }

    public void setPrecioPorKm(Double precioPorKm) {
        this.precioPorKm = precioPorKm;
    }

    public Double getPrecioPorMinuto() {
        return precioPorMinuto;
    }

    public void setPrecioPorMinuto(Double precioPorMinuto) {
        this.precioPorMinuto = precioPorMinuto;
    }

    public Double getTarifaExtraPorPausa() {
        return tarifaExtraPorPausa;
    }

    public void setTarifaExtraPorPausa(Double tarifaExtraPorPausa) {
        this.tarifaExtraPorPausa = tarifaExtraPorPausa;
    }
}
