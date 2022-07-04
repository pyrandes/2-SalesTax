package ct.dao.taxes;

public interface TaxDS {
    float getStateTaxRate(String state);

    float getImportTaxRate();
}
