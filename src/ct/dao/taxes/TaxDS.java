package ct.dao.taxes;

import java.math.BigDecimal;

public interface TaxDS
{
    public BigDecimal getStateTaxRate(String state);
    public BigDecimal getImportTaxRate();
}
