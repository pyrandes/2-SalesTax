package ct.dao.taxes;

import java.math.BigDecimal;

public class FlatTaxDS implements TaxDS
{
    @Override
    public BigDecimal getImportTaxRate() {
        return new BigDecimal("0.05");
    }

    @Override
    public BigDecimal getStateTaxRate(String state) {
        return new BigDecimal("0.10");
    }
}
