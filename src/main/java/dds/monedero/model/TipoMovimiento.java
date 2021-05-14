package dds.monedero.model;

import java.math.BigDecimal;

public interface TipoMovimiento {

  public abstract boolean isDeposito();

  public abstract BigDecimal calcularSaldo(BigDecimal saldo, BigDecimal monto);
}
