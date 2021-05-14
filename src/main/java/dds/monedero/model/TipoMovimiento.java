package dds.monedero.model;

public interface TipoMovimiento {

  public abstract boolean isDeposito();

  public abstract double calcularSaldo(double saldo, double monto);
}
