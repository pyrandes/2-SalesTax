package ct.dao.taxes;

public class StateFreeTaxDS implements TaxDS
{
    @Override
    public float getImportTaxRate() {
        return 0.05f;
    }

    @Override
    public float getStateTaxRate(String state) {
        return state.startsWith("M") ? 0.0f : 0.10f;
    }
}
