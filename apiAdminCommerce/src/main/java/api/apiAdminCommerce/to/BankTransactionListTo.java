package api.apiAdminCommerce.to;

import api.apiAdminCommerce.entity.BankTransactionEntity;
import api.apiAdminCommerce.entity.LicenseEntity;

public class BankTransactionListTo {

    private BankTransactionEntity bankTransactionEntity;

    private LicenseEntity licenseEntity;

    public BankTransactionListTo() {
    }

    public BankTransactionListTo(BankTransactionEntity bankTransactionEntity, LicenseEntity licenseEntity) {
        this.bankTransactionEntity = bankTransactionEntity;
        this.licenseEntity = licenseEntity;
    }

    public BankTransactionEntity getBankTransactionEntity() {
        return bankTransactionEntity;
    }

    public void setBankTransactionEntity(BankTransactionEntity bankTransactionEntity) {
        this.bankTransactionEntity = bankTransactionEntity;
    }

    public LicenseEntity getLicenseEntity() {
        return licenseEntity;
    }

    public void setLicenseEntity(LicenseEntity licenseEntity) {
        this.licenseEntity = licenseEntity;
    }
}
