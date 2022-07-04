package ct.dao.taxes;

public class FlatTaxDS implements TaxDS
{
    @Override
    public float getImportTaxRate() {
        return 0.05f;
    }

    @Override
    public float getStateTaxRate(String state) {
        return 0.10f;
    }
}
