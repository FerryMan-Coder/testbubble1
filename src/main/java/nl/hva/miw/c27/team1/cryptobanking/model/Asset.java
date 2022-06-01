package nl.hva.miw.c27.team1.cryptobanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Asset {

    @JsonProperty("id")
    private String assetName;
    @JsonProperty("symbol")
    private String assetCode;
    @JsonProperty("current_price")
    private double rateInEuros;

    @JsonIgnore
    private final Logger logger = LogManager.getLogger(Asset.class);

    public Asset(String assetName, String assetCode, double rateInEuros) {
        this.assetName = assetName;
        this.assetCode = assetCode;
        this.rateInEuros = rateInEuros;
        logger.info("New all-args Asset");
    }

    public Asset(String assetCode) {
        this("",assetCode,0.0);
        logger.info("New Asset with assetCode");

    }

    public Asset() {
        logger.info("Empty Asset");
    }

    public String getAssetCode() {
        return assetCode;
    }

    public double getRateInEuros() {
        return rateInEuros;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public void setRateInEuros(double rateInEuros) {
        this.rateInEuros = rateInEuros;
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "assetName='" + assetName + '\'' +
                ", assetCode='" + assetCode + '\'' +
                ", rateInEuros=" + rateInEuros +
                '}';
    }
}
