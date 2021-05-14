package dds.monedero.model;

public class Extraccion implements TipoMovimiento{
  public boolean isDeposito() {
    return false;
  }

  public double calcularSaldo(double saldo, double monto){
    return saldo - monto;
  }
}
