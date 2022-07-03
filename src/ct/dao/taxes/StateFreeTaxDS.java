package ct.dao.taxes;

import java.math.BigDecimal;

public class StateFreeTaxDS implements TaxDS
{
    @Override
    public BigDecimal getImportTaxRate() {
        return new BigDecimal("0.05");
    }

    @Override
    public BigDecimal getStateTaxRate(String state) {
        return state.startsWith("M") ? new BigDecimal("0.0") : new BigDecimal("0.10");
    }
}
