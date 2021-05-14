package dds.monedero.model;

public class Deposito implements TipoMovimiento {

  public boolean isDeposito() {
    return true;
  }

  public double calcularSaldo(double saldo, double monto){
    return saldo + monto;
  }
}
