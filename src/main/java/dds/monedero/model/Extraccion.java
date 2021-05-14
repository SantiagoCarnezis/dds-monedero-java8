package dds.monedero.model;

import java.math.BigDecimal;

public class Extraccion implements TipoMovimiento{
  public boolean isDeposito() {
    return false;
  }

  public BigDecimal calcularSaldo(BigDecimal saldo, BigDecimal monto){
    return saldo.subtract(monto);
  }
}
