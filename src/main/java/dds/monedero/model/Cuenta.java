package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void depositar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (cantidadDeDepositos() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los 3 depositos diarios");
    }

    agregarMovimiento(LocalDate.now(), cuanto, new Deposito());
  }


  public void extraer(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (saldoInsuficiente(cuanto)) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    agregarMovimiento(LocalDate.now(), cuanto, new Extraccion());
  }

  boolean saldoInsuficiente(double cuanto){
    return getSaldo() - cuanto < 0;
  }


  /*
   * Se puede notar que en poner y sacar hay repeticion de codigo ambos
   * comienan validando si el monto ingresado es positvo y terminan
   * agregando el movimiento. Se podria abstraer esa parte en una sola funcion
   */

  public void agregarMovimiento(LocalDate fecha, double cuanto, TipoMovimiento tipo) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, tipo);
    movimientos.add(movimiento);
    saldo = tipo.calcularSaldo(this.saldo, cuanto);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  /*
   * Se le deberia pasar a movimiento el mensaje fueExtraido(fecha), de
   * esa forma es mas entendible que es lo que se quiere filtar
   */

  public long cantidadDeDepositos(){
    return getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count();
  }

  public long cantidadDeExtracciones(){
    return getMovimientos().stream().filter(movimiento -> !movimiento.isDeposito()).count();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
