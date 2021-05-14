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

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
  }


  public void extraer(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
  }

  /*
  * El if de la linea 65 tambien se puede delegar en un metodo su condicion:
  * saldoInsuficiente(cuanto)
   */

  /*
   * El if de la linea 65 tambien se puede delegar en un metodo su condicion:
   * saldoInsuficiente(cuanto)
   */

  /*
   * Se deberia utilizar el metodo agregarMovimiento en vez de agregateA (mensaje
   * que entien la clase Movimiento)
   */

  /*
   * Se puede notar que en poner y sacar hay repeticion de codigo ambos
   * comienan validando si el monto ingresado es positvo y terminan
   * agregando el movimiento. Se podria abstraer esa parte en una sola funcion
   */

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
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
