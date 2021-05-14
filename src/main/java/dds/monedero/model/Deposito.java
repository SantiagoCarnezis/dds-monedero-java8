package dds.monedero.model;

import java.math.BigDecimal;

public class Deposito implements TipoMovimiento {

  public boolean isDeposito() {
    return true;
  }

  public BigDecimal calcularSaldo(BigDecimal saldo, BigDecimal monto){
    return saldo.add(monto);
  }
}
